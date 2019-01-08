package tin.p2p.serialization_layer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import tin.p2p.utils.Constants;
import tin.p2p.utils.PasswordHasher;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SerializerUnitTest {
    private String passwordHash;
    private SerializedObject serializedObject;

    @BeforeEach
    void initPasswordHash() throws NoSuchAlgorithmException {
        passwordHash = PasswordHasher.hash("test");
        serializedObject = Serializer.getConnectionToNetObject(passwordHash);
    }

    @Test
    public void connectionToNetObjectShouldHaveProperDataLenght() {
        byte [] data = serializedObject.getData();

        Assertions.assertEquals(74, data.length);
    }

    @Test
    public void connectionToNetObjectShouldHaveProperOpcode() {
        byte [] data = serializedObject.getData();
        byte [] opcodeDataFragment = Arrays.copyOfRange(data, 4, 8);
        int opcode = ByteBuffer.wrap(opcodeDataFragment).getInt();

        Assertions.assertEquals(Constants.OPCODE_CONNECT_TO_NET, opcode);
    }

    @Test
    public void connectionToNetObjectShouldHaveCorrectPassHash() {
        byte [] data = serializedObject.getData();
        byte [] passwordHashDataFragment = Arrays.copyOfRange(data, 10, 74);
        boolean isEquals = Arrays.equals(passwordHash.getBytes(StandardCharsets.US_ASCII), passwordHashDataFragment);

        Assertions.assertTrue(isEquals);
    }

} 

