package tin.p2p.serialization_layer;

import tin.p2p.parser_layer.ObjectToSend;

public class Serializer implements Output{
    private tin.p2p.parser_layer.Output output;

    public Serializer(tin.p2p.parser_layer.Output output) {
        this.output = output;
    }

    @Override
    public void serialize() {
        //todo
        output.addSendableObjectToQueue(new ObjectToSend());
    }
}

