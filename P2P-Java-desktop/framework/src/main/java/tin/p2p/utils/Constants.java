package tin.p2p.utils;

public final class Constants {
    public static final int MAIN_APP_PORT = 8888;

    public static final byte OPCODE_WANT_TO_JOIN_INIT = 10;

    public static final int OPCODE_LENGTH = 1;
    public static final int HASH_LENGTH = 64;

    public static final byte OPCODE_PASS_RESPONSE_LENGTH = 1;
    public static final byte OPCODE_PASS_RESPONSE = 11;

    public static final byte N_RECORDS_LENGTH = 4;
    public static final byte RECORD_LENGTH = 4;
    public static final byte OPCODE_LIST_OD_KNOWN_NODES = 12;

    public static final byte OPCODE_WANT_TO_JOIN = 13;

    public static final byte OPCODE_FILE_LIST_REQUEST = 20;

    public static final byte FILE_HASH_LENGTH = 64;
    public static final byte FILE_NAME_LENGTH = 64;
    public static final byte FILE_LIST_FILE_SIZE_LENGTH = 8;
    public static final byte OPCODE_LIST_OF_FILES = 21;

    public static final byte FILE_OFFSET_LENGTH = 8;
    public static final byte OPCODE_FILE_FRAGMENT_REQUEST = 30;

    public static final byte OPCODE_DON_NOT_HAVE_FILE = 31;

    public static final byte OPCODE_FILE_FRAGMENT = 32;
    public static final int MAXIMUM_FILE_FRAGMENT_SIZE = 1024 * 512;

}
