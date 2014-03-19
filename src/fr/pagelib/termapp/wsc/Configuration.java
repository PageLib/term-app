package fr.pagelib.termapp.wsc;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonStructure;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Configuration {

    private String iamEndpoint;
    private String invoicingEndpoint;
    private String docsEndpoint;
    private String settingsEndpoint;
    private String pdfPath;
    private String usbRoot;
    private String printerNameRegex;
    private double priceColor;

    private double priceGreyLevel;

    private static Configuration config;

    public static Configuration getConfig(){
        return config;
    }

    public static void setConfig(String path) throws IOException {
        config = Configuration.buildFromFile(path);
    }

    private static Configuration buildFromFile(String configPath) throws IOException {

        // Compute the absolute path corresponding to configPath (relative to the current working directory)
        String cwdPath = new File(".").getAbsolutePath();
        if (cwdPath.length() > 1) cwdPath = cwdPath.substring(0, cwdPath.length() - 1);
        String absolutePath = cwdPath + configPath;

        // Parse JSON and build the Configuration
        JsonStructure rvJson = Json.createReader(new FileReader(absolutePath)).read();
        JsonObject root = (JsonObject) rvJson;

        Configuration config = new Configuration();

        config.iamEndpoint = root.getString("iamEndpoint");
        config.invoicingEndpoint = root.getString("invoicingEndpoint");
        config.docsEndpoint = root.getString("docsEndpoint");
        config.settingsEndpoint = root.getString("settingsEndpoint");
        config.pdfPath = root.getString("pdfPath");
        config.usbRoot = root.getString("usbRoot");
        config.printerNameRegex = root.getString("printerNameRegex");
        config.priceColor = root.getJsonNumber("priceColor").doubleValue();
        config.priceGreyLevel = root.getJsonNumber("priceGreyLevel").doubleValue();

        return config;
    }

    private Configuration() {}

    public static String getHost(String url) {
        // Q: Je me demande ce que c'est l'host dans preprod.pagelib.fr : pagelib ou preprod.pagelib
        Pattern hostPattern = Pattern.compile("^[^.]+[/.](.+)[.:][^.]+$");
        Matcher m = hostPattern.matcher(url);
        m.find();
        return m.group(1);
    }
    public static int getPort(String url) {
        Pattern portPattern = Pattern.compile(":([0-9]+)$");
        Matcher m = portPattern.matcher(url);
        m.find();
        try{
            return Integer.valueOf(m.group(1));
        }
        catch(IllegalStateException e ) {
            e.printStackTrace();
        }
        return 80;
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

    public String getUsbRoot() {
        return usbRoot;
    }

    public String getPrinterNameRegex() {
        return printerNameRegex;
    }


    public double getPriceColor() {
        return priceColor;
    }

    public double getPriceGreyLevel() {
        return priceGreyLevel;
    }
}
