package tin.p2p;

import tin.p2p.controller.Controller;
import tin.p2p.model.Connection;
import tin.p2p.model.RemoteNode;
import tin.p2p.socketLayer.SocketManager;

import java.util.concurrent.ConcurrentSkipListSet;

public class RemoteNodesRepository {
    private static RemoteNodesRepository instance;

    private static ConcurrentSkipListSet<Thread> threads;
    private static Controller controller;
    private static ConcurrentSkipListSet<RemoteNode> remoteNodes;

    private RemoteNodesRepository() {}

    public static synchronized RemoteNodesRepository getInstance() {
        //todo singleton
    }
    public void connectToNode() {
//        threads.add(new Thread(this)); // w ty watku jest polaczenie

    }

    public void createNewNet() {
        Thread thread = new Thread(() ->
                SocketManager.createNewNet(RemoteNodesRepository.this));
        thread.
        threads.add(thread);
        thread.start();
    }

    public void notifyError(Exception e) {
        System.err.println(e.getMessage());
    }
}
