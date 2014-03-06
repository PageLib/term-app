package fr.pagelib.termapp;

import fr.pagelib.termapp.wsc.PrintingJob;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Chromaticity;
import javax.print.attribute.standard.Copies;
import javax.print.event.PrintJobEvent;
import javax.print.event.PrintJobAdapter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class PrintingController extends PageController {

    private static class JobListener extends PrintJobAdapter {
        private PrintingJob printingJob;
        private Boolean completed;

        public JobListener(PrintingJob printingJob) {
            completed = false;
            this.printingJob = printingJob;
            log("initialized job listener");
        }

        private Boolean getCompleted() {
            return completed;
        }

        private void log(String message) {
            System.out.println(String.format("%s: %s", printingJob, message));
        }

        @Override
        public void printJobCompleted(PrintJobEvent e) {
            log("job completed");
            completed = true;
        }

        @Override
        public void printJobCanceled(PrintJobEvent e) {
            log("job canceled");
        }

        @Override
        public void printJobFailed(PrintJobEvent e) {
            log("job failed");
        }

        @Override
        public void printDataTransferCompleted(PrintJobEvent e) {
            log("data transfer completed");
        }

        @Override
        public void printJobNoMoreEvents(PrintJobEvent e) {
            completed = true;
            log("no more events");
        }

        @Override
        public void printJobRequiresAttention(PrintJobEvent e) {
            log("job requires attention");
        }
    }

    @FXML VBox jobsListBox;

    Timeline monitoringTimeline;
    PrintService printer;
    ArrayList<JobListener> jobListeners;

    public void reset() {
        startJobs();
        startMonitoring();
    }

    /**
     * Find and set the controller's print service using a regex pattern. If no installed printer matches the pattern,
     * throw a PrinterNotFoundException.
     */
    public void findPrinter(String pattern) throws PrinterNotFoundException {

        PrintService[] services = PrintServiceLookup.lookupPrintServices(DocFlavor.INPUT_STREAM.PDF, null);

        for (PrintService service : services) {
            if (Pattern.matches(pattern, service.toString())) {
                printer = service;
                System.out.println(String.format("Found matching printer: %s", service.toString()));
                return;
            }
        }

        throw new PrinterNotFoundException(pattern);
    }

    public void startJobs() {
        jobListeners = new ArrayList<JobListener>();
        jobsListBox.getChildren().clear();

        for (PrintingJob job : mainController.getCartJobs()) {
            try {
                // Create a javax.print.SimpleDoc
                FileInputStream printerStream = new FileInputStream(job.getPath());
                SimpleDoc printerDoc = new SimpleDoc(printerStream, DocFlavor.INPUT_STREAM.PDF, null);

                // Prepare print settings
                PrintRequestAttributeSet printerAttributes = new HashPrintRequestAttributeSet();
                if (job.getPages() != null) printerAttributes.add(job.getPages());
                printerAttributes.add(new Copies(job.getCopies()));
                printerAttributes.add(job.getColor() ? Chromaticity.COLOR : Chromaticity.MONOCHROME);

                // Start the job
                DocPrintJob printerJob = printer.createPrintJob();

                // Hook and save a listener
                JobListener listener = new JobListener(job);
                printerJob.addPrintJobListener(listener);
                jobListeners.add(listener);

                // Add the corresponding label
                jobsListBox.getChildren().add(new Label(job.getName()));

                printerJob.print(printerDoc, printerAttributes);
            }
            catch (FileNotFoundException e) {
                System.err.println(String.format("Skipping job '%s': file '%s' not found.",
                        job.getName(), job.getPath()));
            }
            catch (PrintException e) {
                System.err.println(String.format("Unhandled PrintException while printing job '%s'", job.getName()));
                e.printStackTrace();
            }
        }
    }

    public void startMonitoring() {
        System.out.println("starting monitoring");

        // Start monitoring timer
        monitoringTimeline = new Timeline(new KeyFrame(
                Duration.seconds(1),
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        monitorJobs();
                    }
                }));
        monitoringTimeline.setCycleCount(Timeline.INDEFINITE);
        monitoringTimeline.play();
    }

    public void monitorJobs() {

        Boolean allJobsComplete = true;

        for (int i = 0; i < jobListeners.size(); i++) {
            if (jobListeners.get(i).getCompleted()) {
                Label label = (Label) jobsListBox.getChildren().get(i);
                label.getStyleClass().add("job-completed-label");
            }
            else allJobsComplete = false;
        }

        if (allJobsComplete) {
            monitoringTimeline.stop();
            mainController.logoutAction();
        }
    }

}
