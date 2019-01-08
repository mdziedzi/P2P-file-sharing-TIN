package tin.p2p.socket_layer;

import tin.p2p.layers_factory.LayersFactory;
import tin.p2p.utils.Constants;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.logging.Logger;

public class NewConnectSocketInput implements NewConnectInput {
    final static Logger log = Logger.getLogger(NewConnectSocketInput.class.getName());

    ServerSocket serverSocket = new ServerSocket(Constants.MAIN_APP_PORT);

    public NewConnectSocketInput() throws IOException {
    }

    public void acceptNewNode() throws SocketTimeoutException, IOException, SecurityException {
        Socket socket = serverSocket.accept();

        log.info("Achieved new connection " + socket.getInetAddress().getHostAddress() + ":" + socket.getPort());

        new Thread(() -> LayersFactory.initLayersOfNewRemoteNode(socket, socket.getInetAddress().getHostAddress())).start();
    }

    @Override
    public void terminate() throws IOException {
        serverSocket.close();
    }

} 

