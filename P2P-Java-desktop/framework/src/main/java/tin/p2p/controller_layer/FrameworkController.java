package tin.p2p.controller_layer;

import tin.p2p.layers_factory.LayersFactory;
import tin.p2p.nodes_layer.NewRemoteNodeListener;

import java.util.concurrent.CompletableFuture;

public class FrameworkController {
    private static final FrameworkController instance = new FrameworkController();
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
        newRemoteNodeListener.run();

    }


    /**
     * Connect to net with specific IP.
     * @param ip IP of node we want to connect with
     * @param callback Object on which the callback will be performed.
     */
    public void connectToNetByIP(String ip, String password, ControllerGUIInterface.ConnectToNetByIPCallback callback) {
//        CompletableFuture.supplyAsync(() -> LayersFactory.initLayersOfNewRemoteNode(ip))
//                .thenAccept(t -> callback.onConnectToNetByIPSucces())
//                .exceptionally((t) -> {
//                    callback.onConnectToNetByIPFailure();
//                    return null;
//                });
    }

}

