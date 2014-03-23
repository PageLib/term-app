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

import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.*;
import org.joda.time.DateTime;
import org.joda.time.Duration;

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
        Text emptyTableMessage = new Text("Vous n'avez envoyé aucun document.\nVous pouvez en envoyer sur www.pagelib.fr");
        emptyTableMessage.setTextAlignment(TextAlignment.CENTER);
        table.setPlaceholder(emptyTableMessage);

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
                        String path = Configuration.getConfig().getPdfPath()
                                + File.separator + newValue.getId() + ".pdf";
                        FileOutputStream fos = new FileOutputStream(path);
                        fos.write(rv);
                        fos.close();

                        mainController.setCurrentDocumentPath(path);
                        mainController.setCurrentDocumentMetadata(newValue);
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
            String url = String.format("%s/v1/docs?user_id=%s",
                    Configuration.getConfig().getDocsEndpoint(), mainController.currentSession.getUserID());
            String rv = Request.Get(url).execute().returnContent().asString();

            JsonObject root = (JsonObject) Json.createReader(new StringReader(rv)).read();
            JsonArray documents = root.getJsonArray("documents");

            for (int i = 0; i < documents.size(); i++) {
                JsonObject document = documents.getJsonObject(i);

                String id = document.getString("id");
                String name = document.getString("name");

                String dateJson = document.getString("date_time");
                DateTime time = new DateTime(DateTime.parse(dateJson));
                Duration duration = new Duration(time, DateTime.now());

                // Choose the right unit to show the time
                long timeShow = 0;
                String unit = "";
                if (duration.getStandardDays() > 0) {
                    timeShow = duration.getStandardDays();
                    unit = " jour";
                }
                else if (duration.getStandardHours() > 0) {
                    timeShow = duration.getStandardHours();
                    unit = " heure";
                }
                else if (duration.getStandardMinutes() > 0) {
                    timeShow = duration.getStandardMinutes();
                    unit = " minute";
                }
                else if (duration.getStandardSeconds() > 0) {
                    timeShow = duration.getStandardSeconds();
                    unit = " seconde";
                }

                // Set up plural
                if(timeShow > 1) unit = String.format("%ss", unit);

                docList.add(new DocumentMetadata(name, String.format("%d %s", timeShow, unit), id));
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
