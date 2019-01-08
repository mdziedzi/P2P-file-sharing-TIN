package tin.p2p.socket_layer.connection;

import tin.p2p.serialization_layer.DeserializedObject;
import tin.p2p.serialization_layer.Deserializer;
import tin.p2p.socket_layer.SocketManager;

import java.io.IOException;
import java.net.Socket;

public class Receiver {
    private Socket socket;


    private Receiver(Socket socket) {
        this.socket = socket;
    }

    public static Receiver create(Socket socket, RemoteNode remoteNode) {
        Receiver receiver = new Receiver(socket);
        Thread thread = new Thread(() -> {
            try {
                receiver.startListening(socket);
            } catch (IOException e) {
                e.printStackTrace();
                remoteNode.onConnectionLost();
            }
        });
        thread.start();
        return receiver;
    }

    private void startListening(Socket socket) throws IOException {
        SocketManager.listen(socket, this);

    }

    public void onNewDataReceived(byte[] receivedData) {
        for(int i=0; i< receivedData.length ; i++) {
            System.out.print(receivedData[i] +" ");
        }
        DeserializedObject deserializedObject = Deserializer.deserialize(receivedData);
        //todo
    }
}

