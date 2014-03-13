package fr.pagelib.termapp;

import fr.pagelib.termapp.wsc.PrintingJob;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.embed.swing.SwingFXUtils;

import org.icepdf.core.exceptions.PDFException;
import org.icepdf.core.exceptions.PDFSecurityException;
import org.icepdf.core.pobjects.Document;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.util.GraphicsRenderingHints;

import javax.print.attribute.standard.PageRanges;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 */
public class JobSettingsController extends PageController {

    @FXML Label documentNameLabel;
    @FXML TextField pagesField;
    @FXML ToggleButton colorToggle;
    @FXML Label greyLevelsHintLabel;
    @FXML TextField copiesField;
    @FXML Label priceLabel;
    @FXML Button okButton;

    @FXML ImageView pdfImageView;
    @FXML Label currentPageLabel;
    @FXML Button previousPageButton;
    @FXML Button nextPageButton;

    SimpleStringProperty pages;
    SimpleBooleanProperty color;
    SimpleStringProperty copies;

    PrintingJob printingJob;
    Document pdfDocument;
    Integer totalPages;
    SimpleIntegerProperty currentPage;

    public JobSettingsController() {
        pages = new SimpleStringProperty();
        color = new SimpleBooleanProperty();
        copies = new SimpleStringProperty();

        currentPage = new SimpleIntegerProperty(-1);
        totalPages = 0;
    }

    public void initialize() {
        // Set up data bindings
        Bindings.bindBidirectional(pages, pagesField.textProperty());
        Bindings.bindBidirectional(color, colorToggle.selectedProperty());
        Bindings.bindBidirectional(copies, copiesField.textProperty());

        // Afficher "oui" ou "non" sur le bouton "couleur" en fonction du choix
        color.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> obs, Boolean oldValue, Boolean newValue) {
                colorToggle.setText(newValue ? "Oui" : "Non");
                greyLevelsHintLabel.setVisible(!newValue);
                printingJob.setColor(newValue);
                updatePrice();
            }
        });

        currentPage.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> obs, Number oldValue, Number newValue) {
                Integer p = (Integer) newValue;

                // Navigation stuff
                currentPageLabel.setText(String.format("%d / %d", p + 1, totalPages));
                previousPageButton.setDisable(p == 0);
                nextPageButton.setDisable(p == totalPages - 1);

                // Show selected page
                BufferedImage awtImage = (BufferedImage) pdfDocument.getPageImage(
                        p, GraphicsRenderingHints.PRINT, Page.BOUNDARY_CROPBOX, 0.0f, 1.0f);
                Image fxImage = SwingFXUtils.toFXImage(awtImage, null);
                pdfImageView.setImage(fxImage);
            }
        });
        // restrict the key input to the selected characters
        pagesField.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (!" 0123456789,-".contains(keyEvent.getCharacter())) {
                    keyEvent.consume();
                }
            }
        });
        pagesField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String s2) {
                // Disable the select button if the pageRange has bot the good look
                okButton.setDisable(false);
                try{
                    printingJob.setPages(generatePageRange(s2));
                    updatePrice();
                }
                catch (IllegalArgumentException | NullPointerException e){
                    okButton.setDisable(true);
                }
            }
        });

        copiesField.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (!"0123456789".contains(keyEvent.getCharacter())) {
                    keyEvent.consume();
                }
            }
        });
        copiesField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String s2) {
                Integer copiesNumber = "".equals(s2) ? 1 : Integer.parseInt(s2);
                printingJob.setCopies(copiesNumber);
                updatePrice();
            }
        });
    }
    public void updatePrice(){
        String price = String.valueOf(printingJob.getPrice());
        int coma = price.indexOf(".");
        price = price.substring(0, Math.min(coma+2, price.length())) + " €";
        price.replace(",", ".");
        priceLabel.setText(price);
    }

    public PageRanges generatePageRange(String pagesToCheck){
        pagesToCheck = pagesToCheck.replaceAll("\\s+","");
        //si le champs est vide, imprimer toutes les pages
        if ("".equals(pagesToCheck)) {
            return new PageRanges(1, totalPages);
        }
        PageRanges pageRanges = new PageRanges(pagesToCheck);
        // Check if one of the page is no bigger than the number of page in the document
        for (int[] range : pageRanges.getMembers()) {
            for (int page : range) {
                if (totalPages < page) {
                    throw new IllegalArgumentException();
                }
            }
        }
        return pageRanges;
    }

    public void reset() {

        documentNameLabel.setText(printingJob.getName());

        pages.set("");
        okButton.setDisable(false);
        color.set(false);
        copies.set("1");

        if (pdfDocument != null) pdfDocument.dispose();

        // Load PDF document
        try {
            pdfDocument = new Document();
            pdfDocument.setFile(mainController.getCurrentDocumentPath());

            totalPages = pdfDocument.getNumberOfPages();
            currentPage.set(0);

            System.out.println(String.format("PDF document loaded, %d pages", totalPages));
        }
        catch (PDFException e) {
            System.out.println("Error parsing PDF document: " + e);
            pdfDocument = null;
        }
        catch (PDFSecurityException e) {
            System.out.println("Security error parsing PDF document: " + e);
            pdfDocument = null;
        }
        catch (FileNotFoundException e) {
            System.out.println("PDF document not found");
            pdfDocument = null;
        }
        catch (IOException e) {
            System.out.println("IOException opening PDF document: " + e);
            pdfDocument = null;
        }
        printingJob = new PrintingJob();
        printingJob.setName(mainController.getCurrentDocumentMetadata().getName());
        printingJob.setPath(mainController.getCurrentDocumentPath());
        printingJob.setCopies(1);
        printingJob.setColor(false);
        printingJob.setPages(generatePageRange(""));
        updatePrice();
    }

    @FXML
    public void previousPage() {
        int p = currentPage.get();
        if (p > 0) currentPage.set(p - 1);
    }

    @FXML
    public void nextPage() {
        int p = currentPage.get();
        if (p < totalPages - 1) currentPage.set(p + 1);
    }

    public void addToCart() {
        // Create and add the new printing job
        mainController.addCartJob(printingJob);

        // Remove the main controller's current job
        mainController.setCurrentDocumentPath(null);
        mainController.setCurrentDocumentMetadata(null);

        // Show the cart page
        mainController.showPage(MainController.Page.CART);
    }
}
