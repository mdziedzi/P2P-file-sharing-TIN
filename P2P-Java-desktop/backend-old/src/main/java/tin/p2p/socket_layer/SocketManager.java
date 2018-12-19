package tin.p2p.socket_layer;

import tin.p2p.serialization_layer.SerializedObject;
import tin.p2p.socket_layer.connection.Receiver;
import tin.p2p.utils.Constants;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;

public class SocketManager {
//    private final static int PORT = 8888;
//    private static ServerSocket serverSocket;

//    public static void joinToNetwork(String nodeName, InetAddress address) throws IOException {
//        Socket socket = new Socket(address, PORT);
//
//
//        socket.close();
//    }

//    private static ServerSocket createAndConfigureSocketForListening(RemoteNodesRepository remoteNodesRepository) {
//        try {
//            serverSocket = new ServerSocket(PORT);
//        } catch (IOException e) {
//            e.printStackTrace();
//            remoteNodesRepository.notifyError(e);
//        }
//
//        return serverSocket;
//    }
//
//    public static void createNewNet(RemoteNodesRepository remoteNodesRepository) {
//        createAndConfigureSocketForListening(remoteNodesRepository);
//        try {
//            Socket socket = serverSocket.accept();
//        } catch (IOException e) {
//            e.printStackTrace();
//            remoteNodesRepository.notifyError(e);
//        }
//
//    }

//    public static void onInterruptedException(RemoteNodesRepository remoteNodesRepository) {
//        if (!serverSocket.isClosed()) {
//            try {
//                serverSocket.close();
//            } catch (IOException e) {
//                remoteNodesRepository.notifyError(e);
//                e.printStackTrace();
//            }
//        }
//    }

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
        while (true) {
            byte length = dis.readByte();
            System.out.println(length);
            if (length > 0) {
                System.out.println("1B");
                byte[] receivedData = new byte[length];
                dis.readFully(receivedData, 0, receivedData.length);
                System.out.println("2B");
                for(int i=0; i< receivedData.length ; i++) {
                    System.out.print(receivedData[i] +" ");
                }
                System.out.println("3B");
                receiver.onNewDataReceived(receivedData);
                System.out.println("4B");
            }
        }
    }

    public static void listenUnknownNodes(NewConnectsReceiver newConnectsReceiver) throws IOException {

        ServerSocket serverSocket = new ServerSocket(Constants.MAIN_APP_PORT);
        while (true) {
            Socket socket = serverSocket.accept();

            DataInputStream dis = new DataInputStream(socket.getInputStream());
            int length = dis.readInt();
            System.out.println(length);
            if (length > 0) {
                System.out.println("3A");
                byte[] receivedData = new byte[length];
                dis.read(receivedData, 0, receivedData.length);
                for(int i=0; i< receivedData.length ; i++) {
                    System.out.print(receivedData[i] +" ");
                }
                newConnectsReceiver.onNewDataReceived(receivedData, socket);

            }
        }

    }


}

