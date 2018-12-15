package tin.p2p.socket_layer;

import tin.p2p.controller_layer.Controller;
import tin.p2p.socket_layer.connection.RemoteNode;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentSkipListSet;

public class RemoteNodesRepository {
    private static RemoteNodesRepository instance;

//    private static ConcurrentSkipListSet<Thread> threads; do zastanowienia czy potrzebne
    private static Controller controller;
    private static ConcurrentSkipListSet<RemoteNode> remoteNodes;

    private RemoteNodesRepository() {}

    public static synchronized RemoteNodesRepository getInstance() {
        if (instance == null) {
            instance = new RemoteNodesRepository();
            controller = Controller.getInstance();
        }
        return instance;
    }


    public RemoteNode getNewRemoteNode(String ip) throws UnknownHostException {
        RemoteNode remoteNode = new RemoteNode();
        remoteNode.setAddress(InetAddress.getByName(ip));
        return remoteNode;
    }

    public void notifyError(IOException e) {
        System.err.println(e.getMessage());
    }
}
