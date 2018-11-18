package tin.p2p.serialization;

import java.nio.ByteBuffer;

public class Deserializer {

    //TODO jaką klasę ma zwracać, obsłużyć wszystkie przypadki komunikatów
    public static int deserialize(ByteBuffer byteBuffer) {
        char opcode = (char)byteBuffer.get();
        return opcode;
    }
} 

