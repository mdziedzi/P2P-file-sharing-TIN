package tin.p2p.parser_layer;

public class ObjectToSend implements SendableObject {

    private byte[] data;

    public ObjectToSend(byte[] data) {
        this.data = data;
    }

    @Override
    public byte[] getDataToSend() {
        return data;
    }
}

