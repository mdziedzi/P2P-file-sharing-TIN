package tin.p2p.socket_layer;

import tin.p2p.serialization_layer.SerializedObject;
import tin.p2p.socket_layer.connection.Receiver;
import tin.p2p.utils.Constants;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;

public class SocketManager {
    private final static int PORT = 8888;
    private static ServerSocket serverSocket;

    public static void joinToNetwork(String nodeName, InetAddress address) throws IOException {
        Socket socket = new Socket(address, PORT);


        socket.close();
    }

    private static ServerSocket createAndConfigureSocketForListening(RemoteNodesRepository remoteNodesRepository) {
        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            e.printStackTrace();
            remoteNodesRepository.notifyError(e);
        }

        return serverSocket;
    }

    public static void createNewNet(RemoteNodesRepository remoteNodesRepository) {
        createAndConfigureSocketForListening(remoteNodesRepository);
        try {
            Socket socket = serverSocket.accept();
        } catch (IOException e) {
            e.printStackTrace();
            remoteNodesRepository.notifyError(e);
        }

    }

    public static void onInterruptedException(RemoteNodesRepository remoteNodesRepository) {
        if (!serverSocket.isClosed()) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                remoteNodesRepository.notifyError(e);
                e.printStackTrace();
            }
        }
    }

    public static Socket connect(InetAddress address) throws IOException {
        Socket socket = new Socket();
        SocketAddress socketAddress = new InetSocketAddress(address, Constants.MAIN_APP_PORT);
        socket.connect(socketAddress);
        return socket;
    }

    public static void send(Socket socket, SerializedObject serializedObject) throws IOException {
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        dos.writeInt(serializedObject.getDataLength());
        dos.write(serializedObject.getData());
    }

    public static void listen(Socket socket, Receiver receiver) throws IOException {
        DataInputStream dis = new DataInputStream(socket.getInputStream());
        int length = dis.readInt();
        if (length > 0) {
            byte[] receivedData = new byte[length];
            dis.readFully(receivedData, 0, receivedData.length);
            receiver.onNewDataReceived(receivedData);
        }

    }
}

