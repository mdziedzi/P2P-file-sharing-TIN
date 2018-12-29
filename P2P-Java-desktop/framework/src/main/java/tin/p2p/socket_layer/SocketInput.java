package tin.p2p.socket_layer;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class SocketInput implements Input{
    private Socket socket;

    public SocketInput(Socket socket) {
        this.socket = socket;
    }

    @Override
    public byte[] getNNextBytes(int length) throws IOException {
        DataInputStream dis = new DataInputStream(socket.getInputStream());
        byte[] receivedData = new byte[length];
        dis.readFully(receivedData, 0, receivedData.length);
        System.out.println("getNNextBytes: " + receivedData);
        for (int i = 0; i < receivedData.length; i++) {
            System.out.print(receivedData[i] + " ");
            System.out.println();
        }
        return receivedData;
    }

    @Override
    public byte getNextByte() throws IOException {
        DataInputStream dis = new DataInputStream(socket.getInputStream());
        byte input = dis.readByte();
        //TODO obsługa little endian
        System.out.println("getNextByte: " + input);
        return input;
    }
}

