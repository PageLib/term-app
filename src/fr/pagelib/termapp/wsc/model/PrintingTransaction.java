package fr.pagelib.termapp.wsc.model;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import java.util.Date;

public class PrintingTransaction extends Transaction{
    int pageColor;
    int pageGreyLevel;
    int copies;

    public PrintingTransaction(String id, String userId, float amount, String currency, Date datetime, int pageColor, int pageGreyLevel, int copies) {
        this.id = id;
        this.userId = userId;
        this.amount = amount;
        this.currency = currency;
        this.datetime = datetime;
        this.pageColor = pageColor;
        this.pageGreyLevel = pageGreyLevel;
        this.copies = copies;
    }

    public PrintingTransaction(String userId, double amount, String currency, int pageColor, int pageGreyLevel, int copies) {
        this.userId = userId;
        this.amount = amount;
        this.currency = currency;
        this.pageColor = pageColor;
        this.pageGreyLevel = pageGreyLevel;
        this.copies = copies;
    }

    public PrintingTransaction() {
    }

    public JsonObjectBuilder getJsonBuilder(){
        int getPageColor = getPageColor();
        int getPageGreyLevel = getPageGreyLevel();
        int getCopies = getCopies();
        return super.getJsonBuilder()
                .add("pages_color", getPageColor)
                .add("transaction_type", "printing")
                .add("pages_grey_level", getPageGreyLevel)
                .add("copies", getCopies);
    }

    public int getPageColor() {
        return pageColor;
    }

    public void setPageColor(int pageColor) {
        this.pageColor = pageColor;
    }

    public int getPageGreyLevel() {
        return pageGreyLevel;
    }

    public void setPageGreyLevel(int pageGreyLevel) {
        this.pageGreyLevel = pageGreyLevel;
    }

    public int getCopies() {
        return copies;
    }

    public void setCopies(int copies) {
        this.copies = copies;
    }
}
