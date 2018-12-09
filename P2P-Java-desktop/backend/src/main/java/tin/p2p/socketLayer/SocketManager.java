package tin.p2p.socketLayer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketManager {
    private final static int PORT = 8888;

    public static void joinToNetwork(String nodeName, InetAddress address) throws IOException {
        Socket socket = new Socket(address, PORT);


        socket.close();
    }

    private ServerSocket createAndConfigureSocketForListening() {
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            e.printStackTrace();
            threadManager.notifyError()
        }

        return serverSocket;
    }



}

