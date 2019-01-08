package tin.p2p.exceptions;

public class UnavailableFileToDownloadException extends RuntimeException {
    private String fileName;

    public UnavailableFileToDownloadException(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}

