package tin.p2p.layers_factory;

import tin.p2p.exceptions.AppPortTakenException;
import tin.p2p.exceptions.CreatingNetException;
import tin.p2p.exceptions.UnsuccessfulConnectionException;
import tin.p2p.nodes_layer.NewRemoteNodeListener;
import tin.p2p.nodes_layer.RemoteNode;
import tin.p2p.nodes_layer.RemoteNodesRepository;
import tin.p2p.nodes_layer.SenderInterface;
import tin.p2p.parser_layer.ParserInput;
import tin.p2p.parser_layer.ParserOutput;
import tin.p2p.serialization_layer.Deserializer;
import tin.p2p.serialization_layer.Serializer;
import tin.p2p.socket_layer.*;
import tin.p2p.utils.Constants;

import java.io.IOException;
import java.net.BindException;
import java.net.Socket;
import java.util.logging.Logger;

public class LayersFactory {
    final static Logger log = Logger.getLogger(LayersFactory.class.getName());

    private static LayersFactory instance = new LayersFactory();

    public static LayersFactory getInstance() {
        return instance;
    }
    private LayersFactory() {}

    public static SenderInterface initLayersOfNewRemoteNode(String ip) throws UnsuccessfulConnectionException {

        Output socketOutput = null;
        Input socketInput = null;
        try {
            socketOutput = new SocketOutput(ip, Constants.MAIN_APP_PORT);
            socketInput = new SocketInput(socketOutput.getSocket());
        } catch (IOException e) {
            if (socketOutput != null)
                socketOutput.closeConnection();

            if(socketInput != null)
                socketInput.closeConnection();

            throw new UnsuccessfulConnectionException(e);
        }

        tin.p2p.parser_layer.Output parserOutput = new ParserOutput(socketOutput);

        tin.p2p.serialization_layer.Output serializer = new Serializer(parserOutput);
        tin.p2p.serialization_layer.Input deserializer = new Deserializer();
        tin.p2p.parser_layer.Input parserInput = new ParserInput(socketInput, deserializer);

        RemoteNode remoteNode = new RemoteNode(serializer, ip, false);
        deserializer.setRemoteNodeReceiver(remoteNode);

        RemoteNodesRepository.registerNode(remoteNode);

        ((ParserInput) parserInput).start();
        ((ParserOutput) parserOutput).start();

        return remoteNode;
    }

    public static NewRemoteNodeListener initNewNodesListenerLayers() throws AppPortTakenException, CreatingNetException {

        NewConnectInput newConnectInput = null;
        try {
            newConnectInput = new NewConnectSocketInput();
        } catch (BindException e) {
            throw new AppPortTakenException(e);
        } catch (IOException e) {
            throw new CreatingNetException(e);
        }
        NewRemoteNodeListener newRemoteNodeListener = new NewRemoteNodeListener(newConnectInput);
        return newRemoteNodeListener;
    }

    public static void initLayersOfNewRemoteNode(Socket socket, String ip) {
        Input socketInput = new SocketInput(socket);
        Output socketOutput = new SocketOutput(socket);

        tin.p2p.parser_layer.Output parserOutput = new ParserOutput(socketOutput);

        tin.p2p.serialization_layer.Output serializer = new Serializer(parserOutput);
        tin.p2p.serialization_layer.Input deserializer = new Deserializer();
        tin.p2p.parser_layer.Input parserInput = new ParserInput(socketInput, deserializer);

        RemoteNode remoteNode = new RemoteNode(serializer, ip, true);
        deserializer.setRemoteNodeReceiver(remoteNode);

        RemoteNodesRepository.registerNode(remoteNode);

        ((ParserInput) parserInput).start();
        ((ParserOutput) parserOutput).start();
    }
}

