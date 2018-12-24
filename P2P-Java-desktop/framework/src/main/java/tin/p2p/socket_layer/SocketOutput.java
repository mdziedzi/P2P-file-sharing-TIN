package tin.p2p.socket_layer;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class SocketOutput implements Output{
    private Socket socket;

    public SocketOutput(Socket socket) {
        this.socket = socket;
    }

    public void send(byte[] data) throws IOException {
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        dos.write(data);
    }
}

