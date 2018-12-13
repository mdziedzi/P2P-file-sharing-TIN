package tin.p2p;

import tin.p2p.model.RemoteNode;
import tin.p2p.serialization.ConnectionSerialisedObject;
import tin.p2p.serialization.Serializer;
import tin.p2p.socketLayer.SocketManager;

import java.net.Socket;

public class Sender {
    private Socket socket;


    public void connectToNode(RemoteNode remoteNode, ConnectionSerialisedObject serialisedObject) {
        Thread thread = new Thread(() -> SocketManager.send(remoteNode.getAddress(), serialisedObject));
    }
}

