package tin.p2p.controller;

import tin.p2p.RemoteNodesRepository;
import tin.p2p.model.RemoteNode;

import java.net.UnknownHostException;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class Controller {
    private static Controller instance;
    private static ControllerGUIInterface controllerGUI;
    private static RemoteNodesRepository remoteNodesRepository;

    private Controller() {}

    public static synchronized Controller getInstance(ControllerGUIInterface guiInterface) {
        if (instance == null) {
            instance = new Controller();
            controllerGUI = guiInterface;
            remoteNodesRepository = RemoteNodesRepository.getInstance();
        }
        return instance;
    }

    public static synchronized Controller getInstance() {
        if (instance != null && remoteNodesRepository != null) {
            return instance;
        }
        return null;
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
        RemoteNode remoteNode = new RemoteNode();
//        try {
//            remoteNode = remoteNodesRepository.getNewRemoteNode(ip);
//        } catch (UnknownHostException e) {
//            callback.onConnectToNetByIPFailure();
//            e.printStackTrace();
//        }

        CompletableFuture.supplyAsync(remoteNode::connect)
                .thenAccept(t -> callback.onConnectToNetByIPSucces())
                .exceptionally((t) -> {
                    System.err.println(t);
                    return null;
                })
                .thenAccept(ex -> callback.onConnectToNetByIPFailure());

//                        callback.onConnectToNetByIPFailure())
//                        callback.onConnectToNetByIPReject()
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


