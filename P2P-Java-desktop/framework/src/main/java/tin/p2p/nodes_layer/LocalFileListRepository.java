package tin.p2p.nodes_layer;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentSkipListSet;

public class LocalFileListRepository {
    final static Logger log = Logger.getLogger(LocalFileListRepository.class.getName());

    private static final LocalFileListRepository instance = new LocalFileListRepository();
    private ConcurrentSkipListSet<FileDTO> fileList = new ConcurrentSkipListSet<>();


    private LocalFileListRepository() {
    }

    public static LocalFileListRepository getInstance() {
        return instance;
    }

    public ArrayList<ArrayList<String>> getFileList() {

        ArrayList<ArrayList<String>> stringFileList = new ArrayList<>();

        fileList.forEach(fileDTO -> {
            ArrayList<String> tmp = new ArrayList<>();
            tmp.add(fileDTO.getName());
            tmp.add(fileDTO.getSize());
            tmp.add(fileDTO.getHash());
            tmp.add(fileDTO.getIp());
            stringFileList.add(tmp);
        });

        return stringFileList;
    }
}
