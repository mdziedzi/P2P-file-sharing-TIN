package tin.p2p.socket_layer;

import tin.p2p.nodes_controller_layer.RemoteNodesController;
import tin.p2p.serialization_layer.Deserializer;
import tin.p2p.serialization_layer.SerializedObject;

import java.io.IOException;
import java.net.ServerSocket;

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

    public void onNewDataReceived(byte[] receivedData) {
        SerializedObject serializedObject = Deserializer.deserialize(receivedData);
        RemoteNodesController.getInstance().onNewDataReceived(serializedObject);
    }
}

