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
        addPage(this.mainController.homePage, "home.fxml");
        addPage(this.mainController.loginPage, "login.fxml");
        addPage(this.mainController.sourcePage, "source.fxml");

        this.mainController.showPage(MainController.Page.HOME);
    }

    private PageController addPage(AnchorPane controllerPage, String fxmlFileName) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFileName));
        AnchorPane pane = (AnchorPane) loader.load();
        controllerPage.getChildren().add(pane);
        PageController controller = loader.getController();
        controller.setMainController(this.mainController);
        return controller;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
