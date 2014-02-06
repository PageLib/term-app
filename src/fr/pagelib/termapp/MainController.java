package fr.pagelib.termapp;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

public class MainController {

    @FXML GridPane headerGrid;
    @FXML Button backButton;
    @FXML Label pageTitleLabel;
    @FXML Label userNameLabel;
    @FXML Button logoutButton;

    @FXML StackPane pagesContainer;
    @FXML AnchorPane homePage;
    @FXML AnchorPane loginPage;
    @FXML AnchorPane sourcePage;
    @FXML AnchorPane usbDocumentPage;
    @FXML AnchorPane cloudDocumentPage;
    @FXML AnchorPane jobSettingsPage;
    @FXML AnchorPane cartPage;
    @FXML AnchorPane printingPage;

    enum Page {  // used for showPage() method
        HOME,
        LOGIN,
        SOURCE,
        USB_DOCUMENT,
        CLOUD_DOCUMENT,
        JOB_SETTINGS,
        CART,
        PRINTING
    }

    Page currentPage;
    Page previousPage;

    public void initialize () {
        // Ensures that the global VBox does not allow space for headerGrid when it is hidden
        headerGrid.managedProperty().bind(headerGrid.visibleProperty());
    }

    @FXML public void backAction () {
        showPage(previousPage);
    }

    @FXML public void logoutAction () {}

    public void showPage (Page page) {

        // Hide all pages
        for (Node ch : pagesContainer.getChildren()) {
            try {
                AnchorPane pane = (AnchorPane) ch;
                pane.setVisible(false);
            }
            catch (ClassCastException e) {}
        }

        // Show the selected page
        String title = "";
        previousPage = null;
        boolean showHeader = true;
        boolean showUserData = true;
        switch (page) {
            case HOME:
                homePage.setVisible(true);
                title = "Bienvenue sur Pagelib";
                showHeader = false;
                break;

            case LOGIN:
                loginPage.setVisible(true);
                title = "Connexion";
                showHeader = false;
                break;

            case SOURCE:
                sourcePage.setVisible(true);
                title = "Où se trouve votre document ?";
                break;

            case USB_DOCUMENT:
                usbDocumentPage.setVisible(true);
                previousPage = Page.SOURCE;
                title = "Documents sur clé USB";
                break;

            case CLOUD_DOCUMENT:
                cloudDocumentPage.setVisible(true);
                previousPage = Page.SOURCE;
                title = "Documents stockés sur pagelib.fr";
                break;

            case JOB_SETTINGS:
                jobSettingsPage.setVisible(true);
                if (currentPage == Page.USB_DOCUMENT || currentPage == Page.CLOUD_DOCUMENT)
                    previousPage = currentPage;
                title = "Paramètres d'impression";
                break;

            case CART:
                cartPage.setVisible(true);
                title = "Panier";
                break;

            case PRINTING:
                printingPage.setVisible(true);
                title = "Impression en cours ...";
                showHeader = false;
                break;
        }

        headerGrid.setVisible(showHeader);
        backButton.setVisible(previousPage != null);
        pageTitleLabel.setText(title);
        userNameLabel.setVisible(showUserData);
        logoutButton.setVisible(showUserData);

        currentPage = page;
    }
}
