package tin.p2p.controller_layer;

import tin.p2p.nodes_controller_layer.RemoteNodesController;
import tin.p2p.utils.PasswordHasher;

import java.security.NoSuchAlgorithmException;
import java.util.concurrent.CompletableFuture;

public class Controller {
    private static Controller instance;
    private static ControllerGUIInterface controllerGUI;
//    private static RemoteNodesRepository remoteNodesRepository;

    private Controller() {}

    public static synchronized Controller getInstance(ControllerGUIInterface guiInterface) {
        if (instance == null) {
            instance = new Controller();
            controllerGUI = guiInterface;
//            remoteNodesRepository = RemoteNodesRepository.getInstance();
        }
        return instance;
    }

    public static synchronized Controller getInstance() {
//        if (instance != null && remoteNodesRepository != null) {
        if (instance != null) {
            return instance;
        }
        return null;
    }


    /**
     * Creates new net based on user IP.
     */
    public void createNewNet(String password, ControllerGUIInterface.CreateNewNetCallback callback) {
        String passwordHash = null;
        try {
            passwordHash = PasswordHasher.hash(password);
        }
        catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
            callback.onCreateNewNetFailure();
        }


        String finalPasswordHash = passwordHash;
        CompletableFuture.supplyAsync(() -> RemoteNodesController.getInstance().createNewNet(finalPasswordHash))
                .thenAccept(t -> callback.onCreateNewNetSuccess())
                .exceptionally((t) -> {
                    System.err.println(t);
                    return null;
                })
                .thenAccept(ex -> callback.onCreateNewNetFailure());

    }


    /**
     * Connect to net with specific IP.
     * @param ip IP of node we want to connect with
     * @param callback Object on which the callback will be performed.
     */
    public void connectToNetByIP(String ip, String password, ControllerGUIInterface.ConnectToNetByIPCallback callback) {
        String passwordHash = null;
        try {
            passwordHash = PasswordHasher.hash(password);
        }
        catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
            callback.onConnectToNetByIPFailure();
        }

        String finalPasswordHash = passwordHash;
        CompletableFuture.supplyAsync(() -> RemoteNodesController.getInstance().connectToNetByIp(ip, finalPasswordHash))
                .thenAccept(t -> callback.onConnectToNetByIPSucces())
                .exceptionally((t) -> {
                    System.err.println(t);
                    return null;
                })
                .thenAccept(ex -> callback.onConnectToNetByIPFailure());
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


