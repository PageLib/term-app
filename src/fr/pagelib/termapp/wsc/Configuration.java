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

    public static Configuration ConfigurationBuilder(String configPath) throws IOException {
        String fileContent = "";
        String path = new File(".").getAbsolutePath();
        if (path.length() > 1) {
            path = path.substring(0, path.length()-1);
        }
        String absolutePath = path  + configPath;
        System.out.println(absolutePath);
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
        JsonStructure rvJson = Json.createReader(new StringReader(fileContent)).read();
        JsonObject root = (JsonObject) rvJson;
        String iamEndpoint = root.getString("iamEndpoint");
        String invoicingEndpoint = root.getString("invoicingEndpoint");
        String docsEndpoint = root.getString("docsEndpoint");
        String settingsEndpoint = root.getString("settingsEndpoint");
        return new Configuration(iamEndpoint, invoicingEndpoint, docsEndpoint, settingsEndpoint);
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
