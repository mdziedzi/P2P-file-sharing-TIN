package tin.p2p.resourceLayer;

import tin.p2p.socketLayer.SocketManager;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class RemoteResourceManager {
    private static RemoteResourceManager instance;

    private RemoteResourceManager() {}

    public static synchronized RemoteResourceManager getInstance() {
        if (instance == null)
            instance = new RemoteResourceManager();

        return instance;
    }

    public void joinToNetwork(String nodeName, String nodeIp) {
        InetAddress nodeAddress = null;
        try {
            nodeAddress = InetAddress.getByName(nodeIp);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        InetAddress finalNodeAddress = nodeAddress;

        Thread thread = new Thread() {
            public void run() {
                System.out.println("Start thread");
                try {
                    SocketManager.joinToNetwork(nodeName, finalNodeAddress);
                } catch (ConnectException e) {
                    System.err.println("Error durring connecting to network");
                } catch (IOException e) {
                    System.err.println("Error in socket manager");
                }
            }
        };
        thread.run();
    }
} 

