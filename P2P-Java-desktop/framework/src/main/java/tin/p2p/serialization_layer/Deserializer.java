package tin.p2p.serialization_layer;


import tin.p2p.nodes_layer.ReceiverInterface;
import tin.p2p.nodes_layer.RemoteNode;

import java.util.ArrayList;

public class Deserializer implements Input{
    private ReceiverInterface receiver;

    public Deserializer() {
    }

    @Override
    public void deserialize(byte opcode, byte[] inputData) {
        switch (opcode) {
            case 20: // list wezłów do których trzeba się połączyć
                // todo konwersja byte[] na tablicę string'ów
                ArrayList<String> nodes = new ArrayList<>();
                receiver.onNodeListReceived(nodes);
        }
    }

    @Override
    public void setRemoteNodeReceiver(RemoteNode remoteNode) {
        this.receiver = remoteNode;
    }
}

