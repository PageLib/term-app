package fr.pagelib.termapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private MainController mainController;

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
        AnchorPane root = (AnchorPane) loader.load();
        primaryStage.setTitle("PageLib terminal app");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();

        this.mainController = loader.getController();

        // Load and set up pages
        addPage(this.mainController.homePage, MainController.Page.HOME, "home.fxml");
        addPage(this.mainController.loginPage, MainController.Page.LOGIN, "login.fxml");
        addPage(this.mainController.sourcePage, MainController.Page.SOURCE, "source.fxml");
        addPage(this.mainController.cloudDocumentPage, MainController.Page.CLOUD_DOCUMENT, "cloudDocument.fxml");

        this.mainController.showPage(MainController.Page.HOME);
    }

    private PageController addPage(AnchorPane containerPage, MainController.Page page, String fxmlFileName)
            throws IOException
    {
        // Load the page's AnchorPane and add it to the root window
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFileName));
        AnchorPane pane = (AnchorPane) loader.load();
        containerPage.getChildren().add(pane);

        // Load the page's controller and bind it with the MainController
        PageController controller = loader.getController();
        controller.setMainController(this.mainController);
        this.mainController.setPageController(page, controller);

        return controller;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
