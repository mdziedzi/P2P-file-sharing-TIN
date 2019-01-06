package tin.p2p.controllerGUI;

import java.util.ArrayList;
import java.util.Collection;

public class File {
    private String name;
    private Collection<String> ips = new ArrayList<>();
    private String hash;
    private Long size;

    public File(ArrayList<String> fileParams, String filesOwner) {
        // todo obsługa błędu gdy nie wszystkie 4 pola są dostępne
        this.name = fileParams.get(0);
        this.hash = fileParams.get(1);
        this.size = Long.valueOf(fileParams.get(2));
        this.ips.add(filesOwner);
    }

    public String getName() {
        return name;
    }

    public Collection<String> getIps() {
        return ips;
    }

    public String getHash() {
        return hash;
    }

    public Long getSize() {
        return size;
    }

    public void addOwner(String owner) {
        ips.add(owner);
    }

    @Override
    public String toString() {
        return "File{" +
                "name='" + name + '\'' +
                ", ips='" + ips.toString() + '\'' +
                ", hash='" + hash + '\'' +
                ", size='" + size + '\'' +
                '}';
    }
}

