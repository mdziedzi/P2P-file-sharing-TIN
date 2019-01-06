package tin.p2p.nodes_layer;

import java.nio.ByteBuffer;
import java.util.ArrayList;

public interface ReceiverInterface {
    void onNodeListReceived(ArrayList<String> nodes);

    void onNewParticipantPasswordReceived(String password);

    void onPasswordCorrect();

    void onPasswordReject();

    void onNewPasswordReceived(String passwordHash);

    void onFileListRequest();

    void onFileListReceived(ArrayList<ArrayList<String>> listOfFiles);

    void onFileFragmentRequest(String fileHash, Long fileOffset);

    void onFileFragmentReceived(String fileHash, Long fileOffset, ByteBuffer fileFragmentData);

    void onNotAuthorizedMsg();
}

