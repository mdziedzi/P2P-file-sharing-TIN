package tin.p2p.controllerGUI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import tin.p2p.controller.Controller;
import tin.p2p.controller.ControllerGUIInterface;
import tin.p2p.model.Node;

import java.io.IOException;

public class ControllerGUI implements ControllerGUIInterface {

    private Controller controller;

    @FXML
    private Button changeNetworkBtnId;

    @FXML
    private TableView<Node> nodesTable;
    @FXML
    private TableColumn<?, ?> nodeIpCol;
    @FXML
    private TableColumn<?, ?> nodeNameCol;

    public static final ObservableList<Node> nodesDataList = FXCollections.observableArrayList();



    @FXML
    private Button connectBtn;

    @FXML
    private TextField usernameTF;

    @FXML
    private TextField ipTF;

    public ControllerGUI() {
        this.controller = Controller.getInstance(this);
    }

    @FXML
    void changeNetworkBtnClick(ActionEvent event) throws IOException {
        FXMLLoader fXMLLoader = new FXMLLoader();
        fXMLLoader.setLocation(getClass().getClassLoader().getResource("changeNetworkModalWindow.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(fXMLLoader.load());
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Znane IP w sieci");
        stage.show();

        stage.setOnCloseRequest((WindowEvent event1) -> {
            loadNodesTable();
        });

        System.out.println(nodesTable.getItems().toString());
    }

    @FXML
    void handleConnectBtnClick(ActionEvent event) {
        ((Stage)((Button)event.getSource()).getScene().getWindow()).hide();
        this.controller.connectToNetwork(usernameTF.getCharacters().toString(), ipTF.getCharacters().toString());

    }



    private void loadNodesTable() {
        nodeNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nodeIpCol.setCellValueFactory(new PropertyValueFactory<>("ip"));
        nodesDataList.clear();
        nodesDataList.addAll(this.controller.getNodesInNetwork());
        nodesTable.setItems(nodesDataList);
    }

    @Override
    public void onCreateNewNetSuccess() {
        //todo
    }

    @Override
    public void onCreateNewNetFailure() {
        //todo
    }

    @Override
    public void onConnectToNetByIPSucces() {
        //todo
    }

    @Override
    public void onConnectToNetByIPReject() {
        //todo
    }

    @Override
    public void onConnectToNetByIPFailure() {
        //todo
    }
}