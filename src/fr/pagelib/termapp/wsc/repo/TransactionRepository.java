package fr.pagelib.termapp.wsc.repo;


import fr.pagelib.termapp.wsc.Configuration;
import fr.pagelib.termapp.wsc.Session;
import fr.pagelib.termapp.wsc.exc.InvoicingBalanceException;
import fr.pagelib.termapp.wsc.exc.InvoicingException;
import fr.pagelib.termapp.wsc.model.PrintingTransaction;
import fr.pagelib.termapp.wsc.model.Transaction;
import org.apache.http.HttpHost;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonStructure;
import java.io.IOException;
import java.io.StringReader;
import java.util.Date;

public class TransactionRepository extends Repository{


    public TransactionRepository(Configuration configuration, Session session) {
        setConfiguration(configuration);
        setSession(session);
    }

    public Transaction post(Transaction transaction) throws InvoicingException {
        try {
            String url = configuration.getInvoicingEndpoint() + "/v1/transactions";
            JsonObject jsonPost = transaction.getJsonBuilder().build();

            Executor executor = getExecutor(Configuration.getConfig().getInvoicingEndpoint());

            String rv = executor.execute(Request.Post(url)
                    .bodyString(jsonPost.toString(), ContentType.APPLICATION_JSON))
                    .returnContent().asString();
            JsonStructure rvJson = Json.createReader(new StringReader(rv)).read();
            JsonObject root = (JsonObject) rvJson;
            return TransactionRepository.buildTransaction(root);
        }
        catch (HttpResponseException e){
            if (e.getStatusCode() == 404){
                throw new InvoicingException();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Transaction buildTransaction(JsonObject json) throws InvoicingException {
        if(json.keySet().contains("error")) {
            if("insufficient_balance".equals(json.getString("error"))) {
                throw new InvoicingBalanceException();
            }
        }

        String transactionType = json.getString("transaction_type");
        String datetimeStr = json.getString("date_time");
        Date datetime= javax.xml.bind.DatatypeConverter.parseDateTime(datetimeStr).getTime();
        //TODO corriger l'heure (une heure de décalage)
        if("printing".equals(transactionType)){
            PrintingTransaction tr = new PrintingTransaction();
            tr.setId(json.getString("id"));
            tr.setUserId(json.getString("user_id"));
            tr.setAmount(Float.parseFloat(json.getString("amount")));
            tr.setDatetime(datetime);
            tr.setCurrency(json.getString("currency"));
            tr.setPageColor(json.getInt("pages_color"));
            tr.setPageGreyLevel(json.getInt("pages_grey_level"));
            tr.setCopies(json.getInt("copies"));

            return tr;
        }
        throw new InvoicingException();
    }


}
