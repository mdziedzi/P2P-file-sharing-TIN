package tin.p2p.controllerGUI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

public class Controller {
    @FXML
    private Button showOtherNodesBtn;

    @FXML
    private ListView<String> nodesList;

    public static final ObservableList<String> data = FXCollections.observableArrayList();

    @FXML
    void handleOtherNodesBtnClick(ActionEvent event) {
        data.clear();
        data.addAll("test1", "test2");
        nodesList.setItems(data);
        System.out.println(nodesList.getItems().toString());



    }
}
