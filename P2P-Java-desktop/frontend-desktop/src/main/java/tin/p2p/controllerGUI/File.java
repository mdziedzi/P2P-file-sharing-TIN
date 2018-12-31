package tin.p2p.controllerGUI;

import java.util.ArrayList;

public class File {
    private String name;
    private String ip;
    private String hash;
    private String size;

    public File(String name, String ip, String hash, String size) {
        this.name = name;
        this.ip = ip;
        this.hash = hash;
        this.size = size;
    }

    public File(ArrayList<String> fileParams) {
        // todo obsługa błędu gdy nie wszystkie 4 pola są dostępne
        this.name = fileParams.get(0);
        this.ip = fileParams.get(1);
        this.hash = fileParams.get(2);
        this.size = fileParams.get(3);
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

    public String getSize() {
        return size;
    }
}

