package tin.p2p.layers_factory;

import tin.p2p.nodes_layer.NewRemoteNodeListener;
import tin.p2p.nodes_layer.RemoteNode;
import tin.p2p.nodes_layer.RemoteNodesRepository;
import tin.p2p.parser_layer.ParserInput;
import tin.p2p.parser_layer.ParserOutput;
import tin.p2p.serialization_layer.Deserializer;
import tin.p2p.serialization_layer.Serializer;
import tin.p2p.socket_layer.*;

import java.io.IOException;
import java.net.Socket;

public class LayersFactory {
    private static LayersFactory instance = new LayersFactory();

    public static LayersFactory getInstance() {
        return instance;
    }
    private LayersFactory() {}

    public static RemoteNode initLayersOfNewRemoteNode(String ip) {

        Input socketInput = new SocketInput();
        Output socketOutput = new SocketOutput();

        tin.p2p.parser_layer.Output parserOutput = new ParserOutput(socketOutput);
        tin.p2p.serialization_layer.Output serializer = new Serializer(parserOutput);
        tin.p2p.serialization_layer.Input deserializer = new Deserializer();

        tin.p2p.parser_layer.Input parserInput = new ParserInput(socketInput, deserializer);
        RemoteNode remoteNode = new RemoteNode(serializer);
        deserializer.setRemoteNodeReceiver(remoteNode);

        return remoteNode;
    }

    public static NewRemoteNodeListener initNewNodesListenerLayers() {

        NewConnectInput newConnectInput = null;
        try {
            newConnectInput = new NewConnectSocketInput();
        } catch (IOException e) {
            e.printStackTrace();
        }
        NewRemoteNodeListener newRemoteNodeListener = new NewRemoteNodeListener(newConnectInput);
        return newRemoteNodeListener;
    }

    public static void initLayersOfNewRemoteNode(Socket socket) {
        Input socketInput = new SocketInput(socket);
        Output socketOutput = new SocketOutput(socket);

        tin.p2p.parser_layer.Output parserOutput = new ParserOutput(socketOutput);

        tin.p2p.serialization_layer.Output serializer = new Serializer(parserOutput);
        tin.p2p.serialization_layer.Input deserializer = new Deserializer();
        tin.p2p.parser_layer.Input parserInput = new ParserInput(socketInput, deserializer);

        RemoteNode remoteNode = new RemoteNode(serializer);
        deserializer.setRemoteNodeReceiver(remoteNode);

        RemoteNodesRepository.registerNode(remoteNode);

        ((ParserInput) parserInput).run();
        ((ParserOutput) parserOutput).run();
    }
}

