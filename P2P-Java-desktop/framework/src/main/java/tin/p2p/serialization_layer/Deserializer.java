package tin.p2p.serialization_layer;


import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import tin.p2p.nodes_layer.ReceiverInterface;
import tin.p2p.nodes_layer.RemoteNode;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.logging.Logger;

import static tin.p2p.utils.Constants.*;

public class Deserializer implements Input{
    final static Logger log = Logger.getLogger(Deserializer.class.getName());

    private ReceiverInterface receiver;

    public Deserializer() {
    }

    @Override
    public void deserialize(byte opcode, byte[] inputData) {
        ByteBuffer data;
        switch (opcode) {
            case OPCODE_WANT_TO_JOIN_INIT:
                data = ByteBuffer.wrap(inputData);
                receiver.onNewParticipantPasswordReceived(decode(data));
                break;
            case OPCODE_PASS_RESPONSE:
                data = ByteBuffer.wrap(inputData);
                if (data.get() != 0) {
                    receiver.onPasswordCorrect();
                } else {
                    receiver.onPasswordReject();
                }
                break;
            case OPCODE_LIST_OD_KNOWN_NODES:
                data = ByteBuffer.wrap(inputData);
                receiver.onNodeListReceived(unpackListOfKnownNodes(data));
                break;
            case OPCODE_WANT_TO_JOIN:
                data = ByteBuffer.wrap(inputData);
                receiver.onNewPasswordReceived(decode(data));
                break;
            case OPCODE_FILE_LIST_REQUEST:
                receiver.onFileListRequest();
                break;
            case OPCODE_LIST_OF_FILES:
                data = ByteBuffer.wrap(inputData);
                receiver.onFileListReceived(unpackListOfFiles(data));
                break;
            case OPCODE_FILE_FRAGMENT_REQUEST:
                data = ByteBuffer.wrap(inputData);
                Pair<String, Long> fileHashAndOffset = decodeRequestedFileFragmentInfo(data);
                receiver.onFileFragmentRequest(fileHashAndOffset.getKey(), fileHashAndOffset.getValue());
                break;
            case OPCODE_FILE_FRAGMENT:
                data = ByteBuffer.wrap(inputData);
                Triple<String, Long, ByteBuffer> decodedPackage = decodeReceivedFileFragment(data);

                receiver.onFileFragmentReceived(decodedPackage.getLeft(), decodedPackage.getMiddle(), decodedPackage.getRight());
                break;
            case OPCODE_NOT_AUTHORIZED:
                receiver.onNotAuthorizedMsg();
                break;
            default:
                log.warning("Deserializer: bad opcode!");
        }
    }

    private Triple<String, Long, ByteBuffer> decodeReceivedFileFragment(ByteBuffer data) {
        int dataLength = data.getInt();

        Long fileOffset = data.getLong();

        byte[] fileHashTmp = new byte[FILE_HASH_LENGTH];

        data.get(fileHashTmp);
        String fileHash = StandardCharsets.US_ASCII.decode(ByteBuffer.wrap(fileHashTmp)).toString();

        ByteBuffer fileFragmentData = ByteBuffer.allocate(dataLength);

        data.get(fileFragmentData.array());

        return Triple.of(fileHash, fileOffset, fileFragmentData);
    }

    private ArrayList<ArrayList<String>> unpackListOfFiles(ByteBuffer data) {
        int nRecords = data.getInt();

        ArrayList<ArrayList<String>> stringFileList = new ArrayList<>();

        for (int i = 0; i < nRecords; i++) {
            ArrayList<String> fileAttributes = new ArrayList<>();
            byte[] fileNameTmp = new byte[FILE_NAME_LENGTH];
            byte[] fileHashTmp = new byte[FILE_HASH_LENGTH];

            data.get(fileNameTmp);
            fileAttributes.add(StandardCharsets.US_ASCII.decode(ByteBuffer.wrap(fileNameTmp)).toString().trim());

            data.get(fileHashTmp);
            fileAttributes.add(StandardCharsets.US_ASCII.decode(ByteBuffer.wrap(fileHashTmp)).toString().trim());

            fileAttributes.add(String.valueOf(data.getLong()));

            stringFileList.add(fileAttributes);
        }

        return stringFileList;
    }

    private String decode(ByteBuffer data) {
        return StandardCharsets.US_ASCII.decode(data).toString();
    }

    private ArrayList<String> unpackListOfKnownNodes(ByteBuffer data) {
        int nRecords = data.getInt();

        ArrayList<Integer> ipsInBytes = new ArrayList<>();
        for (int i = 0; i < nRecords; i++) {
            ipsInBytes.add(data.getInt());
        }

        ArrayList<String> ipsInString = new ArrayList<>();
        for (Integer i : ipsInBytes) {
            ipsInString.add(translateToString(i));
            log.info("Translate IP:" + translateToString(i));
        }

        return ipsInString;
    }

    // pdk
    private String translateToString(Integer i) {
        return ((i >> 24) & 0xFF) + "." +
                ((i >> 16) & 0xFF) + "." +
                ((i >> 8) & 0xFF) + "." +
                (i & 0xFF);
    }

    @Override
    public void setRemoteNodeReceiver(RemoteNode remoteNode) {
        this.receiver = remoteNode;
    }

    private Pair<String, Long> decodeRequestedFileFragmentInfo(ByteBuffer data) {
        Long fileOffset = data.getLong();

        byte[] fileHashTmp = new byte[FILE_HASH_LENGTH];

        data.get(fileHashTmp);
        String fileHash = StandardCharsets.US_ASCII.decode(ByteBuffer.wrap(fileHashTmp)).toString();

        return Pair.of(fileHash, fileOffset);
    }
}

