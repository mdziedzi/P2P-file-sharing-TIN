package tin.p2p.controller_layer;

import org.apache.log4j.Logger;
import tin.p2p.layers_factory.LayersFactory;
import tin.p2p.nodes_layer.*;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class FrameworkController {
    final static Logger log = Logger.getLogger(FrameworkController.class.getName());

    private static final FrameworkController instance = new FrameworkController();
    private ControllerGUIInterface.ListOfNodesViewer listOfNodesViewer;
    private ControllerGUIInterface.ListOfFilesCallback listOfFilesCallback;

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
        this.listOfFilesCallback = callback;
        //todo
        CompletableFuture.supplyAsync(() -> {
            RemoteNodesRepository.getRemoteNodes().forEach(SenderInterface::requestForFileList);
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

    public void initListeningForNewNodes() {
        if (newRemoteNodeListener == null) {
            newRemoteNodeListener = LayersFactory.initNewNodesListenerLayers();
            newRemoteNodeListener.start();
        }
    }

    public void updateViewOfFilesList(ArrayList<ArrayList<String>> listOfFiles) {
        if(listOfFiles != null)
            listOfFilesCallback.onListOfFilesReceived(listOfFiles);
    }
}

