package tin.p2p.parser_layer;

import tin.p2p.utils.Constants;

import java.io.IOException;

import static tin.p2p.utils.Constants.HASH_LENGTH;
import static tin.p2p.utils.Constants.OPCODE_PASS_RESPONSE_LENGTH;

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
            }
            switch (opcode) {
                case Constants.OPCODE_WANT_TO_JOIN_INIT:
                    getRestData(opcode, HASH_LENGTH);
                    break;
                case Constants.OPCODE_PASS_RESPONSE:
                    getRestData(opcode, OPCODE_PASS_RESPONSE_LENGTH);
                    break;
                default:
                    //todo: co z reszta danych (wypisuje sie 2 razy)
                    System.err.println("Unknown opcode");

            }
        }
    }

    private void getRestData(byte opcode, int dataLength) {
        byte[] inputData = new byte[0];
        try {
            inputData = input.getNNextBytes(dataLength);
            System.out.println(inputData);
        } catch (IOException e) {
            e.printStackTrace();
        }
        deserializatorInput.deserialize(opcode, inputData);
    }

}

