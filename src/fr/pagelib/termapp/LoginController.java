package fr.pagelib.termapp;

import fr.pagelib.termapp.wsc.exc.LoginException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import fr.pagelib.termapp.wsc.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginController extends PageController {

    @FXML TextField usernameField;
    @FXML TextField passwordField;
    @FXML Label errorLabel;

    public void reset () {
        usernameField.setText("");
        passwordField.setText("");
        errorLabel.setVisible(false);
    }

    @FXML
    public void backAction () {
        mainController.showPage(MainController.Page.HOME);
    }

    @FXML
    public void loginAction () {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String hash = "";

        // Hash the password
        try{
            MessageDigest md = MessageDigest.getInstance("SHA1");
            md.update(password.getBytes());
            hash = bytesToHex(md.digest());
        }
        catch(NoSuchAlgorithmException e){
            System.out.println("Exception: "+e);
        }

        // TODO: store WS configuration and session in MainController
        IAM iam = new IAM(mainController.getWsConfig());

        try {
            Session session = iam.login(username, hash);
            System.out.println(String.format("Session opened (ID=%s)", session.getSessionID()));

            // TODO: retrieve the user's real name
            mainController.setCurrentUserName(username);
            mainController.setCurrentSession(session);

            mainController.showPage(MainController.Page.SOURCE);
        }
        catch (LoginException e) {
            errorLabel.setVisible(true);
            passwordField.setText("");
            System.out.println("Login exception");
        }
    }

    @FXML
    public void hideErrorLabel() {
        errorLabel.setVisible(false);
    }

    public static String bytesToHex(byte[] b) {
        char hexDigit[] = {'0', '1', '2', '3', '4', '5', '6', '7',
                '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        StringBuilder buf = new StringBuilder();
        for (byte aB : b) {
            buf.append(hexDigit[(aB >> 4) & 0x0f]);
            buf.append(hexDigit[aB & 0x0f]);
        }
        return buf.toString();
    }
}
