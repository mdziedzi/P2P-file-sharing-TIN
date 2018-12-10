package tin.p2p;

import tin.p2p.controller.Controller;
import tin.p2p.model.Connection;
import tin.p2p.socketLayer.SocketManager;

import java.util.concurrent.ConcurrentSkipListSet;

public class ThreadManager {

    private ConcurrentSkipListSet<Thread> threads;
    private Controller controller;
    private ConcurrentSkipListSet<Connection> connections;

    public ThreadManager() {
        controller = Controller.getInstance();
    }

    public void connectToNode() {
//        threads.add(new Thread(this)); // w ty watku jest polaczenie

    }

    public void createNewNet() {
        Thread thread = new Thread(() ->
                SocketManager.createNewNet(ThreadManager.this));

        threads.add(thread);
        thread.start();
    }

    public void notifyError(Exception e) {
        System.err.println(e.getMessage());
    }
}
