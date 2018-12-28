package tin.p2p.parser_layer;

public interface Output {
    void addSendableObjectToQueue(SendableObject sendableObject);

    void listenForPortInfo();
}
