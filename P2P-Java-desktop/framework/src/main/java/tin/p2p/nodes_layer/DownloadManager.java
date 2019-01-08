package tin.p2p.nodes_layer;

import tin.p2p.exceptions.UnavailableFileToDownloadException;
import tin.p2p.utils.Pair;
import tin.p2p.utils.Triple;
import tin.p2p.controller_layer.FrameworkController;

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

    public Void addFileDownload(String fileName, String fileHash) throws UnavailableFileToDownloadException {
        FileDTO fileInfo = RemoteFileListRepository.getInstance().getFileInfoByHash(fileHash);
        if (fileInfo != null) {
            if (filesDownloading.get(fileHash) == null) {
                FileDownloadManager fileDownloadManager = new FileDownloadManager(fileName, fileHash, fileInfo.getSize(), this);

                filesDownloading.put(fileHash, fileDownloadManager);

                fileDownloadManager.start();
            } else { // if is already downloading
                return null;
            }
        } else {
            throw new UnavailableFileToDownloadException(fileName);
        }
        return null;
    }


    public void onReceivedFileFragment(String fileHash, Long fileOffset, ByteBuffer fragmentData) {
        receivedFilesFragment.add(Triple.of(fileHash, fileOffset, fragmentData));
        synchronized (instance) {
            notifyAll();
        }
    }

    public void onNewFileFragmentNeeded(String fileHash, Long fileOffset) {
        requestsToSend.add(Pair.of(fileHash, fileOffset));
        synchronized (instance) {
            notifyAll();
        }
    }

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
                    FileDownloadManager fileDownloadManager = filesDownloading.get(fileFragment.getLeft());
                    if (fileDownloadManager != null) {
                        fileDownloadManager.onFileFragmentReceived(fileFragment.getMiddle(), fileFragment.getRight());
                    }
                }

                if (fileFragmentRequest != null) {
                    FileDTO fileInfo = RemoteFileListRepository.getInstance().getFileInfoByHash(fileFragmentRequest.getLeft());

                    if (fileInfo != null && fileInfo.getRemoteNodes() != null) {

                        List<RemoteNode> remoteNodesHavingFile = fileInfo.getRemoteNodes();

                        if (remoteNodesHavingFile.size() > 0) {
                            Random rnd = new Random();
                            int i = rnd.nextInt(remoteNodesHavingFile.size());

                            remoteNodesHavingFile.get(i).requestForFileFragment(
                                    fileInfo.getHash(), fileFragmentRequest.getRight());
                        }
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

        FrameworkController.getInstance().onDownloadingFinished(fileDownloadManager.getFileName());
    }
}

