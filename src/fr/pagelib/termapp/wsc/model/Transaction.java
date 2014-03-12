package fr.pagelib.termapp.wsc.model;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public abstract class Transaction {
    String id;
    String userId;
    double amount;
    String currency;
    Date datetime;

    public JsonObjectBuilder getJsonBuilder(){
        String getUserId = getUserId();
        String getCurrency = getCurrency();
        double getAmount = getAmount();
        return Json.createObjectBuilder()
                .add("user_id", getUserId)
                .add("currency", getCurrency)
                .add("amount", getAmount);
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }
}
