package tin.p2p.socketLayer;

import tin.p2p.RemoteNodesRepository;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

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


    public static void send(InetAddress address, ConnectionSerialisedObject serialisedObject) {
    }
}

