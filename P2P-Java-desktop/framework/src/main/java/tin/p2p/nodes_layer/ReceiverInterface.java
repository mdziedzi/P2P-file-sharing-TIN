package tin.p2p.nodes_layer;

import java.util.ArrayList;

public interface ReceiverInterface {
    void onNodeListReceived(ArrayList<String> nodes);

    void onPasswordReceived(String password);

    void onPasswordCorrect();

    void onPasswordReject();
}

