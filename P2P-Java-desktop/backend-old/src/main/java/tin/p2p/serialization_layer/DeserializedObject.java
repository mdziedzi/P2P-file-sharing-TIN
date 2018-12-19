package tin.p2p.serialization_layer;

import tin.p2p.utils.Constants;

public class DeserializedObject {
    private final String passwordHash;
    private int opcode;
    private short port;
    public DeserializedObject(int opcode, short port, String passwordHash) {
        this.opcode = opcode;
        this.port = port;
        this.passwordHash = passwordHash;
    }

    public boolean isConnectionRequest() {
        return this.opcode == Constants.OPCODE_CONNECT_TO_NET;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public short getPort() {
        return port;
    }
}

