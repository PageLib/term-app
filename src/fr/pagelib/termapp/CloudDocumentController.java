package fr.pagelib.termapp;


import fr.pagelib.termapp.wsc.DocumentMetadata;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.*;
import org.apache.http.entity.ContentType;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonStructure;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;

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
        table.setItems(docList);

        table.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<DocumentMetadata>() {

            @Override
            public void changed(ObservableValue<? extends DocumentMetadata> observable,
                                DocumentMetadata oldValue, DocumentMetadata newValue) {
                    // Le if permet d'éviter un bug lors du reset de la liste si
                    // on retourne en arrière et que la liste est vidée.
                    if(newValue != null){

                        try {
                            String url = mainController.wsConfig.getDocsEndpoint() + "/v1/docs/"
                                    + newValue.getId() + "/raw";
                            byte[] rv = Request.Get(url).execute().returnContent().asBytes();
                            String path = ".pdf";  //TODO
                            FileOutputStream fos = new FileOutputStream("D:/test.pdf");
                            fos.write(rv);
                            fos.close();
                            mainController.setCurrentDocumentMetadata(newValue);

                            System.out.println(mainController.currentDocumentMetadata.getName());
                            mainController.showPage(MainController.Page.JOB_SETTINGS);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
    }


    public void init() {
        // Update the list content from the document service
        docList.clear();
        try {

            String url = mainController.wsConfig.getDocsEndpoint() + "/v1/docs?user_id="
                    + mainController.currentSession.getUserID();
            String rv = Request.Get(url).execute().returnContent().asString();
            JsonStructure rvJson = Json.createReader(new StringReader(rv)).read();
            JsonObject root = (JsonObject) rvJson;
            JsonArray documents = root.getJsonArray("documents");

            for(int i = 0 ; i < documents.size() ; i++){
                JsonObject document = documents.getJsonObject(i);
                String id = document.getString("id");

                // TODO?? Gérer les noms nulls
                String name = document.getString("name");

                docList.add(new DocumentMetadata(name, "1 jour", id));
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
