package fr.pagelib.termapp.wsc;

import java.io.IOException;
import java.io.StringReader;

import org.apache.http.client.HttpResponseException;
import org.apache.http.client.fluent.*;
import org.apache.http.entity.ContentType;

import javax.json.*;

import fr.pagelib.termapp.wsc.exc.LoginException;

public class IAM {

    public IAM () {
    }

    public Session login (String username, String password) throws LoginException {
        try {
            // Compose request
            String url = Configuration.getConfig().getIamEndpoint() + "/v1/login";
            JsonObject model = Json.createObjectBuilder()
                    .add("login", username)
                    .add("password_hash", password)
                    .build();

            String rv = Request.Post(url)
                    .bodyString(model.toString(), ContentType.APPLICATION_JSON)
                    .execute().returnContent().asString();

            JsonStructure rvJson = Json.createReader(new StringReader(rv)).read();
            JsonObject root = (JsonObject) rvJson;
            return new Session(root.getString("session_id"), root.getString("user_id"));
        }
        catch (HttpResponseException e) {
            if (e.getStatusCode() == 404) {
                throw new LoginException();
            }
            else {
                System.out.println(String.format("Login / Unexpected status code %d (message : %s)",
                        e.getStatusCode(),
                        e.getMessage()));
                return null;
            }
        }
        catch (IOException e) {
            System.out.println("Login / IOException: " + e.getMessage());
            return null;
        }
    }

    public void logout (Session session) {
        try {
            String url = Configuration.getConfig().getIamEndpoint() + String.format("/v1/sessions/%s/logout", session.getSessionID());
            Request.Post(url).execute();
        }
        catch (HttpResponseException e) {
            System.out.println("Unable to properly log out.");
        }
        catch (IOException e) {
            System.out.println("Logout / IOException: " + e.getMessage());
        }
    }
}
