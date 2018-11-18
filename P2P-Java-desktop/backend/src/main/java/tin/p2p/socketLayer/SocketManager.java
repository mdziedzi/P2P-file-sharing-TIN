package tin.p2p.socketLayer;

import java.io.IOException;
import java.net.*;
import java.util.Enumeration;

public class SocketManager {
    private final static int PORT = 8888;

    private ServerSocket createAndConfigureSocketForListening() {
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return serverSocket;
    }

    public static void joinToNetwork(String nodeName, InetAddress address) throws IOException {
        Socket socket = new Socket(address, PORT);

        socket.close();
    }



}

