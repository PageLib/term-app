package fr.pagelib.termapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
        AnchorPane root = (AnchorPane) loader.load();
        primaryStage.setTitle("PageLib terminal app");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();

        MainController mainController = loader.getController();
        mainController.showPage(MainController.Page.SOURCE);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
