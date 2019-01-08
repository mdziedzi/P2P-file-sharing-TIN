package tin.p2p.controllerGUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import tin.p2p.controller_layer.ControllerGUIInterface;
import tin.p2p.controller_layer.FrameworkController;

import java.io.File;

public class NewNetController implements ControllerGUIInterface.CreateNewNetCallback {
    @FXML
    private PasswordField newNetPassword;

    @FXML
    private Button newNetBtn;

    @FXML
    void handleNewNetCreateBtnClick(ActionEvent event) {
        ((Button)event.getSource()).getScene().getWindow().hide();
        FrameworkController.getInstance().createNewNet(
                newNetPassword.getCharacters().toString(),
                new File("./"),
                this);
    }

    @Override
    public void onCreateNewNetSuccess() {
        System.out.println("Create Net success");
    }

    @Override
    public void onCreateNewNetFailure() {
        System.out.println("Create Net failure");

    }
}

