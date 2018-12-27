package tin.p2p.socket_layer;

import java.io.IOException;

public interface Output {
    void send(byte[] data) throws IOException;
}
