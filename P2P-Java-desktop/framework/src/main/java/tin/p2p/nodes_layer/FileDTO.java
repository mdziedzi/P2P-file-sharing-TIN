package tin.p2p.nodes_layer;

public class FileDTO implements Comparable {

    private RemoteNode remoteNode;

    private String name;
    private String ip;
    private String hash;
    private Long size;

    public FileDTO(RemoteNode remoteNode, String name, String ip, String hash, Long size) {
        this.remoteNode = remoteNode;
        this.name = name;
        this.ip = ip;
        this.hash = hash;
        this.size = size;
    }

    public FileDTO(String fileName, String fileHash, Long fileSize) {
        this.name = fileName;
        this.hash = fileHash;
        this.size = fileSize;
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

    public Long getSize() {
        return size;
    }

    public RemoteNode getRemoteNode() {
        return remoteNode;
    }

    @Override
    public int compareTo(Object o) {
        return this == o ? 0 : 1;
    }

    @Override
    public String toString() {
        return "FileDTO{" +
                "name='" + name + '\'' +
                ", ip='" + ip + '\'' +
                ", hash='" + hash + '\'' +
                ", size=" + size +
                '}';
    }
}
