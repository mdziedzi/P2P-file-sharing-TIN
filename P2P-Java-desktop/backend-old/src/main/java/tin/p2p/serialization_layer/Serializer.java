package tin.p2p.serialization_layer;

import tin.p2p.utils.Constants;

import java.nio.charset.StandardCharsets;


public class Serializer {

    public static SerializedObject getConnectionToNetObject(String passwordHash) {
        int opcode = Constants.OPCODE_CONNECT_TO_NET;
        short connectionPort = 8889;// todo, generowany autamatycznie wolny
        byte[] restData = passwordHash.getBytes(StandardCharsets.US_ASCII);
        return new SerializedObject(opcode, connectionPort, restData);
    }
} 

