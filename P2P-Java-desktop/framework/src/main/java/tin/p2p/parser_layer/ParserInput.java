package tin.p2p.parser_layer;

import tin.p2p.utils.Constants;

import java.io.EOFException;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.logging.Logger;

import static tin.p2p.utils.Constants.*;

public class ParserInput extends Thread implements Input{
    final static Logger log = Logger.getLogger(ParserInput.class.getName());
    private volatile boolean running = true;

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
        while (running) {
            byte opcode = 0;
            try {
                opcode = input.getNextByte();
                log.info("ParserInput opcode: " + opcode);
            } catch (EOFException ex) {
                terminate();
                ex.printStackTrace();
                deserializatorInput.terminate();
                return;
            } catch (IOException e) {
                e.printStackTrace();
                input.closeConnection();
                deserializatorInput.terminate();
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
                case Constants.OPCODE_LIST_OF_FILES:
                    readListOfFiles(opcode, N_RECORDS_LENGTH);
                    break;
                case Constants.OPCODE_FILE_FRAGMENT_REQUEST:
                    getRestData(opcode, FILE_OFFSET_LENGTH + FILE_HASH_LENGTH);
                    break;
                case Constants.OPCODE_FILE_FRAGMENT:
                    readFileFragment(opcode);
                    break;
                case Constants.OPCODE_NOT_AUTHORIZED:
                    getRestData(opcode, 0);
                    break;
                default:
                    //todo: co z reszta danych (wypisuje sie 2 razy)
                    log.warning("Unknown opcode");

            }
        }
    }

    private void readFileFragment(byte opcode) {
        int dataLength = 0;

        try {
            dataLength = new BigInteger(input.getNNextBytes(N_RECORDS_LENGTH)).intValue();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ByteBuffer inputData = ByteBuffer.allocate(N_RECORDS_LENGTH + FILE_OFFSET_LENGTH + FILE_HASH_LENGTH + dataLength);
        inputData.putInt(dataLength);

        try {
            inputData.put(input.getNNextBytes(FILE_OFFSET_LENGTH + FILE_HASH_LENGTH));
            inputData.put(input.getNNextBytes(dataLength));
        } catch (IOException e) {
            e.printStackTrace();
        }

        deserializatorInput.deserialize(opcode, inputData.array());
    }

    private void readListOfFiles(byte opcode, byte nRecordsLength) {
        int nRecords = 0;
        try {
            nRecords = new BigInteger(input.getNNextBytes(nRecordsLength)).intValue();

            log.info("readListOfFiles - files number: " + nRecords);
        } catch (IOException e) {
            e.printStackTrace();
            input.closeConnection();
            deserializatorInput.terminate();
            return;
        }

        ByteBuffer inputData = ByteBuffer.allocate(nRecordsLength + (nRecords * (FILE_NAME_LENGTH + FILE_HASH_LENGTH + FILE_LIST_FILE_SIZE_LENGTH)));
        inputData.putInt(nRecords);
        try {
            inputData.put(input.getNNextBytes(nRecords * (FILE_NAME_LENGTH + FILE_HASH_LENGTH + FILE_LIST_FILE_SIZE_LENGTH)));
        } catch (IOException e) {
            e.printStackTrace();
            input.closeConnection();
            deserializatorInput.terminate();
            return;
        }


        deserializatorInput.deserialize(opcode, inputData.array());
    }

    private void readListOfKnownNodes(byte opcode, int nRecordsLength) {
        int nRecords = 0;
        try {
            nRecords = new BigInteger(input.getNNextBytes(nRecordsLength)).intValue();
            log.info("readListOfKnownNodes - nodes number: " + nRecords);
        } catch (IOException e) {
            e.printStackTrace();
            input.closeConnection();
            deserializatorInput.terminate();
            return;
        }

        ByteBuffer inputData = ByteBuffer.allocate(nRecordsLength + nRecords * RECORD_LENGTH);
        inputData.putInt(nRecords);
        try {
            inputData.put(input.getNNextBytes(nRecords * RECORD_LENGTH));
        } catch (IOException e) {
            e.printStackTrace();
            input.closeConnection();
            deserializatorInput.terminate();
            return;
        }


        deserializatorInput.deserialize(opcode, inputData.array());
    }

    private void getRestData(byte opcode, int dataLength) {
        byte[] inputData = new byte[0];
        try {
            inputData = input.getNNextBytes(dataLength);
            log.info("getRestData" + Arrays.toString(inputData));
        } catch (IOException e) {
            e.printStackTrace();
            input.closeConnection();
            deserializatorInput.terminate();
            return;
        }
        deserializatorInput.deserialize(opcode, inputData);
    }

    @Override
    public void terminate() {
        running = false;
        input.closeConnection();
    }
}

