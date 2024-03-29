package tin.p2p.socket_layer;

import tin.p2p.controller_layer.Controller;
import tin.p2p.socket_layer.connection.RemoteNode;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentSkipListSet;

public class RemoteNodesRepository {
    private static RemoteNodesRepository instance;

//    private static ConcurrentSkipListSet<Thread> threads; do zastanowienia czy potrzebne
    private static Controller controller;
    private static ConcurrentSkipListSet<RemoteNode> remoteNodes = new ConcurrentSkipListSet<>();

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
        register(remoteNode);
        return remoteNode;
    }

    public RemoteNode getNewRemoteNode(InetAddress inetAddress) {
        RemoteNode remoteNode = new RemoteNode();
        remoteNode.setAddress(inetAddress);
        register(remoteNode);
        return remoteNode;
    }


    public void unregister(RemoteNode remoteNode) {
        remoteNodes.remove(remoteNode);
    }

    private void register(RemoteNode remoteNode) {
        remoteNodes.add(remoteNode);
    }
}
