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
    private TextField usernameTF;

    @FXML
    private TextField ipTF;

    public ControllerGUI() {
        this.backend = Controller.getInstance(this);
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
        this.backend.connectToNetByIP(ipTF.getCharacters().toString(), new ConnectToNetByIPCallback() {
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
    void handleNewNetBtnClick(ActionEvent event) {
        ((Stage)((Button)event.getSource()).getScene().getWindow()).hide();
//        this.backend.connectToNetwork(usernameTF.getCharacters().toString(), ipTF.getCharacters().toString());

    }



    private void loadNodesTable() {
        nodeNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nodeIpCol.setCellValueFactory(new PropertyValueFactory<>("ip"));
        nodesDataList.clear();
//        nodesDataList.addAll(this.backend.getNodesInNetwork());
        nodesTable.setItems(nodesDataList);
    }


}
