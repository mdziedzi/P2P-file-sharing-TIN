package tin.p2p.nodes_layer;


import java.util.logging.Logger;

public class PasswordRepository {
    final static Logger log = Logger.getLogger(PasswordRepository.class.getName());

    private static String password = "";

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        PasswordRepository.password = password;
    }
}
