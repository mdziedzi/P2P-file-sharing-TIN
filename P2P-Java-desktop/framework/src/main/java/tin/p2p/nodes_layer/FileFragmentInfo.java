package tin.p2p.nodes_layer;

public class FileFragmentInfo {
    private Long offset;
    private boolean downloaded = false;
    private boolean requestSend = false;

    public FileFragmentInfo(Long offset, boolean downloaded, boolean requestSend) {
        this.offset = offset;
        this.downloaded = downloaded;
        this.requestSend = requestSend;
    }

    public FileFragmentInfo(Long offset) {
        this.offset = offset;
    }

    public Long getOffset() {
        return offset;
    }

    public boolean isDownloaded() {
        return downloaded;
    }

    public boolean isRequestSend() {
        return requestSend;
    }

    public void setDownloaded(boolean downloaded) {
        this.downloaded = downloaded;
    }

    public void setRequestSend(boolean requestSend) {
        this.requestSend = requestSend;
    }
}

