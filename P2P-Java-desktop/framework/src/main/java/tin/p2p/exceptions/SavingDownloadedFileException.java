package tin.p2p.exceptions;

public class SavingDownloadedFileException extends RuntimeException {
    private String fileName;

    public SavingDownloadedFileException(Throwable e, String fileName) {
        this.fileName = fileName;
        e.printStackTrace();
    }

    public String getFileName() {
        return fileName;
    }
}

