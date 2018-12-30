package tin.p2p.nodes_layer;

public class FileDTO {

    private RemoteNode remoteNode;

    private String name;
    private String ip;
    private String hash;
    private String size;

    public FileDTO(RemoteNode remoteNode, String name, String ip, String hash, String size) {
        this.remoteNode = remoteNode;
        this.name = name;
        this.ip = ip;
        this.hash = hash;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public String getIp() {
        return ip;
    }

    public String getHash() {
        return hash;
    }

    public String getSize() {
        return size;
    }

    public RemoteNode getRemoteNode() {
        return remoteNode;
    }
}
