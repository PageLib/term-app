package fr.pagelib.termapp;

import fr.pagelib.termapp.wsc.PrintingJob;
import fr.pagelib.termapp.wsc.exc.InvoicingException;
import fr.pagelib.termapp.wsc.model.PrintingTransaction;
import fr.pagelib.termapp.wsc.repo.TransactionRepository;
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

    class PrintJobCell extends ListCell<PrintingJob> {
        HBox hbox = new HBox();
        Label label = new Label("(empty)");
        Pane pane = new Pane();
        Button button = new Button("Supprimer");
        PrintingJob job;

        public PrintJobCell() {
            super();
            hbox.getChildren().addAll(label, pane, button);
            HBox.setHgrow(pane, Priority.ALWAYS);
            button.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent actionEvent) {
                    jobList.remove(job);
                    mainController.removeCartJob(job);
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
                job = printingJob;
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
        // TODO something cleaner for the copies
        int pageGreyLevel = 0;
        int pageColor = 0;
        for(PrintingJob job: jobList){
            if(job.getColor()){
                pageColor += job.getCopies();
            }else {
                pageGreyLevel += job.getCopies();
            }
        }
        double amount = 0.2 * pageColor + 0.1 * pageGreyLevel;
        PrintingTransaction printingTransaction = new PrintingTransaction(
                mainController.getCurrentSession().getUserID(),
                -amount,
                "EUR",
                pageColor,
                pageGreyLevel,
                1
        );
        TransactionRepository transactionRepository = new TransactionRepository(mainController.getWsConfig(), mainController.getCurrentSession());
        try {
            transactionRepository.post(printingTransaction);
            mainController.showPage(MainController.Page.PRINTING);
        } catch (InvoicingException e) {
            e.printStackTrace();
        }
    }

    public void addDocument(){
        mainController.showPage(MainController.Page.SOURCE);
    }

}
