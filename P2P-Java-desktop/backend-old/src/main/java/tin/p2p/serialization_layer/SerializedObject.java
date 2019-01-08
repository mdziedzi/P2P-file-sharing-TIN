package tin.p2p.serialization_layer;

import java.nio.ByteBuffer;

public class SerializedObject {
    private static final int OPCODE_BYTES_LENGTH = 4;
    private static final int PORT_BYTES_LENGTH = 2;
    private static final int INT_BYTES_LENGHT = 4;

    private byte[] data;

    public SerializedObject(int opcode, byte[] restData) {
        int dataArrayLenght = INT_BYTES_LENGHT + OPCODE_BYTES_LENGTH
                + (restData != null ? restData.length : 0);
        ByteBuffer byteBuffer = ByteBuffer.allocate(dataArrayLenght);
        byteBuffer.putInt(dataArrayLenght - OPCODE_BYTES_LENGTH); //na pierwszej pozycji ile będzie danych
        byteBuffer.putInt(opcode).put(restData);
        data = byteBuffer.array();
    }

    public SerializedObject(int opcode, short port, byte[] restData) {
        int dataArrayLenght = INT_BYTES_LENGHT + OPCODE_BYTES_LENGTH + PORT_BYTES_LENGTH
                + (restData != null ? restData.length : 0);
        ByteBuffer byteBuffer = ByteBuffer.allocate(dataArrayLenght);
        byteBuffer.putInt(dataArrayLenght - INT_BYTES_LENGHT);
        byteBuffer.putInt(opcode).putShort(port).put(restData);
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

