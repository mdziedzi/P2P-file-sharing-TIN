package tin.p2p.serialization_layer;

import java.nio.ByteBuffer;

public class Deserializer {

    //TODO jaką klasę ma zwracać, obsłużyć wszystkie przypadki komunikatów
    public static int deserialize(ByteBuffer byteBuffer) {
        return (char) byteBuffer.get();
    }
} 

