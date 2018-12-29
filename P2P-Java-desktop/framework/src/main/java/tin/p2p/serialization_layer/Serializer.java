package tin.p2p.serialization_layer;

import tin.p2p.parser_layer.ObjectToSend;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static tin.p2p.utils.Constants.*;

public class Serializer implements Output{
    private tin.p2p.parser_layer.Output output;

    public Serializer(tin.p2p.parser_layer.Output output) {
        this.output = output;
    }


    @Override
    public void sendPassword(String password) {
        int dataArrayLenght = OPCODE_LENGTH + HASH_LENGTH;
        ByteBuffer byteBuffer = ByteBuffer.allocate(dataArrayLenght);
        byteBuffer.put(OPCODE_WANT_TO_JOIN_INIT);
        byteBuffer.put(password.getBytes(StandardCharsets.US_ASCII));
        output.addSendableObjectToQueue(new ObjectToSend(byteBuffer.array()));
    }

    @Override
    public void sendPasswordConfirmed(boolean b) {
        int dataArrayLenght = OPCODE_LENGTH + OPCODE_PASS_RESPONSE_LENGTH;
        ByteBuffer byteBuffer = ByteBuffer.allocate(dataArrayLenght);
        byteBuffer.put(OPCODE_PASS_RESPONSE);
        byteBuffer.put((byte) (b ? 1 : 0));
        output.addSendableObjectToQueue(new ObjectToSend(byteBuffer.array()));
    }

    @Override
    public void sendListOfNodes(ArrayList<Integer> ips) {
        int nRecords = ips.size();
        int dataArrayLenght = OPCODE_LENGTH + N_RECORDS_LENGTH + nRecords * RECORD_LENGTH;
        ByteBuffer byteBuffer = ByteBuffer.allocate(dataArrayLenght);
//        byteBuffer.order(ByteOrder.LITTLE_ENDIAN); // todo: przemyslec to
        byteBuffer.put(OPCODE_LIST_OD_KNOWN_NODES);
        byteBuffer.putInt(nRecords);
        for (int i = 0; i < nRecords; i++) {
            byteBuffer.putInt(ips.get(i));
        }
        output.addSendableObjectToQueue(new ObjectToSend(byteBuffer.array()));
    }

}

