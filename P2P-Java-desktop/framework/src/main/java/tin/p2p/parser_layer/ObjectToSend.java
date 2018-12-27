package tin.p2p.parser_layer;

public class ObjectToSend implements SendableObject {
    @Override
    public byte[] getDataToSend() {
        return new byte[0];
    }
}

