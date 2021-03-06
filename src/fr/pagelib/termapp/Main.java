package fr.pagelib.termapp;

import fr.pagelib.termapp.wsc.Configuration;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Main extends Application {

    private MainController mainController;

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        // Load configuration
        try {
            String configPath = System.getenv("PAGELIB_TERMAPP_CONFIG");
            if (configPath == null) configPath = "config.json";
            System.out.println("Loading configuration from file " + configPath);
            Configuration.setConfig(configPath);
        }
        catch (FileNotFoundException e) {
            System.err.println("Unable to find configuration file.");
            Platform.exit();
        }
        catch (IOException e) {
            System.err.println("Unexpected IOException while reading configuration file: " + e);
            e.printStackTrace();
            Platform.exit();
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("views/main.fxml"));
        AnchorPane root = (AnchorPane) loader.load();
        primaryStage.setTitle("PageLib terminal app");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();

        this.mainController = loader.getController();

        // Load and set up pages
        addPage(this.mainController.homePage, MainController.Page.HOME, "views/home.fxml");
        addPage(this.mainController.loginPage, MainController.Page.LOGIN, "views/login.fxml");
        addPage(this.mainController.sourcePage, MainController.Page.SOURCE, "views/source.fxml");
        addPage(this.mainController.cloudDocumentPage, MainController.Page.CLOUD_DOCUMENT, "views/cloudDocument.fxml");
        addPage(this.mainController.usbDocumentPage, MainController.Page.USB_DOCUMENT, "views/usbDocument.fxml");
        addPage(this.mainController.jobSettingsPage, MainController.Page.JOB_SETTINGS, "views/jobSettings.fxml");
        addPage(this.mainController.cartPage, MainController.Page.CART, "views/cart.fxml");
        addPage(this.mainController.printingPage, MainController.Page.PRINTING, "views/printing.fxml");

        // Set up the printer (will throw PrinterNotFoundException if no match)
        this.mainController.printingController.findPrinter(Configuration.getConfig().getPrinterNameRegex());

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
