package tin.p2p.nodes_layer;

import java.util.concurrent.ConcurrentSkipListSet;

public class FileListRepository {
    private static final FileListRepository instance = new FileListRepository();
    private ConcurrentSkipListSet<FileDTO> fileList = new ConcurrentSkipListSet<>();


    private FileListRepository() {
    }

    public static FileListRepository getInstance() {
        return instance;
    }

//    public ArrayList<> getFileList() {
//        return new ArrayList<>(fileList);
//    } // todo

//    public void addFileList(ArrayList<String> fileList) { //todo
//        fileList.add(fileList);
//    }
}
