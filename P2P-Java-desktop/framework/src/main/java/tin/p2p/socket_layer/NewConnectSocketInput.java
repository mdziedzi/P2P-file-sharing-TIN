package tin.p2p.socket_layer;

import tin.p2p.layers_factory.LayersFactory;
import tin.p2p.utils.Constants;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class NewConnectSocketInput implements NewConnectInput {
    ServerSocket serverSocket = new ServerSocket(Constants.MAIN_APP_PORT);

    public NewConnectSocketInput() throws IOException {
    }

    public void acceptNewNode() throws IOException, SecurityException {
        System.out.println("slucham dalej");
        Socket socket = serverSocket.accept();
        System.out.println("Achieved new connection " + socket.getInetAddress().getHostAddress());

        new Thread(() -> LayersFactory.initLayersOfNewRemoteNode(socket, socket.getInetAddress().getHostAddress())).start();
    }

} 

