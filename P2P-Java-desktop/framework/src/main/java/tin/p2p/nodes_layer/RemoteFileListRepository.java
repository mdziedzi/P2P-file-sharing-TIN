package tin.p2p.nodes_layer;


import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.logging.Logger;

public class RemoteFileListRepository {
    final static Logger log = Logger.getLogger(RemoteFileListRepository.class.getName());

    private static final RemoteFileListRepository instance = new RemoteFileListRepository();
    private ConcurrentSkipListSet<FileDTO> fileList = new ConcurrentSkipListSet<>();


    private RemoteFileListRepository() {
    }

    public static RemoteFileListRepository getInstance() {
        return instance;
    }

    public Optional<FileDTO> getFileInfoByHash(String fileHash) {
        return fileList.stream().filter(fileDTO -> fileDTO.getHash().equals(fileHash)).findFirst();
    }

    // todo uaktaulniać listę

//    public ArrayList<> getFileList() {
//        return new ArrayList<>(fileList);
//    } // todo


    public ConcurrentSkipListSet<FileDTO> getFileList() {
        return fileList;
    }

    private void addFile(ArrayList<String> fileParams, RemoteNode remoteNode) { //todo
        fileList.add(new FileDTO(fileParams, remoteNode));
    }

    public void addFileList(ArrayList<ArrayList<String>> listOfFiles, RemoteNode remoteNode) {
        listOfFiles.forEach(fileParams -> addFile(fileParams, remoteNode));
    }
}
