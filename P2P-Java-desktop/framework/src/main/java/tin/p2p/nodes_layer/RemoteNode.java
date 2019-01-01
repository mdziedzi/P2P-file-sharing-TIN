package tin.p2p.nodes_layer;

import org.apache.log4j.Logger;
import tin.p2p.controller_layer.FrameworkController;
import tin.p2p.layers_factory.LayersFactory;
import tin.p2p.serialization_layer.Output;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class RemoteNode implements ReceiverInterface, SenderInterface, Comparable {
    final static Logger log = Logger.getLogger(RemoteNode.class.getName());

    private String ip;

    // todo kolejka do której wrzuca deserializator
    private Output output;

    private boolean isAuthorized = false;

    public RemoteNode(Output output, String ip) {
        this.output = output;
        this.ip = ip;
    }


    @Override
    public void onNodeListReceived(ArrayList<String> nodes) {
        log.debug("onNodeListReceived");

        nodes.forEach((node) -> {
                    log.debug("onNodeListReceived: node:" + node);
                    CompletableFuture.supplyAsync(() -> LayersFactory.initLayersOfNewRemoteNode(node))
                            .thenAccept(SenderInterface::connectToRemoteNodeOfTheSameNet)
                            .exceptionally((t) -> {
                                t.printStackTrace();
                                return null;
                            });
                }
        );

    }

    @Override
    public void connectToRemoteNodeOfTheSameNet() {
        output.sendPasswordToRemoteNodeOfTheSameNet(PasswordRepository.getPassword());
    }

    @Override
    public void onNewParticipantPasswordReceived(String passwordHash) {
        authorizeNode(passwordHash);

        ArrayList<Integer> ips = RemoteNodesRepository.getItegerIpList();
        ips.removeIf(s -> s.equals(getIpAsInteger()));
        output.sendListOfNodes(ips);
    }

    private void authorizeNode(String passwordHash) {
        log.debug("Received password: " + passwordHash);
        log.debug("My stored passwordHash: " + PasswordRepository.getPassword());

        if (passwordHash.equals(PasswordRepository.getPassword())) {
            log.debug("Good password hash");
            isAuthorized = true;
            output.sendPasswordConfirmed(true);
        }
    }

    public int getIpAsInteger() { // todo: move it to serializator
        String[] parts;
        int ipNumber = 0;
        parts = ip.split("\\.");
        for (int i = 0; i < parts.length; i++) {
            ipNumber += Integer.parseInt(parts[i]) << (24 - (8 * i));
        }
        return ipNumber;
    }

    @Override
    public void onPasswordCorrect() {
        // todo: powiedz ze sie polaczylismy z siecia
        log.debug("onPasswordCorrect: ");
        FrameworkController.getInstance().initListeningForNewNodes();
    }

    @Override
    public void onPasswordReject() {
        // todo
    }

    @Override
    public void onNewPasswordReceived(String passwordHash) {
        authorizeNode(passwordHash);
    }

    @Override
    public void onFileListRequest() {
        output.sendListOfFiles(LocalFileListRepository.getInstance().getFileList());
        // todo

    }

    @Override
    public void onFileListReceived(ArrayList<ArrayList<String>> listOfFiles) {
        listOfFiles.forEach(strings -> {
            log.debug("File:: Name: " + strings.get(0) + "\tHash: " + strings.get(1) + "\tSize: " + strings.get(2));
        });


        FrameworkController.getInstance().updateViewOfFilesList(listOfFiles);
    }

    @Override
    public Void connectToNetByIp(String password) {
        authenticateMyself(password);
        return null;
    }

    private void authenticateMyself(String password) {
        output.sendPassword(password);
        //todo
    }

    @Override
    public int compareTo(Object o) {
        return this == o ? 0 : 1;
    }

    public String getIp() {
        return ip;
    }

    @Override
    public Void requestForFileList() {
        output.requestForFileList();
        return null;
    }
}

