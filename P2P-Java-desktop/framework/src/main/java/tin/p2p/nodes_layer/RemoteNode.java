package tin.p2p.nodes_layer;

import tin.p2p.serialization_layer.Output;

import java.util.ArrayList;

public class RemoteNode implements ReceiverInterface, SenderInterface, Comparable{
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
        nodes.forEach(System.out::println);
    }

    @Override
    public void onPasswordReceived(String password) {
        System.out.println("Received passowrd (remote, my):");
        System.out.println(password);
        System.out.println(PasswordRepository.getPassword());
        if (password.equals(PasswordRepository.getPassword())) {
            System.out.println("good password");
            isAuthorized = true;
            output.sendPasswordConfirmed(true);
        }


        ArrayList<Integer> ips = RemoteNodesRepository.getItegerIpList();
        ips.removeIf(s -> s.equals(getIpAsInteger()));
        output.sendListOfNodes(ips);
    }

//    public ArrayList<Integer> getIpNumber() {
//        ArrayList<Integer> ips = RemoteNodesRepository.getItegerIpList();
//        ips.removeIf(i -> i.equals())
//    }

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
}

