package fr.pagelib.termapp;

import fr.pagelib.termapp.wsc.PrintingJob;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.util.Callback;


public class CartController extends PageController {

    @FXML ListView<PrintingJob> jobsListView;

    ObservableList<PrintingJob> jobList = FXCollections.observableArrayList();

    static class PrintJobCell extends ListCell<PrintingJob> {
        HBox hbox = new HBox();
        Label label = new Label("(empty)");
        Pane pane = new Pane();
        Button button = new Button("Supprimer");

        public PrintJobCell() {
            super();
            hbox.getChildren().addAll(label, pane, button);
            HBox.setHgrow(pane, Priority.ALWAYS);
            button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {

                }
            });
        }

        @Override
        protected void updateItem(PrintingJob printingJob, boolean empty) {
            super.updateItem(printingJob, empty);
            if (empty){
                setGraphic(null);
            }
            else {
                label.setText(printingJob.getName() != null ? printingJob.getName() : "Sans nom");
                setGraphic(hbox);
            }
        }

    }

    @FXML
    public void initialize() {
        jobsListView.setItems(jobList);
        jobsListView.setCellFactory(new Callback<ListView<PrintingJob>, ListCell<PrintingJob>>() {
            @Override
            public ListCell<PrintingJob> call(ListView<PrintingJob> printingJobListView) {
                return new PrintJobCell();
            }
        });
    }

    public void reset() {
        jobList.clear();
        for(PrintingJob job:mainController.getCartJobs()){
            jobList.add(job);
        }
    }

    public void prepaidPrint() {

    }

}
