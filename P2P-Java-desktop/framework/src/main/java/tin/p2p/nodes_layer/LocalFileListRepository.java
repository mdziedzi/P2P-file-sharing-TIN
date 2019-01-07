package tin.p2p.nodes_layer;

import tin.p2p.utils.Properties;

import java.io.*;
import java.nio.ByteBuffer;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.logging.Logger;

import static tin.p2p.utils.Constants.FILE_NAME_LENGTH;
import static tin.p2p.utils.Constants.MAXIMUM_FILE_FRAGMENT_SIZE;

public class LocalFileListRepository {
    final static Logger log = Logger.getLogger(LocalFileListRepository.class.getName());

    private static final LocalFileListRepository instance = new LocalFileListRepository();
    private ConcurrentSkipListSet<FileDTO> fileList = new ConcurrentSkipListSet<>();

    private File workspaceFolder; // todo init na początku


    private LocalFileListRepository() {
    }

    public static LocalFileListRepository getInstance() {
        return instance;
    }

    public  ByteBuffer getFileFragment(String fileHash, Long fileOffset) {
        File workspaceFolder = Properties.getWorkspaceDirectory();

        if (workspaceFolder != null && workspaceFolder.isDirectory()) {

            List<File> filesInDirectory = Arrays.asList(workspaceFolder.listFiles());

            for (FileDTO fileDTO : fileList) {
                if (fileDTO.getHash().equals(fileHash)) {
                    Optional<File> foundFile = filesInDirectory.stream()
                            .filter(file -> file.getName().equals(fileDTO.getName())).findFirst();
                    if (foundFile.isPresent()) {
                        long fileSize = foundFile.get().length();
                        ByteBuffer byteBuffer = ByteBuffer.allocate(
                                fileSize - fileOffset < MAXIMUM_FILE_FRAGMENT_SIZE ? (int) (fileSize - fileOffset) : MAXIMUM_FILE_FRAGMENT_SIZE);

                        RandomAccessFile file = null;
                        try {
                            file = new RandomAccessFile(foundFile.get(), "r");
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        try {
                            file.seek(fileOffset);
                            file.read(byteBuffer.array(), 0, byteBuffer.array().length);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return byteBuffer;

                    }
                }
            }
        }


        return null;
    }

    public ArrayList<ArrayList<String>> getFileList() {
        fileList.clear();

        loadLocalFiles();

        ArrayList<ArrayList<String>> stringFileList = new ArrayList<>();

        fileList.forEach(fileDTO -> {
            ArrayList<String> tmp = new ArrayList<>();
            tmp.add(fileDTO.getName());
            tmp.add(fileDTO.getHash());
            tmp.add(fileDTO.getSize().toString());
            stringFileList.add(tmp);
        });

        return stringFileList;
    }

    private void loadLocalFiles() {
        File workspaceFolder = Properties.getWorkspaceDirectory();

        if (workspaceFolder != null && workspaceFolder.isDirectory()) {
            List<File> filesInDirectory = Arrays.asList(Objects.requireNonNull(workspaceFolder.listFiles())); // todo obsługa jeżeli null

            filesInDirectory.stream().filter(File::isFile).forEach(this::addToFilesList);
        }
    }

    private void addToFilesList(File file) {
        String fileName = file.getName();
        if (fileName.length() > FILE_NAME_LENGTH) {
            log.warning("File name too long. This file is omitted in sharing on net: " + fileName);
        } else {
            Long fileSize = file.length();
            String fileHash = null;
            try {
                fileHash = hashFileContent(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            fileList.add(new FileDTO(fileName, fileHash, fileSize));
        }
    }

    private String hashFileContent(File file) throws IOException {
        MessageDigest sha256 = null;
        try {
            sha256 = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        InputStream is = new FileInputStream(file);

        byte[] buffer = new byte[1024];

        int numRead;
        do {
            numRead = is.read(buffer);
            if (numRead > 0) {
                sha256.update(buffer, 0, numRead);
            }
        } while (numRead != -1);

        is.close();

        byte[] digest = sha256.digest();
        StringBuilder hexStr = new StringBuilder();

        for (byte aDigest : digest)
            hexStr.append(Integer.toString((aDigest & 0xff) + 0x100, 16).substring(1));

        return hexStr.toString();
    }

}
