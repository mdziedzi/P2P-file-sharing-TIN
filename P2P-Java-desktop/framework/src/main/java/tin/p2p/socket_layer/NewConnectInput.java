package tin.p2p.socket_layer;

import java.io.IOException;

public interface NewConnectInput {
    void acceptNewNode() throws IOException;

    void terminate() throws IOException;
}
