package tin.p2p.nodes_layer;

import java.util.concurrent.ConcurrentSkipListSet;

public class RemoteNodesRepository {
    private static ConcurrentSkipListSet<RemoteNode> remoteNodes = new ConcurrentSkipListSet<>();

    public static void registerNode(RemoteNode remoteNode) {
        remoteNodes.add(remoteNode);
    }
}

