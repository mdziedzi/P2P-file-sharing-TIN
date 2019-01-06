package tin.p2p.nodes_layer;

import tin.p2p.controller_layer.FrameworkController;
import tin.p2p.layers_factory.LayersFactory;
import tin.p2p.serialization_layer.Output;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

public class RemoteNode implements ReceiverInterface, SenderInterface, Comparable {
    final static Logger log = Logger.getLogger(RemoteNode.class.getName());

    private String ip;

    // todo kolejka do której wrzuca deserializator
    private Output output;

    private boolean isAuthorized = false;

    public RemoteNode(Output output, String ip, boolean isConnectingToUs) {
        if (!isConnectingToUs) {
            this.isAuthorized = true;
        }
        this.output = output;
        this.ip = ip;
    }


    @Override
    public void onNodeListReceived(ArrayList<String> nodes) {
        log.info("onNodeListReceived()");
        if (isAuthorized) {
            nodes.forEach((node) -> {
                        log.info("onNodeListReceived: node:" + node);
                        CompletableFuture.supplyAsync(() -> LayersFactory.initLayersOfNewRemoteNode(node))
                                .thenAccept(SenderInterface::connectToRemoteNodeOfTheSameNet)
                                .exceptionally((t) -> {
                                    t.printStackTrace();
                                    return null;
                                });
                    }
            );
        } else {
            log.warning("NOT AUTHORIZED");
            output.sendNotAuthorizedMsg();
        }

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
        log.info("Received password: " + passwordHash);
        log.info("My stored passwordHash: " + PasswordRepository.getPassword());

        if (passwordHash.equals(PasswordRepository.getPassword())) {
            log.info("Good password hash");
            isAuthorized = true;
            output.sendPasswordConfirmed(true);
        } else {
            output.sendPasswordConfirmed(false);
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
        log.info("onPasswordCorrect: ");
        FrameworkController.getInstance().initListeningForNewNodes();
    }

    @Override
    public void onPasswordReject() {
        FrameworkController.getInstance().wrongPassword();
    }

    @Override
    public void onNewPasswordReceived(String passwordHash) {
        authorizeNode(passwordHash);
    }

    @Override
    public void onFileListRequest() {
        if (isAuthorized) {
            output.sendListOfFiles(LocalFileListRepository.getInstance().getFileList());
        } else {
            log.warning("NOT AUTHORIZED");
            output.sendNotAuthorizedMsg();
        }
    }

    @Override
    public void onFileListReceived(ArrayList<ArrayList<String>> listOfFiles) {
        if (isAuthorized) {

            listOfFiles.forEach(strings -> {
                log.info("File:: Name: " + strings.get(0) + "\tHash: " + strings.get(1) + "\tSize: " + strings.get(2));
            });

            RemoteFileListRepository.getInstance().addFileList(listOfFiles, this);

            FrameworkController.getInstance().updateViewOfFilesList(listOfFiles, getIp());
        } else {
            log.warning("NOT AUTHORIZED");
            output.sendNotAuthorizedMsg();
        }
    }

    @Override
    public void onFileFragmentRequest(String fileHash, Long fileOffset) {
        if (isAuthorized) {

            //todo
            log.info("onFileFragmentRequest:: FileHash: " + fileHash + "\tFileOffset: " + fileOffset.toString());
            output.sendFileFragment(fileHash, fileOffset,
                    LocalFileListRepository.getInstance().getFileFragment(fileHash, fileOffset));
        } else {
            log.warning("NOT AUTHORIZED");
            output.sendNotAuthorizedMsg();
        }
    }

    @Override
    public void onFileFragmentReceived(String fileHash, Long fileOffset, ByteBuffer fileFragmentData) {
        if (isAuthorized) {

            DownloadManager.getInstance().onReceivedFileFragment(fileHash, fileOffset, fileFragmentData);
            //todo
            log.info("onFileFragmentReceived:: " + "fileHash: " + fileHash + "\tfileOffset: " + fileOffset);
        } else {
            log.warning("NOT AUTHORIZED");
            output.sendNotAuthorizedMsg();
        }
    }

    @Override
    public void onNotAuthorizedMsg() {
        log.warning("You have no access to perform this action");
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
        if (o instanceof RemoteNode)
            return this.ip.compareTo(((RemoteNode) o).ip);
        else
            return 1;
    }

    public String getIp() {
        return ip;
    }

    @Override
    public Void requestForFileList() {
        output.requestForFileList();
        return null;
    }

    @Override
    public void requestForFileFragment(String fileHash, Long fileOffset) {
        output.requestForFileFragment(fileOffset, fileHash);
    }


}

