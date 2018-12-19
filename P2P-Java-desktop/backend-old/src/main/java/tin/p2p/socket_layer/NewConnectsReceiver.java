package tin.p2p.socket_layer;

import tin.p2p.nodes_controller_layer.RemoteNodesController;
import tin.p2p.serialization_layer.DeserializedObject;
import tin.p2p.serialization_layer.Deserializer;
import tin.p2p.serialization_layer.SerializedObject;
import tin.p2p.socket_layer.connection.Receiver;
import tin.p2p.socket_layer.connection.RemoteNode;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class NewConnectsReceiver {
    private RemoteNodesController remoteNodesController;
    private ServerSocket serverSocket;

    public NewConnectsReceiver(RemoteNodesController remoteNodesController) {
        this.remoteNodesController = remoteNodesController;
    }

    public void startListening() {
        Thread thread = new Thread(() -> {
            try {
                SocketManager.listenUnknownNodes(this);
            } catch (IOException e) {
                e.printStackTrace();
//                remoteNodesController.onConnectionLost(); //todo
            }
        });
        thread.start();
    }

    public void onNewDataReceived(byte[] receivedData, Socket socket) {

        DeserializedObject deserializedObject = Deserializer.deserialize(receivedData);
        RemoteNode remoteNode = RemoteNodesRepository.getInstance().getNewRemoteNode(socket.getInetAddress());
        remoteNode.setSenderSocket(socket);
        RemoteNodesController.getInstance().onNewDataReceived(deserializedObject, remoteNode);
    }

}

