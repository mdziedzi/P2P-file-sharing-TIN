package tin.p2p.parser_layer;

import tin.p2p.utils.Constants;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;

import static tin.p2p.utils.Constants.*;

public class ParserInput extends Thread implements Input{
    private tin.p2p.socket_layer.Input input;
    private tin.p2p.serialization_layer.Input deserializatorInput;
    @Override
    public void run() {
        listenToInputData();
    }

    public ParserInput(tin.p2p.socket_layer.Input lowerLayerInput,
                       tin.p2p.serialization_layer.Input deserializationInput) {
        this.input = lowerLayerInput;
        this.deserializatorInput = deserializationInput;
    }

    private void listenToInputData() {
        while (true) {
            byte opcode = 0;
            try {
                opcode = input.getNextByte();
                System.out.println("ParserInput opcode: " + opcode);
            } catch (IOException e) {
                e.printStackTrace();
                input.closeConnection();
                return;
            }
            switch (opcode) {
                case Constants.OPCODE_WANT_TO_JOIN_INIT:
                    getRestData(opcode, HASH_LENGTH);
                    break;
                case Constants.OPCODE_PASS_RESPONSE:
                    getRestData(opcode, OPCODE_PASS_RESPONSE_LENGTH);
                    break;
                case Constants.OPCODE_LIST_OD_KNOWN_NODES:
                    readListOfKnownNodes(opcode, N_RECORDS_LENGTH);
                    break;
                case Constants.OPCODE_WANT_TO_JOIN:
                    getRestData(opcode, HASH_LENGTH);
                    break;
                case Constants.OPCODE_FILE_LIST_REQUEST:
                    getRestData(opcode, 0);
                    break;
                default:
                    //todo: co z reszta danych (wypisuje sie 2 razy)
                    System.err.println("Unknown opcode");

            }
        }
    }

    private void readListOfKnownNodes(byte opcode, int nRecordsLength) {
        int nRecords = 0;
        try {
            nRecords = new BigInteger(input.getNNextBytes(nRecordsLength)).intValue();
            System.out.println(nRecords);
        } catch (IOException e) {
            e.printStackTrace();
            input.closeConnection();
            return;
        }

        ByteBuffer inputData = ByteBuffer.allocate(nRecordsLength + nRecords * RECORD_LENGTH);
        inputData.putInt(nRecords);
        try {
            inputData.put(input.getNNextBytes(nRecords * RECORD_LENGTH));
        } catch (IOException e) {
            e.printStackTrace();
            input.closeConnection();
            return;
        }


        deserializatorInput.deserialize(opcode, inputData.array());
    }

    private void getRestData(byte opcode, int dataLength) {
        byte[] inputData = new byte[0];
        try {
            inputData = input.getNNextBytes(dataLength);
            System.out.println(inputData);
        } catch (IOException e) {
            e.printStackTrace();
            input.closeConnection();
            return;
        }
        deserializatorInput.deserialize(opcode, inputData);
    }

}

