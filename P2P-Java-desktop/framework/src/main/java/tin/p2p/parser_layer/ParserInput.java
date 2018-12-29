package tin.p2p.parser_layer;

import tin.p2p.utils.Constants;

import java.io.IOException;

import static tin.p2p.utils.Constants.HASH_LENGTH;

public class ParserInput extends Thread implements Input{
    private tin.p2p.socket_layer.Input input;
    private tin.p2p.serialization_layer.Input higherLayerInput;
    @Override
    public void run() {
        listenToInputData();
    }

    public ParserInput(tin.p2p.socket_layer.Input lowerLayerInput,
                       tin.p2p.serialization_layer.Input higherLayerInput) {
        this.input = lowerLayerInput;
        this.higherLayerInput = higherLayerInput;
    }

    private void listenToInputData() {
        while (true) {
            byte opcode = 0;
            try {
                opcode = input.getNextByte();
                System.out.println(opcode);
            } catch (IOException e) {
                e.printStackTrace();
            }
            switch (opcode) {
                case Constants.OPCODE_WANT_TO_JOIN_INIT:
                    byte[] inputData = new byte[0];
                    try {
                        inputData = input.getNNextBytes(HASH_LENGTH);
                        System.out.println(inputData);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    higherLayerInput.deserialize(opcode, inputData);
                    break;
                default:
                    System.err.println("Unknown opcode");

            }
        }
    }

}

