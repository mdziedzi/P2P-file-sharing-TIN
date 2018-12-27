package tin.p2p.parser_layer;

import tin.p2p.utils.Constants;

import java.io.IOException;

public class ParserInput implements Runnable, Input{
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
            } catch (IOException e) {
                e.printStackTrace();
            }
            switch (opcode) {
                case Constants.OPCODE_CONNECT_TO_NET:
                    byte[] inputData = new byte[0];
                    try {
                        inputData = input.getNNextBytes(64);
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

