package tin.p2p.nodes_layer;

import tin.p2p.controller_layer.FrameworkController;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.logging.Logger;

public class RemoteNodesRepository {
    final static Logger log = Logger.getLogger(RemoteNodesRepository.class.getName());

    private static ConcurrentSkipListSet<RemoteNode> remoteNodes = new ConcurrentSkipListSet<>();

    public static void registerNode(RemoteNode remoteNode) {
        remoteNodes.add(remoteNode);
        notifyGui();
    }

    public static void unregisterNode(RemoteNode remoteNode) {
        remoteNodes.remove(remoteNode);
        notifyGui();
    }

    private static void notifyGui() {
        FrameworkController.getInstance().updateViewOfRemoteNodes(getStringIpList());
    }

    public static ArrayList<Integer> getItegerIpList() {
        ArrayList<Integer> ips = new ArrayList<>();
        remoteNodes.forEach(remoteNode -> ips.add(remoteNode.getIpAsInteger()));
        return ips;
    }

    public static ArrayList<String> getStringIpList() {
        ArrayList<String> remoteNodesIps = new ArrayList<>();
        remoteNodes.forEach(rn -> remoteNodesIps.add(rn.getIp()));
        return remoteNodesIps;
    }

    public static ConcurrentSkipListSet<? extends SenderInterface> getRemoteNodes() {
        return remoteNodes;
    }

    public static RemoteNode find(String fileOwner) {
        return remoteNodes.stream().filter(remoteNode -> remoteNode.getIp().equals(fileOwner)).findFirst().get();
    }


    public static void endOfProgram() {
        remoteNodes.forEach(RemoteNode::terminate);
    }
}

