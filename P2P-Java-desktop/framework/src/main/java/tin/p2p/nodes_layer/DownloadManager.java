package tin.p2p.nodes_layer;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class DownloadManager extends Thread {
    private static final DownloadManager instance = new DownloadManager();

    private HashMap<String, FileDownloadManager> filesDownloading = new HashMap<>();

    private Queue<Triple<String, Long, ByteBuffer>> receivedFilesFragment = new ConcurrentLinkedQueue<>();

    private Queue<Pair<String, Long>> requestsToSend = new ConcurrentLinkedQueue<>();


    private DownloadManager() {}

    public static DownloadManager getInstance() {
        if (!instance.isAlive())
            instance.start();

        return instance;
    }

    public Void addFileDownload(String fileName, String fileHash) {
        FileDTO fileInfo = RemoteFileListRepository.getInstance().getFileInfoByHash(fileHash).get();
        FileDownloadManager fileDownloadManager =
                new FileDownloadManager(fileName, fileHash, fileInfo.getSize(), this);
        // todo ograniczenie jezeli juz pobieramy
        filesDownloading.put(fileHash, fileDownloadManager);

        fileDownloadManager.start();
        return null;
    }


    public void onReceivedFileFragment(String fileHash, Long fileOffset, ByteBuffer fragmentData) {
        synchronized (instance) {
            receivedFilesFragment.add(Triple.of(fileHash, fileOffset, fragmentData));
            notifyAll(); // todo do weryfikacji
        }
    }

    public void onNewFileFragmentNeeded(String fileHash, Long fileOffset) {
        synchronized (instance) {
            requestsToSend.add(Pair.of(fileHash, fileOffset));
            notifyAll(); // todo do weryfikacji
        }
    }

    // todo reakacja na to jak dodano do kolejki

    @Override
    public void run() {
        while(true) {
            Triple<String, Long, ByteBuffer> fileFragment = receivedFilesFragment.poll();
            Pair<String, Long> fileFragmentRequest = requestsToSend.poll();
            if (fileFragment == null && fileFragmentRequest == null) {
                try {
                    synchronized (instance) {
                        wait();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {

                if (fileFragment != null) {
                    filesDownloading.get(fileFragment.getLeft()).onFileFragmentReceived(fileFragment.getMiddle(), fileFragment.getRight());
                    // todo jezeli nie ma w hashmapie
                }

                if (fileFragmentRequest != null) {
                    Optional<FileDTO> fileInfo = RemoteFileListRepository.getInstance().getFileInfoByHash(fileFragmentRequest.getLeft());
                    // todo, w fileDTO lista remoteNodow
                    if (fileInfo.isPresent() && fileInfo.get().getRemoteNode() != null) {
                        fileInfo.get().getRemoteNode().requestForFileFragment(
                                fileInfo.get().getHash(), fileFragmentRequest.getRight());
                    }
                }
            }
        }
    }
}

