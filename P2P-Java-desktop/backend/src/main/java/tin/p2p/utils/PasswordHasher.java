package tin.p2p.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordHasher {

    public static String hash(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA1");
        md.reset();
        byte[] buffer = input.getBytes(StandardCharsets.UTF_8);
        md.update(buffer);
        byte[] digest = md.digest();
        StringBuilder hexStr = new StringBuilder();
        for (byte aDigest : digest) {
            hexStr.append(Integer.toString((aDigest & 0xff) + 0x100, 16).substring(1));
        }
        System.out.println(hexStr.toString());
        return hexStr.toString();
    }
}
