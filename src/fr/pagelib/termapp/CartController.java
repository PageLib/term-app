package fr.pagelib.termapp;

import fr.pagelib.termapp.wsc.PrintingJob;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;


public class CartController extends PageController {

    @FXML ListView<PrintingJob> jobsListView;

    public void initialize() {

    }

    public void prepaidPrint() {
        mainController.showPage(MainController.Page.PRINTING);
    }

}
