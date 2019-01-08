package tin.p2p.socket_layer;

import java.io.IOException;

public interface Input {
    byte[] getNNextBytes(int length) throws IOException;
    byte getNextByte() throws IOException;

    void closeConnection();
}
