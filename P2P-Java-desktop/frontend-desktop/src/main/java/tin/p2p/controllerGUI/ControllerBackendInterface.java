package tin.p2p.controllerGUI;

import tin.p2p.model.Node;

import java.util.List;

public interface ControllerBackendInterface {

    /**
     * Wysyła żądanie o liste poprzednio zaufanych numerów IP
     */
    void listOfPreviouslyTrustedIPNumbersRequest();

    void connectToNetwork(String nodeName, String nodeIp);

    List<Node> getNodesInNetwork();
}
