package tin.p2p.resourceLayer;

import tin.p2p.model.Node;

import java.util.ArrayList;
import java.util.List;

public class ResourceManager {
    private static ResourceManager instance;
    private RemoteResourceManager remoteResourceManager;
    private List<Node> nodesInNetwork = new ArrayList<>();

    private ResourceManager() {}

    public static synchronized ResourceManager getInstance() {
        if (instance == null) {
            instance = new ResourceManager();
        }
        return instance;
    }

    public List<Node> getNodesInNetwork() {
        nodesInNetwork.add(new Node("test11", "11.111.111.111"));
        nodesInNetwork.add(new Node("test22", "0.0.0.0"));
        return nodesInNetwork;
    }

    public void connectToNetwork(String nodeName, String nodeIp) {
        remoteResourceManager = RemoteResourceManager.getInstance();
        remoteResourceManager.joinToNetwork(nodeName, nodeIp);
    }
}

