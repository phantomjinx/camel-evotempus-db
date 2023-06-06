package io.hawt.processor;

import java.math.BigDecimal;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.mongodb.MongoDbConstants;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.client.model.Filters;

public class IntervalQueryProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        String body = exchange.getIn().getBody(String.class);
        JsonElement json = JsonParser.parseString(body);
        if (!json.isJsonObject()) {
            throw new IllegalStateException("Body is not expected json object");
        }
        JsonObject jsonObject = json.getAsJsonObject();
        String fromStr = jsonObject.get("from").getAsString();
        String toStr = jsonObject.get("to").getAsString();

        BigDecimal from = new BigDecimal(fromStr);
        BigDecimal to = new BigDecimal(toStr);

        /*
         * Sets the query header for the search for applicable intervals
         */
        exchange.getMessage()
            .setHeader(MongoDbConstants.CRITERIA,
                    Filters.and(
                            Filters.lte("from", to),
                            Filters.gte("to", from)
                    )
                );
    }

}
