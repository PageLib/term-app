package fr.pagelib.termapp.wsc.repo;


import fr.pagelib.termapp.wsc.Configuration;
import fr.pagelib.termapp.wsc.Session;
import fr.pagelib.termapp.wsc.exc.InvoicingBalanceException;
import fr.pagelib.termapp.wsc.exc.InvoicingException;
import fr.pagelib.termapp.wsc.exc.NotFound;
import fr.pagelib.termapp.wsc.model.PrintingTransaction;
import fr.pagelib.termapp.wsc.model.Transaction;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonStructure;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.Date;

public class BalanceRepository extends Repository{


    public BalanceRepository(Configuration configuration, Session session) {
        setConfiguration(configuration);
        setSession(session);
    }

    public BigDecimal get(String userId) throws NotFound {
        try {
            String url = String.format("%s/v1/user/%s/balance", configuration.getInvoicingEndpoint(), userId);

            Executor executor = getExecutor(Configuration.getConfig().getInvoicingEndpoint());

            String rv = executor.execute(Request.Get(url)).returnContent().asString();
            JsonStructure rvJson = Json.createReader(new StringReader(rv)).read();
            JsonObject root = (JsonObject) rvJson;
            return root.getJsonNumber("balance").bigDecimalValue();
        }
        catch (HttpResponseException e){
            if (e.getStatusCode() == 404){
                throw new NotFound();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
