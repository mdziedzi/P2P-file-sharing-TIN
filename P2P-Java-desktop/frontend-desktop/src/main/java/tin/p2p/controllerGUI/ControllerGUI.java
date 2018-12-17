package tin.p2p.controllerGUI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import tin.p2p.controller_layer.Controller;
import tin.p2p.controller_layer.ControllerGUIInterface;
import tin.p2p.socket_layer.connection.RemoteNode;

import java.io.IOException;

public class ControllerGUI implements ControllerGUIInterface {

    private Controller backend;

    @FXML
    private Button changeNetworkBtnId;

    @FXML
    private TableView<RemoteNode> nodesTable;
    @FXML
    private TableColumn<?, ?> nodeIpCol;
    @FXML
    private TableColumn<?, ?> nodeNameCol;

    public static final ObservableList<RemoteNode> nodesDataList = FXCollections.observableArrayList();



    @FXML
    private Button connectBtn;

    @FXML
    private PasswordField existingNetPassword;

    @FXML
    private TextField ipTF;

    @FXML
    private PasswordField newNetPassword;

    @FXML
    private Button newNetBtn;

    @FXML
    private Button joinNetBtnId;

    public ControllerGUI() {
        this.backend = Controller.getInstance(this);
    }

    @FXML
    void joinNetBtnClick(ActionEvent event) throws IOException {
        FXMLLoader fXMLLoader = new FXMLLoader();
        fXMLLoader.setLocation(getClass().getClassLoader().getResource("joinNetModalWindow.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(fXMLLoader.load());
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Dołącz do sieci");
        stage.show();

        stage.setOnCloseRequest((WindowEvent event1) -> {
            loadNodesTable();
        });

    }

    @FXML
    void handleConnectBtnClick(ActionEvent event) {
        ((Stage)((Button)event.getSource()).getScene().getWindow()).hide();
        this.backend.connectToNetByIP(ipTF.getCharacters().toString(),
                existingNetPassword.getCharacters().toString(), new ConnectToNetByIPCallback() {
            @Override
            public void onConnectToNetByIPSucces() {
                System.out.println("Connect success");
            }

            @Override
            public void onConnectToNetByIPReject() {
                System.out.println("Connect reject");
            }

            @Override
            public void onConnectToNetByIPFailure() {
                System.out.println("Connect failure");
            }

            @Override
            public void onIPFormatFailure() {
                System.out.println("Bad ip format");
            }
        });

    }

    @FXML
    void newNetBtnClick(ActionEvent event) throws IOException {
        FXMLLoader fXMLLoader = new FXMLLoader();
        fXMLLoader.setLocation(getClass().getClassLoader().getResource("newNetModalWindow.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(fXMLLoader.load());
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Stwórz nową sieć");
        stage.show();

        stage.setOnCloseRequest((WindowEvent event1) -> {
            loadNodesTable();
        });

    }

    @FXML
    void handleNewNetCreateBtnClick(ActionEvent event) {
        ((Stage)((Button)event.getSource()).getScene().getWindow()).hide();
        this.backend.createNewNet(newNetPassword.getCharacters().toString(), new CreateNewNetCallback() {
            @Override
            public void onCreateNewNetSuccess() {
                System.out.println("Create new net success");

            }

            @Override
            public void onCreateNewNetFailure() {
                System.err.println("Create new net failure");
            }
        });
    }



    private void loadNodesTable() {
        nodeNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nodeIpCol.setCellValueFactory(new PropertyValueFactory<>("ip"));
        nodesDataList.clear();
//        nodesDataList.addAll(this.backend.getNodesInNetwork());
        nodesTable.setItems(nodesDataList);
    }


}
