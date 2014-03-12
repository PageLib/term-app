package fr.pagelib.termapp.wsc.repo;


import fr.pagelib.termapp.wsc.Configuration;
import fr.pagelib.termapp.wsc.Session;

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



}
