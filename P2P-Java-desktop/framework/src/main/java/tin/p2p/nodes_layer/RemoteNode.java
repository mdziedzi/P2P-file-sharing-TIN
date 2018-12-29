package tin.p2p.nodes_layer;

import tin.p2p.serialization_layer.Output;

import java.util.ArrayList;

public class RemoteNode implements ReceiverInterface, SenderInterface, Comparable{
    // todo kolejka do której wrzuca deserializator
    private Output output;

    private boolean isAuthorized = false;

    public RemoteNode(Output output) {
        this.output = output;
    }


    @Override
    public void onNodeListReceived(ArrayList<String> nodes) {

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
    }

    @Override
    public void onPasswordCorrect() {
        // todo
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

}

