package tin.p2p.serialization;

import tin.p2p.MessageType;

import static tin.p2p.Constants.*;

import java.nio.ByteBuffer;

public class Serializer {

    public static byte[] serializeMessagePackage(MessageType messageType) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(MESSAGE_LENGTH);
        byteBuffer.put(0, (byte)messageType.opcode());
        return byteBuffer.array();
    }
} 

