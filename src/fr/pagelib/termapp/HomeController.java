package fr.pagelib.termapp;

import javafx.fxml.FXML;

public class HomeController extends PageController {

    @FXML
    public void loginAction () {
        mainController.showPage(MainController.Page.LOGIN);
    }

}
