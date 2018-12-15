package tin.p2p.model;

import tin.p2p.Receiver;
import tin.p2p.Sender;
import tin.p2p.SerializedObject;
import tin.p2p.serialization.Serializer;

import java.net.InetAddress;
import java.util.concurrent.ConcurrentLinkedQueue;

public class RemoteNode {
    private InetAddress address;
    private Sender sender;
    private Receiver receiver;
    private ConcurrentLinkedQueue taskQueue;



    public RemoteNode() {
        this.sender = new Sender();
    }

    public InetAddress getAddress() {
        return address;
    }

    public void setAddress(InetAddress address) {
        this.address = address;
    }

    public Void connect() {
        SerializedObject serialisedObject = Serializer.getConnectionToNetObject();
        sender.connectToNode(this, serialisedObject);
        return null;

    }

    public void setReceiver(Receiver receiver) {
        this.receiver = receiver;
    }
}

