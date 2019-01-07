package tin.p2p.nodes_layer;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;

public class DownloadManager extends Thread {
    private static final DownloadManager instance = new DownloadManager();

    private volatile boolean running = true;

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
        FileDTO fileInfo = RemoteFileListRepository.getInstance().getFileInfoByHash(fileHash);
        FileDownloadManager fileDownloadManager = new FileDownloadManager(fileName, fileHash, fileInfo.getSize(), this);
        // todo ograniczenie jezeli juz pobieramy
        filesDownloading.put(fileHash, fileDownloadManager);

        fileDownloadManager.start();
        return null;
    }


    public void onReceivedFileFragment(String fileHash, Long fileOffset, ByteBuffer fragmentData) {
        receivedFilesFragment.add(Triple.of(fileHash, fileOffset, fragmentData));
        synchronized (instance) {
            notifyAll(); // todo do weryfikacji
        }
    }

    public void onNewFileFragmentNeeded(String fileHash, Long fileOffset) {
        requestsToSend.add(Pair.of(fileHash, fileOffset));
        synchronized (instance) {
            notifyAll(); // todo do weryfikacji
        }
    }

    // todo reakacja na to jak dodano do kolejki

    @Override
    public void run() {
        while(running) {
            Triple<String, Long, ByteBuffer> fileFragment = receivedFilesFragment.poll();
            Pair<String, Long> fileFragmentRequest = requestsToSend.poll();
            if (fileFragment == null && fileFragmentRequest == null) {
                try {
                    synchronized (instance) {
                        instance.wait();
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
                    FileDTO fileInfo = RemoteFileListRepository.getInstance().getFileInfoByHash(fileFragmentRequest.getLeft());
                    // todo, w fileDTO lista remoteNodow
                    if (fileInfo != null && fileInfo.getRemoteNodes() != null) {
                        //todotodo
                        List<RemoteNode> remoteNodesHavingFile = fileInfo.getRemoteNodes();

                        Random rnd = new Random();
                        int i = rnd.nextInt(remoteNodesHavingFile.size());

                        remoteNodesHavingFile.get(i).requestForFileFragment(
                                fileInfo.getHash(), fileFragmentRequest.getRight());
                    }
                }
            }
        }
    }

    public void terminate() {
        filesDownloading.values().forEach(FileDownloadManager::terminate);
        running = false;
        synchronized (instance) {
            instance.notifyAll();
        }
    }

    public void unregister(FileDownloadManager fileDownloadManager) {
        filesDownloading.remove(fileDownloadManager.getFileHash());
    }
}

