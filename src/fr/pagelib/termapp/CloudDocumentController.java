package fr.pagelib.termapp;


import fr.pagelib.termapp.wsc.DocumentMetadata;
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
import java.io.IOException;
import java.io.StringReader;

public class CloudDocumentController extends PageController {

    @FXML TableView<DocumentMetadata> table;
    @FXML TableColumn<DocumentMetadata, String> nameColumn;
    @FXML TableColumn<DocumentMetadata, String> dateColumn;

    private ObservableList<DocumentMetadata> docList = FXCollections.observableArrayList();

    public CloudDocumentController(){

    }


    public void init() {

        nameColumn.setCellValueFactory(
                new PropertyValueFactory<DocumentMetadata, String>("name")
        );
        dateColumn.setCellValueFactory(
                new PropertyValueFactory<DocumentMetadata, String>("date")
        );

        docList.clear();
        try {

            String url = mainController.wsConfig.getDocsEndpoint() + "/v1/docs?user_id="
                    + mainController.currentSession.getUserID();
            String rv = Request.Get(url).execute().returnContent().asString();
            JsonStructure rvJson = Json.createReader(new StringReader(rv)).read();
            JsonObject root = (JsonObject) rvJson;
            JsonArray documents = root.getJsonArray("documents");
            System.out.println(documents);

            for(int i = 0 ; i < documents.size() ; i++){
                JsonObject document = documents.getJsonObject(i);
                String id = document.getString("id");

                // TODO?? Gérer les noms nulls
                String name = document.getString("name");

                docList.add(new DocumentMetadata(name, "Hier"));
            }


        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        table.setItems(docList);
    }

}
