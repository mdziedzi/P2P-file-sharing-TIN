package tin.p2p.nodes_layer;

public interface SenderInterface {
    void connectToRemoteNodeOfTheSameNet();

    Void connectToNetByIp(String password);

    Void requestForFileList();

    void requestForFileFragment(String fileHash, Long fileOffset);

    void terminate();
}

