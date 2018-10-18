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

    private InetAddress getBroadcastAddress() {
        Enumeration<NetworkInterface> interfaces = null;

        try {
            interfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            e.printStackTrace();
        }

        if (interfaces != null) {

            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();

                try {
                    if (networkInterface.isLoopback()) {
                        continue;    // Do not want to use the loopback interface.
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }

                for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
                    InetAddress broadcast = interfaceAddress.getBroadcast();
                    if (broadcast == null) {
                        continue;
                    }

                    return broadcast;
                }
            }
        }
        return InetAddress.getLoopbackAddress();
    }

}

