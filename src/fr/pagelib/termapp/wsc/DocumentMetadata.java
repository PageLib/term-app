package fr.pagelib.termapp.wsc;

import javafx.beans.property.SimpleStringProperty;

public class DocumentMetadata {

    private String name;
    private String date;


    private String id;

    public DocumentMetadata(String name, String date) {
        this.name = name;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}