package tin.p2p.serialization_layer;

import tin.p2p.nodes_layer.RemoteNode;

import java.nio.ByteBuffer;

public interface Input {
    void deserialize(byte opcode, ByteBuffer inputData);

    void setRemoteNodeReceiver(RemoteNode remoteNode);

    void terminate();
}