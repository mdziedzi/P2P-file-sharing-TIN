package tin.p2p.nodes_controller_layer;

import tin.p2p.exceptions.BadIpFormatException;
import tin.p2p.socket_layer.RemoteNodesRepository;
import tin.p2p.socket_layer.connection.RemoteNode;

import java.net.UnknownHostException;

public class RemoteNodesController {


    public static Void connectToNetByIp(String ip) {
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
}
