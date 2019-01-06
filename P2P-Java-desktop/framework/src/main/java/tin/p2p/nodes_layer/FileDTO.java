package tin.p2p.nodes_layer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FileDTO implements Comparable {

    private List<RemoteNode> remoteNodes = new ArrayList<>();

    private String name;
    private String ip;
    private String hash;
    private Long size;

    public FileDTO(RemoteNode remoteNode, String name, String ip, String hash, Long size) {
        this.remoteNodes.add(remoteNode);
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

    public FileDTO(ArrayList<String> fileParams, RemoteNode remoteNode) {
        this.name = fileParams.get(0);
        this.hash = fileParams.get(1);
        this.size = Long.valueOf(fileParams.get(2));
        this.remoteNodes.add(remoteNode);
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

    public List<RemoteNode> getRemoteNodes() {
        return remoteNodes;
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

    public void addRemoteNodeOwner(RemoteNode remoteNode) {
        this.remoteNodes.add(remoteNode);
    }
}
