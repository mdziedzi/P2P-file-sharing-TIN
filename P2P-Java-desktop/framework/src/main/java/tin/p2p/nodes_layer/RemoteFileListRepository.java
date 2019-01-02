package tin.p2p.nodes_layer;


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

//    public ArrayList<> getFileList() {
//        return new ArrayList<>(fileList);
//    } // todo

//    public void addFileList(ArrayList<String> fileList) { //todo
//        fileList.add(fileList);
//    }
}
