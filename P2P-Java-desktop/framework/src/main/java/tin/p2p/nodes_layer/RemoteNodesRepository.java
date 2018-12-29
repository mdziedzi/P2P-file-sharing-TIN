package tin.p2p.nodes_layer;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentSkipListSet;

public class RemoteNodesRepository {
    private static ConcurrentSkipListSet<RemoteNode> remoteNodes = new ConcurrentSkipListSet<>();

    public static void registerNode(RemoteNode remoteNode) {
        remoteNodes.add(remoteNode);
    }

    public static ArrayList<Integer> getItegerIpList() {
        ArrayList<Integer> ips = new ArrayList<>();
        remoteNodes.forEach(remoteNode -> ips.add(remoteNode.getIpAsInteger()));
        return ips;
    }
}

