package fr.pagelib.termapp;

import fr.pagelib.termapp.wsc.PrintingJob;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class PrintingController extends PageController {

    @FXML VBox jobsListBox;

    public void reset() {
        // Create a label per job to print
        jobsListBox.getChildren().clear();
        for (PrintingJob job : mainController.getCartJobs()) {
            jobsListBox.getChildren().add(new Label(job.getName()));
        }
    }

}
