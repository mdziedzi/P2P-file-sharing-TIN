package tin.p2p.nodes_layer;


import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class RemoteFileListRepository {
    final static Logger log = Logger.getLogger(RemoteFileListRepository.class.getName());

    private static final RemoteFileListRepository instance = new RemoteFileListRepository();
    private ConcurrentHashMap<String, FileDTO> files = new ConcurrentHashMap<>();


    private RemoteFileListRepository() {
    }

    public static RemoteFileListRepository getInstance() {
        return instance;
    }

    public FileDTO getFileInfoByHash(String fileHash) {
        return files.get(fileHash);
    }

    public Collection<FileDTO> getFileList() {
        return files.values();
    }

    public ArrayList<ArrayList<String>> getFileListRaw() {
        ArrayList<ArrayList<String>> stringFileList = new ArrayList<>();

        files.values().forEach(fileDTO -> {
            ArrayList<String> tmp = new ArrayList<>();
            tmp.add(fileDTO.getName());
            tmp.add(fileDTO.getHash());
            tmp.add(fileDTO.getSize().toString());
            StringBuilder sb = new StringBuilder();
            fileDTO.getRemoteNodes().forEach(remoteNode -> {
                sb.append(remoteNode.getIp());
                sb.append(" ");
            });
            tmp.add(sb.toString());
            stringFileList.add(tmp);
        });

        return stringFileList;
    }

    private void addFile(ArrayList<String> fileParams, RemoteNode remoteNode) {
        FileDTO file = files.get(fileParams.get(1));
        if (file == null) {
            files.put(fileParams.get(1), new FileDTO(fileParams, remoteNode));
        } else {
            file.addRemoteNodeOwner(remoteNode);
        }
    }

    public void addFileList(ArrayList<ArrayList<String>> listOfFiles, RemoteNode remoteNode) {
        listOfFiles.forEach(fileParams -> addFile(fileParams, remoteNode));
    }

    public void onNewFilesInNetRequest() {
        files.clear();
    }
}
