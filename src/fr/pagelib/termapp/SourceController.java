package fr.pagelib.termapp;


import javafx.fxml.FXML;

public class SourceController extends PageController{

    @FXML
    public void usbDocumentAction () {
        mainController.showPage(MainController.Page.USB_DOCUMENT);
    }

    @FXML
    public void cloudDocumentAction() {
        mainController.showPage(MainController.Page.CLOUD_DOCUMENT);
    }
}
