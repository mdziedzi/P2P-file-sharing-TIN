package tin.p2p.nodes_layer;

public class PasswordRepository {

    private static String password = "";

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        PasswordRepository.password = password;
    }
}
