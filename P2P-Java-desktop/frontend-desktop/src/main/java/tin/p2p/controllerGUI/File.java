package tin.p2p.controllerGUI;

import java.util.ArrayList;
import java.util.Collection;

public class File {
    private String name;
    private String owners;
    private String hash;
    private Long size;

    public File(ArrayList<String> fileParams) {
        this.name = fileParams.get(0);
        this.hash = fileParams.get(1);
        this.size = Long.valueOf(fileParams.get(2));
        this.owners = fileParams.get(3);
    }

    public String getName() {
        return name;
    }

    public String getOwners() {
        return owners;
    }

    public String getHash() {
        return hash;
    }

    public Long getSize() {
        return size;
    }

    @Override
    public String toString() {
        return "File{" +
                "name='" + name + '\'' +
                ", owners='" + owners.toString() + '\'' +
                ", hash='" + hash + '\'' +
                ", size='" + size + '\'' +
                '}';
    }
}

