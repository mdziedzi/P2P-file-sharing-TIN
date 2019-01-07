package tin.p2p.nodes_layer;

import org.apache.commons.lang3.tuple.Pair;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static tin.p2p.utils.Constants.MAXIMUM_FILE_FRAGMENT_SIZE;

public class FileDownloadManager extends Thread {

    private volatile boolean running = true;

    private DownloadManager downloadManager;

    private String fileHash;
    private String fileName;
    private RandomAccessFile randomAccessFile;

    // todo tablica ze statusami o fragmentach pliku, tworzona na początku, aktualizowana
    private ArrayList<FileFragmentInfo> fileFragmentsInfos = new ArrayList<>();
    private Queue<Pair<Long, ByteBuffer>> receivedFileFragments = new ConcurrentLinkedQueue<>();


    public FileDownloadManager(String fileName, String fileHash, Long fileSize, DownloadManager downloadManager) {
        this.downloadManager = downloadManager;
        this.fileHash = fileHash;
        this.fileName = fileName;

        long offset = 0;
        do {
            fileFragmentsInfos.add(new FileFragmentInfo(offset));
            offset += MAXIMUM_FILE_FRAGMENT_SIZE;
        } while (offset < fileSize);

        try {
            randomAccessFile = new RandomAccessFile(fileName, "rws");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (running) {
            Pair<Long, ByteBuffer> receivedFragment = receivedFileFragments.poll();

            //todo
            Collection<FileFragmentInfo> nFragmentsToDownload = findNFragmentsToDownload(5);

            nFragmentsToDownload.forEach(fragmentInfo -> {
                downloadManager.onNewFileFragmentNeeded(fileHash, fragmentInfo.getOffset());
                fragmentInfo.setRequestSend(true);
            });

            if (receivedFragment != null) {
                // a może zamiast przesyłać offset to przesyłać numer bloku
                Optional<FileFragmentInfo> fileFragmentInfo = fileFragmentsInfos.stream()
                        .filter(fragmentInfo -> fragmentInfo.getOffset().equals(receivedFragment.getLeft())).findFirst();
                if (fileFragmentInfo.isPresent()) {
                    writeFragmentToFile(fileFragmentInfo.get(), receivedFragment.getRight());
                    fileFragmentInfo.get().setDownloaded(true);

                    if (isWholeFileDownloaded()) {
                        downloadManager.unregister(this);
                        return;
                    }
                    //todo inteligentniejsze zlecanie pobrania
                }
            } else {
                try {
                    synchronized (receivedFileFragments) {
                        receivedFileFragments.wait();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean isWholeFileDownloaded() {
        return fileFragmentsInfos.stream().allMatch(FileFragmentInfo::isDownloaded);
    }

    public void onFileFragmentReceived(Long fileOffset, ByteBuffer fileFragmentData) {
        synchronized (receivedFileFragments) {
            receivedFileFragments.add(Pair.of(fileOffset, fileFragmentData));
            receivedFileFragments.notifyAll();
        }
    }

    private void writeFragmentToFile(FileFragmentInfo fragmentInfo, ByteBuffer fileFragmentData) {
        try {
            randomAccessFile.seek(fragmentInfo.getOffset());
            randomAccessFile.write(fileFragmentData.array(), 0, fileFragmentData.array().length);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Collection<FileFragmentInfo> findNFragmentsToDownload(int fragmentsNumber) {
        List<FileFragmentInfo> notDownloadedFragments =
                fileFragmentsInfos.stream().filter(fragmentInfo -> !fragmentInfo.isDownloaded()).collect(Collectors.toList());

        List<FileFragmentInfo> notDownloadedAndNotRequested =
                notDownloadedFragments.stream().filter(fragmentInfo -> !fragmentInfo.isRequestSend()).collect(Collectors.toList());

        return notDownloadedAndNotRequested;
    }

    public void terminate() {
        running = false;
        synchronized (receivedFileFragments) {
            receivedFileFragments.notifyAll();
        }
    }

    public String getFileHash() {
        return fileHash;
    }
}

