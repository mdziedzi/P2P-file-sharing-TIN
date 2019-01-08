package tin.p2p.serialization_layer;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class Deserializer {

    //TODO jaką klasę ma zwracać, obsłużyć wszystkie przypadki komunikatów
    public static DeserializedObject deserialize(byte[] bytesArray) {
        int lenght = bytesArray.length;
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytesArray, 4, lenght - 4);
        int opcode = 0;
        short port = 0;

        if (lenght >= 4) {
            opcode = byteBuffer.getInt();
            System.out.println();
            System.out.println("DeserializedObject Opcode: " + opcode);
        }
        if (lenght >= 6) {
            port = byteBuffer.getShort();
            System.out.println("DeserializedObject Port: " + port);
        }

        String passwordHash = StandardCharsets.US_ASCII.decode(byteBuffer).toString();
        System.out.println("DeserializedObject PasswordHash: " + passwordHash);

        return new DeserializedObject(opcode, port, passwordHash);
    }
} 

