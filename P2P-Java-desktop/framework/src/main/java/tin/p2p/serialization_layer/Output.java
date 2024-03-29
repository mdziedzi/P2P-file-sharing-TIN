package tin.p2p.serialization_layer;

import java.nio.ByteBuffer;
import java.util.ArrayList;

public interface Output {

    void sendPassword(String password);

    void sendPasswordConfirmed(boolean b);

    void sendListOfNodes(ArrayList<Integer> ips);

    void sendPasswordToRemoteNodeOfTheSameNet(String passwordHash);

    void requestForFileList();

    void sendListOfFiles(ArrayList<ArrayList<String>> fileList);

    void requestForFileFragment(Long fileOffset, String fileHash);

    void sendFileFragment(String fileHash, Long fileOffset, ByteBuffer fileFragment);

    void sendNotAuthorizedMsg();

    void terminate();

    void requestForSalt();

    void requestForSaltForConnectionInTheSameNet();

    void sendSalt(int salt);

    void sendSaltForConnectionInTheSameNet(int salt);

    void sendNoSuchFileMessage(String fileHash, Long fileOffset);
}
