package tin.p2p.socket_layer;

import java.io.IOException;
import java.net.Socket;

public interface Output {
    void send(byte[] data) throws IOException;

    Socket getSocket();

    void closeConnection();
}
