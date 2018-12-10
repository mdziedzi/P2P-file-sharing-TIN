package tin.p2p.socketLayer;

import tin.p2p.ThreadManager;

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

    private static ServerSocket createAndConfigureSocketForListening(ThreadManager threadManager) {
        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            e.printStackTrace();
            threadManager.notifyError(e);
        }

        return serverSocket;
    }

    public static void createNewNet(ThreadManager threadManager) {
        createAndConfigureSocketForListening(threadManager);
        try {
            Socket socket = serverSocket.accept();
        } catch (IOException e) {
            e.printStackTrace();
            threadManager.notifyError(e);
        }

    }

    public static void onInterruptedException(ThreadManager threadManager) {
        if (!serverSocket.isClosed()) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                threadManager.notifyError(e);
                e.printStackTrace();
            }
        }
    }



}

