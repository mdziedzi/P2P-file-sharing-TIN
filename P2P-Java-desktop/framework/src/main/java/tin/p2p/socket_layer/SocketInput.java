package tin.p2p.socket_layer;

import org.apache.log4j.Logger;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;

public class SocketInput implements Input{
    final static Logger log = Logger.getLogger(SocketInput.class.getName());

    private Socket socket;

    public SocketInput(Socket socket) {
        this.socket = socket;
    }

    @Override
    public byte[] getNNextBytes(int length) throws IOException {
        DataInputStream dis = new DataInputStream(socket.getInputStream());
        byte[] receivedData = new byte[length];
        dis.readFully(receivedData, 0, receivedData.length);

        log.debug("getNNextBytes: " + Arrays.toString(receivedData));

        return receivedData;
    }

    @Override
    public byte getNextByte() throws IOException {
        DataInputStream dis = new DataInputStream(socket.getInputStream());
        byte input = dis.readByte();
        //TODO obsługa little endian

        log.debug("getNextByte: " + input);

        return input;
    }

    @Override
    public void closeConnection() {
        if(socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

