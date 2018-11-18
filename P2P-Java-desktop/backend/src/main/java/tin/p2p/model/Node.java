package tin.p2p.model;

import javafx.beans.property.SimpleStringProperty;

public class Node {
    private SimpleStringProperty name;
    private SimpleStringProperty ip;

    public Node(String name, String ip) {
        this.name = new SimpleStringProperty(name);
        this.ip = new SimpleStringProperty(ip);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getIp() {
        return ip.get();
    }

    public SimpleStringProperty ipProperty() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip.set(ip);
    }
}

