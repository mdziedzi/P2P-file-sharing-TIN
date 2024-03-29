package tin.p2p.nodes_layer;

import tin.p2p.controller_layer.FrameworkController;
import tin.p2p.layers_factory.LayersFactory;
import tin.p2p.serialization_layer.Output;

import java.nio.ByteBuffer;
import java.nio.file.NoSuchFileException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

public class RemoteNode implements ReceiverInterface, SenderInterface, Comparable {
    final static Logger log = Logger.getLogger(RemoteNode.class.getName());

    private String ip;

    private Output output;

    private boolean isAuthorized = false;
    private int salt;

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
        output.requestForSaltForConnectionInTheSameNet();
    }

    @Override
    public void onNewParticipantPasswordReceived(String passwordHash) {
        authorizeNode(passwordHash);

        ArrayList<Integer> ips = RemoteNodesRepository.getItegerIpList();
        ips.removeIf(s -> s.equals(getIpAsInteger()));
        output.sendListOfNodes(ips);
    }

    private void authorizeNode(String remotePasswordAndSaltHash) {
        log.info("Received remotePasswordAndSaltHash: " + remotePasswordAndSaltHash);
        String myPassword = PasswordRepository.getPassword();
        log.info("My stored hashed password: " + myPassword);
        String myPasswordWithSalt = myPassword + salt;
        log.info("myPasswordWithSalt " + myPasswordWithSalt);

        String myPasswordWithSaltHash = null;
        try {
            myPasswordWithSaltHash = PasswordHasher.hash(myPasswordWithSalt);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        log.info("myPasswordWithSaltHash = " + myPasswordWithSaltHash);

        if (remotePasswordAndSaltHash.equals(myPasswordWithSaltHash)) {
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

            FrameworkController.getInstance().updateViewOfFilesList(
                    RemoteFileListRepository.getInstance().getFileListRaw());
        } else {
            log.warning("NOT AUTHORIZED");
            output.sendNotAuthorizedMsg();
        }
    }

    @Override
    public void onFileFragmentRequest(String fileHash, Long fileOffset) {
        if (isAuthorized) {

            log.info("onFileFragmentRequest:: FileHash: " + fileHash + "\tFileOffset: " + fileOffset.toString());

            ByteBuffer fileFragmentData = null;
            try {
                fileFragmentData = LocalFileListRepository.getInstance().getFileFragment(fileHash, fileOffset);
            } catch (NoSuchFileException e) {
                output.sendNoSuchFileMessage(fileHash, fileOffset);
                return;
            }

            output.sendFileFragment(fileHash, fileOffset, fileFragmentData);
        } else {
            log.warning("NOT AUTHORIZED");
            output.sendNotAuthorizedMsg();
        }
    }

    @Override
    public void onFileFragmentReceived(String fileHash, Long fileOffset, ByteBuffer fileFragmentData) {
        if (isAuthorized) {

            DownloadManager.getInstance().onReceivedFileFragment(fileHash, fileOffset, fileFragmentData);
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
    public Void connectToNetByIp(String passwordHash) {
        output.requestForSalt();
        return null;
    }

    private void authenticateMyself(String completeHash) {
        output.sendPassword(completeHash);
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

    @Override
    public void terminate() {
        output.terminate();
        RemoteNodesRepository.unregisterNode(this);
    }

    @Override
    public void onRequestForSaltReceiver() {
        assignSalt();
        output.sendSalt(salt);
    }

    @Override
    public void onRequestForSaltInTheSameNetReceiver() {
        assignSalt();
        output.sendSaltForConnectionInTheSameNet(salt);
    }

    @Override
    public void onSaltReceived(int receivedSalt) {
        authenticateMyself(hashContent(mergePasswordWithSalt(PasswordRepository.getPassword(), receivedSalt)));
    }

    @Override
    public void onSaltInTheSameNetReceived(int receivedSalt) {
        output.sendPasswordToRemoteNodeOfTheSameNet(hashContent(mergePasswordWithSalt(PasswordRepository.getPassword(), receivedSalt)));
    }

    @Override
    public void onDontHaveSuchFile(String fileHash, Long offset) {
        // todo przekazac do FileDownloadManagera, bez tego tez dziala tylko taki fragment pliku zostanie pobrany na koniec
        // może wyrejestrować danego node'a z posiadania takiego pliku
    }

    private String hashContent(String s) {
        String hash = null;
        try {
            hash = PasswordHasher.hash(s);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hash;
    }

    private String mergePasswordWithSalt(String typedPassword, int receivedSalt) {
        return typedPassword + receivedSalt;
    }

    private void assignSalt() {
        Random rand = new Random();
        salt = rand.nextInt();
    }


}

