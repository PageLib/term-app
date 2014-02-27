package fr.pagelib.termapp.wsc;

import javafx.beans.property.SimpleStringProperty;

public class DocumentMetadata {

    private String name;
    private String date;

    public DocumentMetadata(String name, String date) {
        this.name = name;
        this.date = date;
    }
}
