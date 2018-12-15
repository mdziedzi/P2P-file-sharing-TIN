package tin.p2p;

import tin.p2p.socketLayer.SocketManager;

import java.io.IOException;
import java.net.Socket;

public class Receiver {
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

