package fr.pagelib.termapp.wsc;

/**
 *
 */
public class Configuration {

    private String iamEndpoint;
    private String invoicingEndpoint;
    private String docsEndpoint;
    private String settingsEndpoint;

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
