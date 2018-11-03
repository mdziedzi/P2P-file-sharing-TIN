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
import tin.p2p.model.Node;

import java.io.IOException;

public class Controller {
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
            mockupNodesData();
        });

        System.out.println(nodesTable.getItems().toString());
    }

    @FXML
    void handleConnectBtnClick(ActionEvent event) {
        ((Stage)((Button)event.getSource()).getScene().getWindow()).hide();

    }


    private void mockupNodesData() {
        nodeNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nodeIpCol.setCellValueFactory(new PropertyValueFactory<>("ip"));
        nodesDataList.clear();
        nodesDataList.addAll(new Node("test1", "11.111.111.111"), new Node("test2", "0.0.0.0"));
        nodesTable.setItems(nodesDataList);
    }
}
