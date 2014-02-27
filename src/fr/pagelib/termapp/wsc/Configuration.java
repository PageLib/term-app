package fr.pagelib.termapp.wsc;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonStructure;
import java.io.*;

public class Configuration {

    private String iamEndpoint;
    private String invoicingEndpoint;
    private String docsEndpoint;
    private String settingsEndpoint;

    public static Configuration buildFromFile(String configPath) throws IOException {

        // Compute the absolute path corresponding to configPath (relative to the current working directory)
        String cwdPath = new File(".").getAbsolutePath();
        if (cwdPath.length() > 1) cwdPath = cwdPath.substring(0, cwdPath.length() - 1);
        String absolutePath = cwdPath + configPath;

        // Parse JSON and build the Configuration
        JsonStructure rvJson = Json.createReader(new FileReader(absolutePath)).read();
        JsonObject root = (JsonObject) rvJson;

        return new Configuration(
                root.getString("iamEndpoint"),
                root.getString("invoicingEndpoint"),
                root.getString("docsEndpoint"),
                root.getString("settingsEndpoint"));
    }

    public Configuration (String iamEndpoint, String invoicingEndpoint, String docsEndpoint, String settingsEndpoint) {
        this.iamEndpoint = iamEndpoint;
        this.invoicingEndpoint = invoicingEndpoint;
        this.docsEndpoint = docsEndpoint;
        this.settingsEndpoint = settingsEndpoint;
    }

    public String getIamEndpoint() {
        return iamEndpoint;
    }

    public String getInvoicingEndpoint() {
        return invoicingEndpoint;
    }

    public String getDocsEndpoint() {
        return docsEndpoint;
    }

    public String getSettingsEndpoint() {
        return settingsEndpoint;
    }
}
