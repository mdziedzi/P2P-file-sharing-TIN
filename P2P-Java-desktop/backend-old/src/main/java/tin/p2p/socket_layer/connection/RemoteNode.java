package tin.p2p.socket_layer.connection;

import tin.p2p.serialization_layer.SerializedObject;
import tin.p2p.serialization_layer.Serializer;
import tin.p2p.socket_layer.RemoteNodesRepository;

import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

public class RemoteNode implements Comparable {
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

    public Void connect(String passwordHash) {
        SerializedObject serialisedObject = Serializer.getConnectionToNetObject(passwordHash);
        sender.connectToNode(this, serialisedObject);
        return null;

    }

    public void setReceiver(Receiver receiver) {
        this.receiver = receiver;
    }

    public void onConnectionLost() {
        RemoteNodesRepository.getInstance().unregister(this);
    }

    @Override
    public int compareTo(Object o) {
        return this == o ? 0 : 1;
    }

    public void setSenderSocket(Socket socket) {
        sender.setSocket(socket);
    }
}

