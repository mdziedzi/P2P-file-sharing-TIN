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

    public void acceptNewNode() throws IOException {
        Socket socket = serverSocket.accept();

        // wyslij na jakim porcie teraz bedziesz obslugiwal
        LayersFactory.initLayersOfNewRemoteNode(socket); // tutaj nowy soket

        // wyslij port na którym bedziesz dalej sluchal
    }

} 

