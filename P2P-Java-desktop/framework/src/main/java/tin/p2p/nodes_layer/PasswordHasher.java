package tin.p2p.nodes_layer;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordHasher {
    public static String hash(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.reset();
        byte[] buffer = input.getBytes(StandardCharsets.US_ASCII);
        md.update(buffer);
        byte[] digest = md.digest();
        StringBuilder hexStr = new StringBuilder();
        for (byte aDigest : digest) {
            hexStr.append(Integer.toString((aDigest & 0xff) + 0x100, 16).substring(1));
        }
        return hexStr.toString();
    }
}
