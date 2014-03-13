package fr.pagelib.termapp;

import fr.pagelib.termapp.wsc.Configuration;
import fr.pagelib.termapp.wsc.DocumentMetadata;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.*;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;

public class CloudDocumentController extends PageController {

    @FXML TableView<DocumentMetadata> table;
    @FXML TableColumn<DocumentMetadata, String> nameColumn;
    @FXML TableColumn<DocumentMetadata, String> dateColumn;

    private ObservableList<DocumentMetadata> docList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        nameColumn.setCellValueFactory(new PropertyValueFactory<DocumentMetadata, String>("name"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<DocumentMetadata, String>("date"));
        table.setItems(docList);

        table.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<DocumentMetadata>() {

            @Override
            public void changed(ObservableValue<? extends DocumentMetadata> observable,
                                DocumentMetadata oldValue, DocumentMetadata newValue) {

                // newValue may be null when the list is cleared (in that case do not try to download the document)
                if (newValue != null) {
                    try {
                        // Download the file contents
                        String url = String.format("%s/v1/docs/%s/raw",
                                Configuration.getConfig().getDocsEndpoint(), newValue.getId());
                        byte[] rv = Request.Get(url).execute().returnContent().asBytes();

                        // Store them into a temporary file
                        String path =  Configuration.getConfig().getPdfPath()
                                + File.separator + newValue.getId() + ".pdf";
                        FileOutputStream fos = new FileOutputStream(path);
                        fos.write(rv);
                        fos.close();

                        mainController.setCurrentDocumentPath(path);
                        mainController.setCurrentDocumentMetadata(newValue);
                        mainController.showPage(MainController.Page.JOB_SETTINGS);
                    }
                    catch (IOException e) {
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
            String url = String.format("%s/v1/docs?user_id=%s",
                    Configuration.getConfig().getDocsEndpoint(), mainController.currentSession.getUserID());
            String rv = Request.Get(url).execute().returnContent().asString();

            JsonObject root = (JsonObject) Json.createReader(new StringReader(rv)).read();
            JsonArray documents = root.getJsonArray("documents");

            for (int i = 0; i < documents.size(); i++) {
                JsonObject document = documents.getJsonObject(i);

                String id = document.getString("id");
                String name = document.getString("name");

                docList.add(new DocumentMetadata(name, "1 jour", id));
            }
        }
        catch (ClientProtocolException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
