package tin.p2p.controllerGUI;

import java.util.ArrayList;

public class File {
    private String name;
    private String ip;
    private String hash;
    private Integer size;

    public File(ArrayList<String> fileParams, String filesOwner) {
        // todo obsługa błędu gdy nie wszystkie 4 pola są dostępne
        this.name = fileParams.get(0);
        this.hash = fileParams.get(1);
        this.size = Integer.valueOf(fileParams.get(2));
        this.ip = filesOwner;
    }

    public String getName() {
        return name;
    }

    public String getIp() {
        return ip;
    }

    public String getHash() {
        return hash;
    }

    public Integer getSize() {
        return size;
    }

    @Override
    public String toString() {
        return "File{" +
                "name='" + name + '\'' +
                ", ip='" + ip + '\'' +
                ", hash='" + hash + '\'' +
                ", size='" + size + '\'' +
                '}';
    }
}

