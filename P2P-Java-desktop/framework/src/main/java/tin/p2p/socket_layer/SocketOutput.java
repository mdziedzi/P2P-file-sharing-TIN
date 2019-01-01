package tin.p2p.socket_layer;

import org.apache.log4j.Logger;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Arrays;

public class SocketOutput implements Output{
    final static Logger log = Logger.getLogger(SocketOutput.class.getName());

    private Socket socket;

    public SocketOutput(Socket socket) {
        this.socket = socket;
    }

    public SocketOutput() {}

    public SocketOutput(String ip, int port) throws IOException {
        log.debug("Create socket by connecting to " + ip + ":" + port);

        connect(new InetSocketAddress(ip, port));
    }

    public void send(byte[] data) throws IOException {
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        dos.write(data);

        log.debug("Socket Output - Send: " + Arrays.toString(data));
    }

    @Override
    public Socket getSocket() {
        return this.socket;
    }

    @Override
    public void closeConnection() {
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void connect(InetSocketAddress address) throws IOException {
        Socket socket = new Socket();
//        SocketAddress socketAddress = new InetSocketAddress(address, Constants.MAIN_APP_PORT);
        socket.connect(address);
        this.socket = socket;

//        SocketRepository.getInstance().setSocket(socket);

    }
}

