package tin.p2p.nodes_layer;

import tin.p2p.controller_layer.FrameworkController;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentSkipListSet;

public class RemoteNodesRepository {
    private static ConcurrentSkipListSet<RemoteNode> remoteNodes = new ConcurrentSkipListSet<>();

    public static void registerNode(RemoteNode remoteNode) {
        remoteNodes.add(remoteNode);
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

    public static ConcurrentSkipListSet<RemoteNode> getRemoteNodes() {
        return remoteNodes;
    }
}

