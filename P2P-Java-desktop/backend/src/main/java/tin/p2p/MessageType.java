package tin.p2p;

public enum MessageType {

    FILES_LIST_REQUEST(12),
    NODES_IN_NETWORK_REQUEST(10),
    SENDING_FILES_LIST(30),
    SEARCH_FILE(13),
    SENDING_FILE(31);

    private int opcode;

    MessageType(int op) {
        this.opcode = op;
    }

    public int opcode() {
        return this.opcode;
    }
}

