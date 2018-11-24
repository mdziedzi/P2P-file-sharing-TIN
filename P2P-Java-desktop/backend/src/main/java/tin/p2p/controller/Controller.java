package tin.p2p.controller;

import tin.p2p.model.Node;
import tin.p2p.resourceLayer.ResourceManager;

import java.util.ArrayList;
import java.util.List;

public class Controller {
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

    public List<Node> getNodesInNetwork() {
        nodesInNetwork.add(new Node("test11", "11.111.111.111"));
        nodesInNetwork.add(new Node("test22", "0.0.0.0"));
        return nodesInNetwork;
    }

    public void connectToNetwork(String nodeName, String nodeIp) {
        resourceManager = ResourceManager.getInstance();
    }

    /**
     * Creates new net based on user IP.
     */
    public void createNewNet(ControllerGUIInterface.CreateNewNetCallback callback) {
        // todo
        callback.onCreateNewNetSuccess();
        callback.onCreateNewNetFailure();

    }


    /**
     * Connect to net with specific IP.
     * @param ip IP of node we want to connect with
     * @param callback Object on which the callback will be performed.
     */
    public void connectToNetByIP(String ip, ControllerGUIInterface.ConnectToNetByIPCallback callback) {
        // todo
        callback.onConnectToNetByIPSucces();
        callback.onConnectToNetByIPReject();
        callback.onConnectToNetByIPFailure();
    }

    /**
     * Disconnect from the net. Closes all network things and prepares for rerun application.
     *
     * @param callback Object on which the callback will be performed.
     */
    public void disconnectFromNet(ControllerGUIInterface.DisconnectCallback callback) {
        // todo
        callback.onDisconnectSuccess();

        // todo czy przewidujemy bład?
        callback.onDisconnectFailure();
    }

}


