package tin.p2p.nodes_layer;

import tin.p2p.layers_factory.LayersFactory;
import tin.p2p.serialization_layer.Output;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class RemoteNode implements ReceiverInterface, SenderInterface, Comparable {
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
        // todo
        nodes.forEach((node) -> {
                    System.out.println(node);
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
        System.out.println("Received passowrd (remote, my):");
        System.out.println(passwordHash);
        System.out.println(PasswordRepository.getPassword());
        if (passwordHash.equals(PasswordRepository.getPassword())) {
            System.out.println("good passwordHash");
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
        // todo callback na froncie

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
        // todo

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

