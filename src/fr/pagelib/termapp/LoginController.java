package fr.pagelib.termapp;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class LoginController extends PageController {

    @FXML TextField usernameField;
    @FXML TextField passwordField;

    @FXML
    public void backAction () {
        mainController.showPage(MainController.Page.HOME);
    }

    @FXML
    public void loginAction () {
        String username = usernameField.getText();
        String password = passwordField.getText();

        System.out.println(String.format("Logging in with username=%s, password=%s", username, password));
    }

}
