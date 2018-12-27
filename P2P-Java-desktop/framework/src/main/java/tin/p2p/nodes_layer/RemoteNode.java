package tin.p2p.nodes_layer;

import tin.p2p.serialization_layer.Output;

import java.util.ArrayList;

public class RemoteNode implements ReceiverInterface, SenderInterface{
    // todo kolejka do której wrzuca deserializator
    private Output output;

    public RemoteNode(Output output) {
        this.output = output;
    }


    @Override
    public void onNodeListReceived(ArrayList<String> nodes) {

    }


}

