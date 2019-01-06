package tin.p2p.nodes_layer;

import org.apache.commons.lang3.SystemUtils;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.logging.Logger;

import static tin.p2p.utils.Constants.FILE_NAME_LENGTH;
import static tin.p2p.utils.Constants.MAXIMUM_FILE_FRAGMENT_SIZE;

public class LocalFileListRepository {
    final static Logger log = Logger.getLogger(LocalFileListRepository.class.getName());

    private static final LocalFileListRepository instance = new LocalFileListRepository();
    private ConcurrentSkipListSet<FileDTO> fileList = new ConcurrentSkipListSet<>();


    private LocalFileListRepository() {
    }

    public static LocalFileListRepository getInstance() {
        return instance;
    }

    public  ByteBuffer getFileFragment(String fileHash, Long fileOffset) {
        URL url = getLocation(LocalFileListRepository.class);

        File directory = urlToFile(url).getParentFile();

        List<File> filesInDirectory = Arrays.asList(directory.listFiles());

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
        return null;
    }

    public ArrayList<ArrayList<String>> getFileList() {
        fileList.clear();

        URL url = getLocation(LocalFileListRepository.class);

        File directory = urlToFile(url).getParentFile();

        List<File> filesInDirectory = Arrays.asList(directory.listFiles()); // todo obsługa jeżeli null

        filesInDirectory.stream().filter(File::isFile).forEach(file -> {
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

        });

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

    private String hashFileContent(File file) throws IOException {
        String md5;
        try (InputStream is = Files.newInputStream(file.toPath())) {
            md5 = org.apache.commons.codec.digest.DigestUtils.sha256Hex(is);
        }
        return md5;
    }


    /**
     * Gets the base location of the given class.
     * <p>
     * If the class is directly on the file system (e.g.,
     * "/path/to/my/package/MyClass.class") then it will return the base directory
     * (e.g., "file:/path/to").
     * </p>
     * <p>
     * If the class is within a JAR file (e.g.,
     * "/path/to/my-jar.jar!/my/package/MyClass.class") then it will return the
     * path to the JAR (e.g., "file:/path/to/my-jar.jar").
     * </p>
     *
     * @param c The class whose location is desired.
     * FileUtils#urlToFile(URL) to convert the result to a {@link File}.
     */
    public static URL getLocation(final Class<?> c) {
        if (c == null) return null; // could not load the class

        // try the easy way first
        try {
            final URL codeSourceLocation =
                    c.getProtectionDomain().getCodeSource().getLocation();
            if (codeSourceLocation != null) return codeSourceLocation;
        }
        catch (final SecurityException e) {
            // NB: Cannot access protection domain.
        }
        catch (final NullPointerException e) {
            // NB: Protection domain or code source is null.
        }

        // NB: The easy way failed, so we try the hard way. We ask for the class
        // itself as a resource, then strip the class's path from the URL string,
        // leaving the base path.

        // get the class's raw resource path
        final URL classResource = c.getResource(c.getSimpleName() + ".class");
        if (classResource == null) return null; // cannot find class resource

        final String url = classResource.toString();
        final String suffix = c.getCanonicalName().replace('.', '/') + ".class";
        if (!url.endsWith(suffix)) return null; // weird URL

        // strip the class's path from the URL string
        final String base = url.substring(0, url.length() - suffix.length());

        String path = base;

        // remove the "jar:" prefix and "!/" suffix, if present
        if (path.startsWith("jar:")) path = path.substring(4, path.length() - 2);

        try {
            return new URL(path);
        }
        catch (final MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Converts the given {@link URL} to its corresponding {@link File}.
     * <p>
     * This method is similar to calling {@code new File(url.toURI())} except that
     * it also handles "jar:file:" URLs, returning the path to the JAR file.
     * </p>
     *
     * @param url The URL to convert.
     * @return A file path suitable for use with e.g. {@link java.io.FileInputStream}
     * @throws IllegalArgumentException if the URL does not correspond to a file.
     */
    public static File urlToFile(final URL url) {
        return url == null ? null : urlToFile(url.toString());
    }

    /**
     * Converts the given URL string to its corresponding {@link File}.
     *
     * @param url The URL to convert.
     * @return A file path suitable for use with e.g. {@link java.io.FileInputStream}
     * @throws IllegalArgumentException if the URL does not correspond to a file.
     */
    public static File urlToFile(final String url) {
        String path = url;
        if (path.startsWith("jar:")) {
            // remove "jar:" prefix and "!/" suffix
            final int index = path.indexOf("!/");
            path = path.substring(4, index);
        }
        try {
            if (SystemUtils.IS_OS_WINDOWS && path.matches("file:[A-Za-z]:.*")) {
                path = "file:/" + path.substring(5);
            }
            return new File(new URL(path).toURI());
        }
        catch (final MalformedURLException e) {
            // NB: URL is not completely well-formed.
        }
        catch (final URISyntaxException e) {
            // NB: URL is not completely well-formed.
        }
        if (path.startsWith("file:")) {
            // pass through the URL as-is, minus "file:" prefix
            path = path.substring(5);
            return new File(path);
        }
        throw new IllegalArgumentException("Invalid URL: " + url);
    }
}
