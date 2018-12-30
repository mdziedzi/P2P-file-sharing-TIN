package tin.p2p.serialization_layer;

import java.util.ArrayList;

public interface Output {

    void sendPassword(String password);

    void sendPasswordConfirmed(boolean b);

    void sendListOfNodes(ArrayList<Integer> ips);

    void sendPasswordToRemoteNodeOfTheSameNet(String passwordHash);
}
