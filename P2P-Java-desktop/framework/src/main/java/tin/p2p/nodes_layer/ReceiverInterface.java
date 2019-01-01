package tin.p2p.nodes_layer;

import java.util.ArrayList;

public interface ReceiverInterface {
    void onNodeListReceived(ArrayList<String> nodes);

    void onNewParticipantPasswordReceived(String password);

    void onPasswordCorrect();

    void onPasswordReject();

    void onNewPasswordReceived(String passwordHash);

    void onFileListRequest();

    void onFileListReceived(ArrayList<ArrayList<String>> listOfFiles);
}

