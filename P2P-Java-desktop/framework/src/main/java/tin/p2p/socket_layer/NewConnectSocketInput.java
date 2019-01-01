package tin.p2p.socket_layer;

import org.apache.log4j.Logger;
import tin.p2p.layers_factory.LayersFactory;
import tin.p2p.utils.Constants;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class NewConnectSocketInput implements NewConnectInput {
    final static Logger log = Logger.getLogger(NewConnectSocketInput.class.getName());

    ServerSocket serverSocket = new ServerSocket(Constants.MAIN_APP_PORT);

    public NewConnectSocketInput() throws IOException {
    }

    public void acceptNewNode() throws IOException, SecurityException {
        Socket socket = serverSocket.accept();

        log.debug("Achieved new connection " + socket.getInetAddress().getHostAddress() + ":" + socket.getPort());

        new Thread(() -> LayersFactory.initLayersOfNewRemoteNode(socket, socket.getInetAddress().getHostAddress())).start();
    }

} 

