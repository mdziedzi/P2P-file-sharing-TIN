package tin.p2p.nodes_controller_layer;

import tin.p2p.exceptions.BadIpFormatException;
import tin.p2p.serialization_layer.SerializedObject;
import tin.p2p.socket_layer.NewConnectsReceiver;
import tin.p2p.socket_layer.RemoteNodesRepository;
import tin.p2p.socket_layer.connection.RemoteNode;

import java.net.UnknownHostException;

public class RemoteNodesController {

    private static RemoteNodesController instance;

    private RemoteNodesController() {
    }

    public static synchronized RemoteNodesController getInstance() {
        if (instance == null) {
            instance = new RemoteNodesController();
        }
        return instance;
    }

    public Void connectToNetByIp(String ip) {
        RemoteNode remoteNode;
        try {
            remoteNode = RemoteNodesRepository.getInstance().getNewRemoteNode(ip);
        } catch (UnknownHostException e) {
            System.err.println(e.getMessage());
            throw new BadIpFormatException();
        }
        remoteNode.connect();
        return null;
    }

    public Void createNewNet() {
        NewConnectsReceiver newConnectsReceiver = new NewConnectsReceiver(this);
        newConnectsReceiver.startListening();


        return null;
    }

    public void onNewDataReceived(SerializedObject receivedData) {
        //todo
    }
}
