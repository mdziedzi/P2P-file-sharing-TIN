package tin.p2p.serialization_layer;

import java.nio.ByteBuffer;

import static tin.p2p.utils.Constants.MESSAGE_LENGTH;

public class Serializer {

    public static byte[] serializeMessagePackage(MessageType messageType) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(MESSAGE_LENGTH);
        byteBuffer.put(0, (byte)messageType.opcode());
        return byteBuffer.array();
    }

    public static SerializedObject getConnectionToNetObject() {
        //todo
        return new SerializedObject();
    }
} 

