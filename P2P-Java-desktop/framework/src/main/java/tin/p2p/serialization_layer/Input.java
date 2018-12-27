package tin.p2p.serialization_layer;

import tin.p2p.nodes_layer.RemoteNode;

public interface Input {
    void deserialize(byte opcode, byte[] inputData);

    void setRemoteNodeReceiver(RemoteNode remoteNode);
}