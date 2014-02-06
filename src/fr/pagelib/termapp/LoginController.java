package fr.pagelib.termapp;

import fr.pagelib.termapp.wsc.exc.LoginException;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import fr.pagelib.termapp.wsc.*;

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
        String password = passwordField.getText();  // TODO: SHA-1 hash

        // TODO: store WS configuration and session in MainController
        Configuration wsConfig = new Configuration("http://localhost:5001", "", "", "");
        IAM iam = new IAM(wsConfig);

        try {
            Session session = iam.login(username, password);
            System.out.println(String.format("Session opened (ID=%s)", session.getSessionID()));
            mainController.showPage(MainController.Page.SOURCE);
        }
        catch (LoginException e) {
            System.out.println("Login exception");
        }
    }
}
