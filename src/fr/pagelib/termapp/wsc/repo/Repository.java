package fr.pagelib.termapp.wsc.repo;


import fr.pagelib.termapp.wsc.Configuration;
import fr.pagelib.termapp.wsc.Session;
import fr.pagelib.termapp.wsc.exc.InvoicingException;
import org.apache.http.HttpHost;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.fluent.Executor;

public abstract class Repository {

    Configuration configuration;
    Session session;

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }


    public  Executor getExecutor(String endPoint) {
        String hostName = Configuration.getHost(endPoint);
        int hostPort = Configuration.getPort(endPoint);

        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(getSession().getUserID(), getSession().getSessionID());
        HttpHost host = new HttpHost(hostName, hostPort);
        return Executor.newInstance().auth(host, credentials).authPreemptive(host);
    }
}
