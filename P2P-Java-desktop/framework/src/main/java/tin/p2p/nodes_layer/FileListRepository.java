package tin.p2p.nodes_layer;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentSkipListSet;

public class FileListRepository {
    private static final FileListRepository instance = new FileListRepository();
    private ConcurrentSkipListSet<ArrayList<String>> fileList = new ConcurrentSkipListSet<>();


    private FileListRepository() {
    }

    public static FileListRepository getInstance() {
        return instance;
    }

    public ArrayList<ArrayList<String>> getFileList() {
        return new ArrayList<>(fileList);
    }
}
