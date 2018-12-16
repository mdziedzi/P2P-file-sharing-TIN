package tin.p2p.serialization_layer;

import java.nio.ByteBuffer;

public class SerializedObject {
    private static final int OPCODE_BYTES_LENGTH = 4;
    private byte[] data;

    public SerializedObject(int opcode, byte[] restData) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(OPCODE_BYTES_LENGTH
                + (restData != null ? restData.length : 0));
        byteBuffer.putInt(opcode).put(restData);
        data = byteBuffer.array();
    }

    public int getDataLength() {
        if(data != null)
            return data.length;
        return 0;
    }

    public byte[] getData() {
        return data;
    }
}

