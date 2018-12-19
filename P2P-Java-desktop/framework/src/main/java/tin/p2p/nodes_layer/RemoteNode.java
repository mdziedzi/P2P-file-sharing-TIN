package tin.p2p.nodes_layer;

import tin.p2p.serialization_layer.Input;
import tin.p2p.serialization_layer.Output;

public class RemoteNode implements Runnable, ReceiverInterface, SenderInterface{
    // todo kolejka do której wrzuca deserializator
    private Output output;
    private Input input;
    @Override
    public void run() {
//        input.getNextCosTam
    }
}

