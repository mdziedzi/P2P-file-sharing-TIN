package tin.p2p.nodes_layer;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
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

    // todo uaktaulniać listę

//    public ArrayList<> getFileList() {
//        return new ArrayList<>(fileList);
//    } // todo


    public Collection<FileDTO> getFileList() {
        return files.values();
    }

    private void addFile(ArrayList<String> fileParams, RemoteNode remoteNode) { //todo
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
}
