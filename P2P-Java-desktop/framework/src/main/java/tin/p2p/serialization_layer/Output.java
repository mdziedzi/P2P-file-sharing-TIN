package tin.p2p.serialization_layer;

public interface Output {

    void sendPassword(String password);

    void sendPasswordConfirmed(boolean b);
}
