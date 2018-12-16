package tin.p2p.serialization_layer;

public class Deserializer {

    //TODO jaką klasę ma zwracać, obsłużyć wszystkie przypadki komunikatów
    public static SerializedObject deserialize(byte[] byteBuffer) {
        return new SerializedObject(0, new byte[1]);
    }
} 

