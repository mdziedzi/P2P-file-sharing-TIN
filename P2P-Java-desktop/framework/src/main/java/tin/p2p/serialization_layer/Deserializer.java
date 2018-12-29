package tin.p2p.serialization_layer;


import tin.p2p.nodes_layer.ReceiverInterface;
import tin.p2p.nodes_layer.RemoteNode;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static tin.p2p.utils.Constants.OPCODE_PASS_RESPONSE;
import static tin.p2p.utils.Constants.OPCODE_WANT_TO_JOIN_INIT;

public class Deserializer implements Input{
    private ReceiverInterface receiver;

    public Deserializer() {
    }

    @Override
    public void deserialize(byte opcode, byte[] inputData) {
        ByteBuffer data;
        switch (opcode) {
            case OPCODE_WANT_TO_JOIN_INIT:
                data = ByteBuffer.wrap(inputData);
                String password = StandardCharsets.US_ASCII.decode(data).toString();
                receiver.onPasswordReceived(password);
                break;
            case OPCODE_PASS_RESPONSE:
                data = ByteBuffer.wrap(inputData);
                if (data.get() != 0) {
                    receiver.onPasswordCorrect();
                } else {
                    receiver.onPasswordReject();
                }
                break;
            default:
                System.out.println("Deserializer: bad opcode!");
        }
    }

    @Override
    public void setRemoteNodeReceiver(RemoteNode remoteNode) {
        this.receiver = remoteNode;
    }
}

