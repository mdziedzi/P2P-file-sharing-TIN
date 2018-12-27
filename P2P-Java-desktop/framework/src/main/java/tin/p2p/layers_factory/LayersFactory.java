package tin.p2p.layers_factory;

import tin.p2p.nodes_layer.RemoteNode;
import tin.p2p.parser_layer.ParserInput;
import tin.p2p.parser_layer.ParserOutput;
import tin.p2p.serialization_layer.Deserializer;
import tin.p2p.serialization_layer.Serializer;
import tin.p2p.socket_layer.SocketInput;
import tin.p2p.socket_layer.SocketOutput;

public class LayersFactory {
    private static LayersFactory instance = new LayersFactory();

    public static LayersFactory getInstance() {
        return instance;
    }
    private LayersFactory() {}

    public static RemoteNode initLayersOfNewRemoteNode(String ip) {
        // todo po lewej interfejsy
        SocketInput socketInput = new SocketInput();
        SocketOutput socketOutput = new SocketOutput();
        ParserOutput parserOutput = new ParserOutput(socketOutput);
        Serializer serializer = new Serializer(parserOutput);
        Deserializer deserializer = new Deserializer();
        ParserInput parserInput = new ParserInput(socketInput, deserializer);
        RemoteNode remoteNode = new RemoteNode(serializer);
        deserializer.setRemoteNodeReceiver(remoteNode);

        return remoteNode;
    }

    public static Void initNewNodesListenerLayers() {

        return null;
    }
}

