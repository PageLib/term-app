package fr.pagelib.termapp;

import fr.pagelib.termapp.wsc.DocumentMetadata;
import javafx.fxml.FXML;

public class SourceController extends PageController{

    @FXML
    public void usbDocumentAction () {
        mainController.showPage(MainController.Page.USB_DOCUMENT);
    }

    @FXML
    public void cloudDocumentAction() {
        //mainController.showPage(MainController.Page.CLOUD_DOCUMENT);

        // Temporary override for job_settings branch
        mainController.setCurrentDocumentPath("/Users/raphael/Projects/pagelib/tmpdoc.pdf");
        mainController.setCurrentDocumentMetadata(new DocumentMetadata("Test Document.pdf", "Today"));
        mainController.showPage(MainController.Page.JOB_SETTINGS);
    }
}
