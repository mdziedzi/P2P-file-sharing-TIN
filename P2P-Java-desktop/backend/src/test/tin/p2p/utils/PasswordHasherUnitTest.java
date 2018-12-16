package tin.p2p.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

public class PasswordHasherUnitTest {
    @Test
    public void resultShouldHaveLenght64() throws NoSuchAlgorithmException {
        String passwordHash = PasswordHasher.hash("test");

        int length = passwordHash.length();

        Assertions.assertEquals(64, length);
    }

    @Test
    public void resultConvertedToByteArrayShouldHaveLength64() throws NoSuchAlgorithmException {
        String passwordHash = PasswordHasher.hash("test");

        int length = passwordHash.getBytes(StandardCharsets.US_ASCII).length;

        Assertions.assertEquals(64, length);
    }


}


