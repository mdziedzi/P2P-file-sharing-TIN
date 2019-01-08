package tin.p2p.socket_layer;

import java.net.Socket;

public class SocketRepository {
    private static SocketRepository ourInstance = new SocketRepository();

    private Socket socket;

    private SocketRepository() {
    }

    public static SocketRepository getInstance() {
        return ourInstance;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
}
