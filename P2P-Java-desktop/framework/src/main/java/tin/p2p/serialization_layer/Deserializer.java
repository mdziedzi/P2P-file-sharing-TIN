package tin.p2p.serialization_layer;


import tin.p2p.nodes_layer.ReceiverInterface;
import tin.p2p.nodes_layer.RemoteNode;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static tin.p2p.utils.Constants.*;

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
                receiver.onNewParticipantPasswordReceived(decode(data));
                break;
            case OPCODE_PASS_RESPONSE:
                data = ByteBuffer.wrap(inputData);
                if (data.get() != 0) {
                    receiver.onPasswordCorrect();
                } else {
                    receiver.onPasswordReject();
                }
                break;
            case OPCODE_LIST_OD_KNOWN_NODES:
                data = ByteBuffer.wrap(inputData);
                receiver.onNodeListReceived(unpackLostOfKnownNodes(data));
                break;
            case OPCODE_WANT_TO_JOIN:
                data = ByteBuffer.wrap(inputData);
                receiver.onNewPasswordReceived(decode(data));
            default:
                System.out.println("Deserializer: bad opcode!");
        }
    }

    private String decode(ByteBuffer data) {
        return StandardCharsets.US_ASCII.decode(data).toString();
    }

    private ArrayList<String> unpackLostOfKnownNodes(ByteBuffer data) {
        int nRecords = data.getInt();

        ArrayList<Integer> ipsInBytes = new ArrayList<>();
        for (int i = 0; i < nRecords; i++) {
            ipsInBytes.add(data.getInt());
        }

        ArrayList<String> ipsInString = new ArrayList<>();
        for (Integer i : ipsInBytes) {
            ipsInString.add(translateToString(i));
        }

        return ipsInString;
    }

    // pdk
    private String translateToString(Integer i) {
        return ((i >> 24) & 0xFF) + "." +
                ((i >> 16) & 0xFF) + "." +
                ((i >> 8) & 0xFF) + "." +
                (i & 0xFF);
    }

    @Override
    public void setRemoteNodeReceiver(RemoteNode remoteNode) {
        this.receiver = remoteNode;
    }
}

