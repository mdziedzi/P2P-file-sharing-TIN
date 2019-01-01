package tin.p2p.controllerGUI;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tin.p2p.controller_layer.ControllerGUIInterface;
import tin.p2p.controller_layer.FrameworkController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ControllerGUI implements ControllerGUIInterface.ListOfNodesViewer, ControllerGUIInterface.ListOfFilesCallback {

    @FXML
    private Button changeNetworkBtnId;
    @FXML
    private Button joinNetBtnId;

    @FXML
    private TableView<String> nodesTable;
    @FXML
    private TableColumn<String, String> nodeIpCol;

    public static final ObservableList<String> nodesDataList = FXCollections.observableArrayList();


    @FXML
    private TableColumn<File, String> fileNameCol;
    @FXML
    private TableColumn<File, Integer> fileSizeCol;
    @FXML
    private TableColumn<File, String> fileOwnerCol;
    @FXML
    private TableView<File> filesInNetTable;

    public static final ObservableList<File> filesInNet= FXCollections.observableArrayList();

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
    void refreshNodesTable(Event event) {
        ArrayList<String> nodes = FrameworkController.getInstance().getListOfNodes();
        setNodesTableContent(nodes);
    }


    @Override
    public void onListOfNodesUpdated(ArrayList<String> nodesIps) {
        setNodesTableContent(nodesIps);
    }

    @FXML
    void onRefreshFilesListRequest(ActionEvent event) {
        FrameworkController.getInstance().getListOfFilesInNet(this);

        filesInNet.clear();
        filesInNetTable.setItems(filesInNet);
    }

    private void setNodesTableContent(ArrayList<String> nodesIps) {
        if(nodesIps != null) {
            nodeIpCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue()));
            nodesDataList.setAll(nodesIps);
            nodesTable.setItems(nodesDataList);
        }
    }

    @Override
    public void onListOfFilesReceived(ArrayList<ArrayList<String>> filesList, String filesOwner) {
        if (filesList != null)
            setFilesTableContent(filesList.stream()
                    .map(fileAttributes -> new File(fileAttributes, filesOwner)).collect(Collectors.toList()));
    }

    private void setFilesTableContent(List<File> files) {
        fileNameCol.setCellValueFactory(new PropertyValueFactory<File, String>("name"));
        fileSizeCol.setCellValueFactory(new PropertyValueFactory<File, Integer>("size"));
        fileOwnerCol.setCellValueFactory(new PropertyValueFactory<File, String>("ip"));

        filesInNet.addAll(files);
        filesInNetTable.setItems(filesInNet);
    }
}
