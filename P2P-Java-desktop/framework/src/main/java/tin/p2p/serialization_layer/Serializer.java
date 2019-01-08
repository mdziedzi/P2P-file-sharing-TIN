package tin.p2p.serialization_layer;

import tin.p2p.parser_layer.ObjectToSend;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.logging.Logger;

import static tin.p2p.utils.Constants.*;

public class Serializer implements Output{
    final static Logger log = Logger.getLogger(Serializer.class.getName());

    private tin.p2p.parser_layer.Output output;

    public Serializer(tin.p2p.parser_layer.Output output) {
        this.output = output;
    }


    @Override
    public void sendPassword(String password) {
        int dataArrayLenght = OPCODE_LENGTH + HASH_LENGTH;
        ByteBuffer byteBuffer = ByteBuffer.allocate(dataArrayLenght);
        byteBuffer.put(OPCODE_WANT_TO_JOIN_INIT);
        byteBuffer.put(password.getBytes(StandardCharsets.US_ASCII));
        output.addSendableObjectToQueue(new ObjectToSend(byteBuffer.array()));
    }

    @Override
    public void sendPasswordConfirmed(boolean b) {
        int dataArrayLenght = OPCODE_LENGTH + OPCODE_PASS_RESPONSE_LENGTH;
        ByteBuffer byteBuffer = ByteBuffer.allocate(dataArrayLenght);
        byteBuffer.put(OPCODE_PASS_RESPONSE);
        byteBuffer.put((byte) (b ? 1 : 0));
        output.addSendableObjectToQueue(new ObjectToSend(byteBuffer.array()));
    }

    @Override
    public void sendListOfNodes(ArrayList<Integer> ips) {
        int nRecords = ips.size();
        int dataArrayLenght = OPCODE_LENGTH + N_RECORDS_LENGTH + nRecords * RECORD_LENGTH;
        ByteBuffer byteBuffer = ByteBuffer.allocate(dataArrayLenght);
        byteBuffer.put(OPCODE_LIST_OD_KNOWN_NODES);
        byteBuffer.putInt(nRecords);
        for (Integer ip : ips) {
            byteBuffer.putInt(ip);
        }
        output.addSendableObjectToQueue(new ObjectToSend(byteBuffer.array()));
    }

    @Override
    public void sendPasswordToRemoteNodeOfTheSameNet(String passwordHash) {
        int dataArrayLenght = OPCODE_LENGTH + HASH_LENGTH;
        ByteBuffer byteBuffer = ByteBuffer.allocate(dataArrayLenght);
        byteBuffer.put(OPCODE_WANT_TO_JOIN);
        byteBuffer.put(passwordHash.getBytes(StandardCharsets.US_ASCII));
        output.addSendableObjectToQueue(new ObjectToSend(byteBuffer.array()));
    }

    @Override
    public void requestForFileList() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(OPCODE_LENGTH);
        byteBuffer.put(OPCODE_FILE_LIST_REQUEST);
        output.addSendableObjectToQueue(new ObjectToSend(byteBuffer.array()));
    }

    @Override
    public void sendListOfFiles(ArrayList<ArrayList<String>> fileList) {
        int nRecords = fileList.size();
        int dataArrayLenght = OPCODE_LENGTH + N_RECORDS_LENGTH + (nRecords * (FILE_NAME_LENGTH + FILE_HASH_LENGTH + FILE_LIST_FILE_SIZE_LENGTH));
        ByteBuffer byteBuffer = ByteBuffer.allocate(dataArrayLenght);
        byteBuffer.put(OPCODE_LIST_OF_FILES);
        byteBuffer.putInt(nRecords);

        log.info("Serialize list of files: nRecords:" + nRecords);
        fileList.forEach(strings -> log.info("File:: Name: " + strings.get(0) + "\tHash: " + strings.get(1) + "\tSize: " + strings.get(2)));

        for (ArrayList<String> aFileList : fileList) {
            ByteBuffer tmpBufferFileName = ByteBuffer.allocate(FILE_NAME_LENGTH);
            ByteBuffer tmpBufferHashName = ByteBuffer.allocate(FILE_HASH_LENGTH);
            ByteBuffer tmpBufferFileSize = ByteBuffer.allocate(FILE_LIST_FILE_SIZE_LENGTH);

            tmpBufferFileName.put(aFileList.get(0).getBytes(StandardCharsets.US_ASCII));
            tmpBufferHashName.put(aFileList.get(1).getBytes(StandardCharsets.US_ASCII));
            tmpBufferFileSize.putLong(Long.valueOf(aFileList.get(2)));

            byteBuffer.put(tmpBufferFileName.array());
            byteBuffer.put(tmpBufferHashName.array());
            byteBuffer.put(tmpBufferFileSize.array());
        }
        output.addSendableObjectToQueue(new ObjectToSend(byteBuffer.array()));
    }

    @Override
    public void requestForFileFragment(Long fileOffset, String fileHash) {
        int dataArrayLenght = OPCODE_LENGTH + FILE_OFFSET_LENGTH + FILE_HASH_LENGTH;
        ByteBuffer byteBuffer = ByteBuffer.allocate(dataArrayLenght);
        byteBuffer.put(OPCODE_FILE_FRAGMENT_REQUEST);
        byteBuffer.putLong(fileOffset);
        byteBuffer.put(fileHash.getBytes(StandardCharsets.US_ASCII));
        output.addSendableObjectToQueue(new ObjectToSend(byteBuffer.array()));
    }

    @Override
    public void sendFileFragment(String fileHash, Long fileOffset, ByteBuffer fileFragment) {
        ByteBuffer resultByteBuffer = ByteBuffer.allocate(
                OPCODE_LENGTH + N_RECORDS_LENGTH + FILE_OFFSET_LENGTH + FILE_HASH_LENGTH + fileFragment.array().length);

        resultByteBuffer.put(OPCODE_FILE_FRAGMENT);
        resultByteBuffer.putInt(fileFragment.array().length);
        resultByteBuffer.putLong(fileOffset);
        resultByteBuffer.put(fileHash.getBytes(StandardCharsets.US_ASCII));
        resultByteBuffer.put(fileFragment.array());

        output.addSendableObjectToQueue(new ObjectToSend(resultByteBuffer.array()));
    }

    @Override
    public void sendNotAuthorizedMsg() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(OPCODE_LENGTH);
        byteBuffer.put(OPCODE_NOT_AUTHORIZED);
        output.addSendableObjectToQueue(new ObjectToSend(byteBuffer.array()));
    }

    @Override
    public void terminate() {
        output.terminate();
    }

    @Override
    public void requestForSalt() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(OPCODE_LENGTH);
        byteBuffer.put(OPCODE_REQUEST_FOR_SALT);
        output.addSendableObjectToQueue(new ObjectToSend(byteBuffer.array()));
    }

    @Override
    public void requestForSaltForConnectionInTheSameNet() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(OPCODE_LENGTH);
        byteBuffer.put(OPCODE_REQUEST_FOR_SALT_IN_THE_SAME_NET);
        output.addSendableObjectToQueue(new ObjectToSend(byteBuffer.array()));
    }

    @Override
    public void sendSalt(int salt) {
        int dataArrayLenght = OPCODE_LENGTH + SALT_LENGTH;
        ByteBuffer byteBuffer = ByteBuffer.allocate(dataArrayLenght);
        byteBuffer.put(OPCODE_SALT_FOR_HASH);
        byteBuffer.putInt(salt);
        output.addSendableObjectToQueue(new ObjectToSend(byteBuffer.array()));
    }

    @Override
    public void sendSaltForConnectionInTheSameNet(int salt) {
        int dataArrayLenght = OPCODE_LENGTH + SALT_LENGTH;
        ByteBuffer byteBuffer = ByteBuffer.allocate(dataArrayLenght);
        byteBuffer.put(OPCODE_SALT_FOR_HASH_IN_THE_SAME_NET);
        byteBuffer.putInt(salt);
        output.addSendableObjectToQueue(new ObjectToSend(byteBuffer.array()));
    }

    @Override
    public void sendNoSuchFileMessage(String fileHash, Long fileOffset) {
        int dataArrayLenght = OPCODE_LENGTH + FILE_OFFSET_LENGTH + HASH_LENGTH;
        ByteBuffer byteBuffer = ByteBuffer.allocate(dataArrayLenght);
        byteBuffer.put(OPCODE_DON_NOT_HAVE_FILE);
        byteBuffer.putLong(fileOffset);
        byteBuffer.put(fileHash.getBytes(StandardCharsets.US_ASCII));
        output.addSendableObjectToQueue(new ObjectToSend(byteBuffer.array()));
    }


}

