package tin.p2p.socket_layer.connection;

import tin.p2p.socket_layer.SocketManager;

import java.io.IOException;
import java.net.Socket;

class Receiver {
    private Socket socket;


    private Receiver(Socket socket) {
        this.socket = socket;
    }

    static Receiver create(Socket socket) throws IOException {
        Receiver receiver = new Receiver(socket);
        receiver.startListening(socket);
        return receiver;
    }

    private void startListening(Socket socket) throws IOException {
        SocketManager.listen(socket);

    }

}

