package tin.p2p.model;

import tin.p2p.Receiver;
import tin.p2p.Sender;
import tin.p2p.exception.ConnectionToNetException;
import tin.p2p.serialization.ConnectionSerialisedObject;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

public class RemoteNode {
    private InetAddress address;
    private Sender sender;
    private Receiver receiver;
    private ConcurrentLinkedQueue taskQueue;



    public RemoteNode() {
        this.sender = new Sender();
        this.receiver = new Receiver();


    }

    public InetAddress getAddress() {
        return address;
    }

    public void setAddress(InetAddress address) {
        this.address = address;
    }

    public Void connect() {
        sender.connectToNode(this, new ConnectionSerialisedObject());
        throw new ConnectionToNetException();

    }
}

