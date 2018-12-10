package tin.p2p.controller;

import tin.p2p.Constants;
import tin.p2p.ThreadManager;
import tin.p2p.model.Node;
import tin.p2p.resourceLayer.ResourceManager;

import java.util.ArrayList;
import java.util.List;

public class Controller {
    private static Controller instance;
    private static ControllerGUIInterface controllerGUI;
    private static ThreadManager threadManager;

    private Controller() {}

    public static synchronized Controller getInstance(ControllerGUIInterface guiInterface) {
        if (instance == null) {
            instance = new Controller();
            controllerGUI = guiInterface;
            threadManager = new ThreadManager();
        }
        return instance;
    }

    public static synchronized Controller getInstance() {
        if (instance != null && threadManager != null) {
            return instance;
        }
        return null;
    }


    /**
     * Creates new net based on user IP.
     */
    public void createNewNet(ControllerGUIInterface.CreateNewNetCallback callback) {
        // todo
        threadManager.createNewNet();
//        if (Constants.OPERATION_SUCCESSFUL == ) {
            callback.onCreateNewNetSuccess();
//        } else {
            callback.onCreateNewNetFailure();
//        }


    }


    /**
     * Connect to net with specific IP.
     * @param ip IP of node we want to connect with
     * @param callback Object on which the callback will be performed.
     */
    public void connectToNetByIP(String ip, ControllerGUIInterface.ConnectToNetByIPCallback callback) {
        // todo

//        if (Constants.OPERATION_SUCCESSFUL == threadManager.connectToNode(ip)) {
//            callback.onConnectToNetByIPSucces();
//
//        }

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


