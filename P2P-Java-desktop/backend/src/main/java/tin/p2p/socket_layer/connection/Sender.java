package tin.p2p.socket_layer.connection;

import tin.p2p.exceptions.ConnectionToNetException;
import tin.p2p.serialization_layer.SerializedObject;
import tin.p2p.socket_layer.SocketManager;

import java.io.IOException;
import java.net.Socket;

public class Sender {
    private Socket socket;


    public void connectToNode(RemoteNode remoteNode, SerializedObject serializedObject) {
        try {
            socket = SocketManager.connect(remoteNode.getAddress());
            sendJoinNetworkRequest(socket, serializedObject);
            remoteNode.setReceiver(Receiver.create(socket, remoteNode));
        } catch (IOException e) {
            e.printStackTrace();
            throw new ConnectionToNetException();
        }
    }

    private void sendJoinNetworkRequest(Socket socket, SerializedObject serializedObject) throws IOException {
        SocketManager.send(socket, serializedObject);
    }
}

