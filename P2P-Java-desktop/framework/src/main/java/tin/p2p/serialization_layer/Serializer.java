package tin.p2p.serialization_layer;

import org.apache.log4j.Logger;
import tin.p2p.parser_layer.ObjectToSend;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static tin.p2p.utils.Constants.*;

public class Serializer implements Output{
    final static Logger log = Logger.getLogger(Serializer.class.getName());

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

    @Override
    public void sendPasswordToRemoteNodeOfTheSameNet(String passwordHash) {
        int dataArrayLenght = OPCODE_LENGTH + HASH_LENGTH;
        ByteBuffer byteBuffer = ByteBuffer.allocate(dataArrayLenght);
        byteBuffer.put(OPCODE_WANT_TO_JOIN);
        byteBuffer.put(passwordHash.getBytes(StandardCharsets.US_ASCII));
        output.addSendableObjectToQueue(new ObjectToSend(byteBuffer.array()));
    }

    @Override
    public void requestForFileList() {
        int dataArrayLenght = OPCODE_LENGTH;
        ByteBuffer byteBuffer = ByteBuffer.allocate(dataArrayLenght);
        byteBuffer.put(OPCODE_FILE_LIST_REQUEST);
        output.addSendableObjectToQueue(new ObjectToSend(byteBuffer.array()));
    }

    @Override
    public void sendListOfFiles(ArrayList<ArrayList<String>> fileList) {
        int nRecords = fileList.size();
        int dataArrayLenght = OPCODE_LENGTH + N_RECORDS_LENGTH + nRecords * FILE_LIST_ELEMENT_LENGTH * AMOUNT_OF_ELEMENTS_IN_ROW;
        ByteBuffer byteBuffer = ByteBuffer.allocate(dataArrayLenght);
//        byteBuffer.order(ByteOrder.LITTLE_ENDIAN); // todo: przemyslec to
        byteBuffer.put(OPCODE_LIST_OF_FILES);
        byteBuffer.putInt(nRecords);
        for (int i = 0; i < nRecords; i++) {
            System.out.println();
            fileList.get(i).forEach(s -> byteBuffer.put()); // todo: dopelniac bity zrobic tablice bajtow itd.
        }
        output.addSendableObjectToQueue(new ObjectToSend(byteBuffer.array()));
    }

}

