package fr.pagelib.termapp;

public class PrinterNotFoundException extends Exception {

    String pattern;

    public PrinterNotFoundException(String pattern) {
        super();
        this.pattern = pattern;
    }

    public String toString() {
        return String.format("No printer matching '%s' found.", pattern);
    }
}
