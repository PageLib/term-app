package fr.pagelib.termapp;

import fr.pagelib.termapp.wsc.exc.LoginException;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import fr.pagelib.termapp.wsc.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
        Configuration wsConfig = new Configuration("http://localhost:5001", "", "", "");
        IAM iam = new IAM(wsConfig);

        try {
            Session session = iam.login(username, hash);
            System.out.println(String.format("Session opened (ID=%s)", session.getSessionID()));
            mainController.showPage(MainController.Page.SOURCE);
        }
        catch (LoginException e) {
            System.out.println("Login exception");
        }
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
