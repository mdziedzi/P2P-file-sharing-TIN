package tin.p2p.controller;

import tin.p2p.controllerGUI.ControllerBackendInterface;
import tin.p2p.model.Node;
import tin.p2p.resourceLayer.ResourceManager;

import java.util.ArrayList;
import java.util.List;

public class Controller implements ControllerBackendInterface {
    private static Controller instance;
    private static ControllerGUIInterface controllerGUI;
    private ResourceManager resourceManager;
    private List<Node> nodesInNetwork = new ArrayList<>();

    private Controller() {}

    public static synchronized Controller getInstance(ControllerGUIInterface guiInterface) {
        if (instance == null) {
            instance = new Controller();
            controllerGUI = guiInterface;
        }
        return instance;
    }

    @Override
    public List<Node> getNodesInNetwork() {
        nodesInNetwork.add(new Node("test11", "11.111.111.111"));
        nodesInNetwork.add(new Node("test22", "0.0.0.0"));
        return nodesInNetwork;
    }

    @Override
    public void connectToNetwork(String nodeName, String nodeIp) {
        resourceManager = ResourceManager.getInstance();
    }

    @Override
    public void listOfPreviouslyTrustedIPNumbersRequest() {
        //todo
        controllerGUI.showPreviousTrustedIPNumbers();
    }
}


