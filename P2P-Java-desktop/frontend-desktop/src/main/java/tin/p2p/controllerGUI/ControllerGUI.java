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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import tin.p2p.controller_layer.ControllerGUIInterface;
import tin.p2p.controller_layer.FrameworkController;

import java.io.IOException;
import java.util.ArrayList;

public class ControllerGUI implements ControllerGUIInterface.ListOfNodesViewer {

    @FXML
    private Button changeNetworkBtnId;
    @FXML
    private Button joinNetBtnId;

    @FXML
    private TableView<String> nodesTable;
    @FXML
    private TableColumn<?, ?> nodeIpCol;

    public static final ObservableList<String> nodesDataList = FXCollections.observableArrayList();


    @FXML
    private TableColumn<?, ?> fileNameCol;
    @FXML
    private TableView<?> filesInNetTable;

    public ControllerGUI() {
        FrameworkController.getInstance().registerListOfNodesViewer(this);
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

//        stage.setOnCloseRequest((WindowEvent event1) -> {
//            loadNodesTable();
//        });

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

//        stage.setOnCloseRequest((WindowEvent event1) -> {
//            loadNodesTable();
//        });

    }

    @FXML
    void refreshNodesTable(ActionEvent event) {
        nodesDataList.clear();
        nodesDataList.addAll(FrameworkController.getInstance().getListOfNodes());
        nodesTable.setItems(nodesDataList);
    }


    @Override
    public void onListOfNodesUpdated(ArrayList<String> nodesIps) {
        nodesIps.forEach(System.out::println);
        nodeIpCol.setCellValueFactory(new PropertyValueFactory<>("ip"));
        nodesDataList.clear();
        nodesDataList.addAll(nodesIps);
        nodesTable.setItems(nodesDataList);
    }
}
