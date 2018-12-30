package tin.p2p.controller_layer;

import tin.p2p.layers_factory.LayersFactory;
import tin.p2p.nodes_layer.*;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class FrameworkController {
    private static final FrameworkController instance = new FrameworkController();
    private ControllerGUIInterface.ListOfNodesViewer listOfNodesViewer;

    private FrameworkController() {}
    private NewRemoteNodeListener newRemoteNodeListener;

    public static FrameworkController getInstance() {
        return instance;
    }
    /**
     * Creates new net based on user IP.
     */
    public void createNewNet(String password, ControllerGUIInterface.CreateNewNetCallback callback) {
        this.newRemoteNodeListener = LayersFactory.initNewNodesListenerLayers();
        try {
            PasswordRepository.setPassword(PasswordHasher.hash(password));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        newRemoteNodeListener.start();

    }

    public void registerListOfNodesViewer(ControllerGUIInterface.ListOfNodesViewer listOfNodesViewer) {
        this.listOfNodesViewer = listOfNodesViewer;
    }


    /**
     * Connect to net with specific IP.
     * @param ip IP of node we want to connect with
     * @param callback Object on which the callback will be performed.
     */
    public void connectToNetByIP(String ip, String password, ControllerGUIInterface.ConnectToNetByIPCallback callback) {

        try {
            password = PasswordHasher.hash(password);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        PasswordRepository.setPassword(password);
        String finalPassword = password;
        CompletableFuture.supplyAsync(() -> LayersFactory.initLayersOfNewRemoteNode(ip))
                .thenAccept(t -> t.connectToNetByIp(finalPassword)).thenAccept(t -> callback.onConnectToNetByIPSucces())
                .exceptionally((t) -> {
                    callback.onConnectToNetByIPFailure();
                    return null;
                });
    }

    public void getListOfFilesInNet(ControllerGUIInterface.ListOfFilesCallback callback) {
        //todo
        CompletableFuture.supplyAsync(() -> {
            RemoteNodesRepository.getRemoteNodes().forEach(RemoteNode::requestForFileList);
            return null;
        });
    }

    public ArrayList<String> getListOfNodes() {
        return RemoteNodesRepository.getStringIpList();
    }

    public void updateViewOfRemoteNodes(ArrayList<String> remoteNodesIps) {
        if (listOfNodesViewer != null) {
            listOfNodesViewer.onListOfNodesUpdated(remoteNodesIps);
        }
    }

}

