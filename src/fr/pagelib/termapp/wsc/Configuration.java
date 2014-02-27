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
    private String pdfPath;

    public static Configuration Builder(String configPath) throws IOException {
        // Generate absolute path
        String path = new File(".").getAbsolutePath();
        if (path.length() > 1) {
            path = path.substring(0, path.length()-1);
        }
        String absolutePath = path  + configPath;
        String fileContent = "";

        // Read the json file
        BufferedReader br = new BufferedReader(new FileReader(absolutePath));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            fileContent = sb.toString();
        } finally {
            br.close();
        }

        //
        JsonStructure rvJson = Json.createReader(new StringReader(fileContent)).read();
        JsonObject root = (JsonObject) rvJson;

        // Fill the configuration
        Configuration configuration = new Configuration();
        configuration.iamEndpoint = root.getString("iamEndpoint");
        configuration.invoicingEndpoint = root.getString("invoicingEndpoint");
        configuration.docsEndpoint = root.getString("docsEndpoint");
        configuration.settingsEndpoint = root.getString("settingsEndpoint");
        configuration.pdfPath = root.getString("pdfPath");
        return configuration;
    }

    public Configuration () {
    }

   public Configuration (String iamEndpoint, String invoicingEndpoint, String docsEndpoint, String settingsEndpoint, String pdfPath) {
        this.iamEndpoint = iamEndpoint;
        this.invoicingEndpoint = invoicingEndpoint;
        this.docsEndpoint = docsEndpoint;
        this.settingsEndpoint = settingsEndpoint;
        this.pdfPath = pdfPath;
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

    public String getPdfPath() {
        return pdfPath;
    }

    public void setPdfPath(String pdfPath) {
        this.pdfPath = pdfPath;
    }
}
