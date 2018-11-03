package tin.p2p.resourceLayer;

import tin.p2p.model.Node;

import java.util.ArrayList;
import java.util.List;

public class ResourceManager {
    public static List<Node> getNodesInNetwork() {
        List<Node> nodes = new ArrayList<>();
        nodes.add(new Node("test11", "11.111.111.111"));
        nodes.add(new Node("test22", "0.0.0.0"));
        return nodes;
    }
}

