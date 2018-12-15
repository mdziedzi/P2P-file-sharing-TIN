package tin.p2p;

import tin.p2p.exception.ConnectionToNetException;
import tin.p2p.model.RemoteNode;
import tin.p2p.socketLayer.SocketManager;

import java.io.IOException;
import java.net.Socket;

public class Sender {
    private Socket socket;


    public void connectToNode(RemoteNode remoteNode, SerializedObject serializedObject) {
        try {
            socket = SocketManager.connect(remoteNode.getAddress());
            sendJoinNetworkRequest(socket, serializedObject);
            remoteNode.setReceiver(Receiver.create(socket));
        } catch (IOException e) {
            e.printStackTrace();
            throw new ConnectionToNetException();
        }
    }

    private void sendJoinNetworkRequest(Socket socket, SerializedObject serializedObject) throws IOException {
        SocketManager.send(socket, serializedObject);
    }
}

