package fr.pagelib.termapp;


import fr.pagelib.termapp.wsc.DocumentMetadata;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class CloudDocumentController extends PageController {

    @FXML TableView<DocumentMetadata> table;
    @FXML TableColumn<DocumentMetadata, String> nameColumn;
    @FXML TableColumn<DocumentMetadata, String> dateColumn;

    private ObservableList<DocumentMetadata> docList = FXCollections.observableArrayList();

    public CloudDocumentController(){

    }

    @FXML
    public void initialize() {

        nameColumn.setCellValueFactory(
                new PropertyValueFactory<DocumentMetadata, String>("name")
        );
        dateColumn.setCellValueFactory(
                new PropertyValueFactory<DocumentMetadata, String>("date")
        );

        docList.clear();
        docList.add(new DocumentMetadata("Rapport 1", "Hier"));
        docList.add(new DocumentMetadata("Rapport 2", "Hier"));
        docList.add(new DocumentMetadata("Rapport 3", "Hier"));
        table.setItems(docList);
        System.out.println("???");
    }

}
