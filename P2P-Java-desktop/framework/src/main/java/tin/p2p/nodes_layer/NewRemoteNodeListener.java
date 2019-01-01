package tin.p2p.nodes_layer;

import org.apache.log4j.Logger;
import tin.p2p.socket_layer.NewConnectInput;

import java.io.IOException;

public class NewRemoteNodeListener extends Thread {
    final static Logger log = Logger.getLogger(NewRemoteNodeListener.class.getName());

    private NewConnectInput newConnectInput;

    public NewRemoteNodeListener(NewConnectInput newConnectInput) {
        this.newConnectInput = newConnectInput;
    }

    @Override
    public void run() {
        while (true) {
            try {
                newConnectInput.acceptNewNode();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

