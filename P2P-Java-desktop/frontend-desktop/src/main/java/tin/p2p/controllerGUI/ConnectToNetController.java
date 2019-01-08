package tin.p2p.controllerGUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import tin.p2p.controller_layer.ControllerGUIInterface;
import tin.p2p.controller_layer.FrameworkController;

import java.io.File;

public class ConnectToNetController implements ControllerGUIInterface.ConnectToNetByIPCallback {
    @FXML
    private Button connectBtn;

    @FXML
    private PasswordField existingNetPassword;

    @FXML
    private TextField ipTF;

    @FXML
    void handleConnectBtnClick(ActionEvent event) {
        ((Button)event.getSource()).getScene().getWindow().hide();
        FrameworkController.getInstance().connectToNetByIP(
                ipTF.getCharacters().toString(),
                existingNetPassword.getCharacters().toString(),
                new File("./"),
                this);
    }

    @Override
    public void onConnectToNetByIPSucces() {
        System.out.println("Connect to net success");
    }

    @Override
    public void onConnectToNetByIPReject() {
        System.out.println("Connect to net reject - WRONG PASSWORD");
    }

    @Override
    public void onConnectToNetByIPFailure() {
        System.out.println("Connect to net failure");
    }

    @Override
    public void onIPFormatFailure() {
        System.out.println("IP format failure");
    }
}

