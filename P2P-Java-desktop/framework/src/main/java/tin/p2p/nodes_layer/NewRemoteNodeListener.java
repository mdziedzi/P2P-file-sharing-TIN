package tin.p2p.nodes_layer;

import tin.p2p.socket_layer.NewConnectInput;

import java.io.IOException;

public class NewRemoteNodeListener extends Thread {
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

