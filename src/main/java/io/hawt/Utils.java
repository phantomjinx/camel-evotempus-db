package io.hawt;

import org.apache.camel.CamelContext;
import org.apache.camel.ExtendedCamelContext;
import org.apache.camel.Message;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Utils {

    public static JsonObject convertBody(Message message) {
        String body = message.getBody(String.class);
        JsonElement json = JsonParser.parseString(body);
        if (!json.isJsonObject()) {
            throw new IllegalStateException("Body is not expected json object");
        }
        JsonObject jsonObject = json.getAsJsonObject();
        return jsonObject;
    }

    public static void enableStatsAndInflightBrowse(CamelContext context) {
        ExtendedCamelContext extendedCamelContext = context.adapt(ExtendedCamelContext.class);
        extendedCamelContext.getExchangeFactoryManager().setStatisticsEnabled(true);
        extendedCamelContext.getExchangeFactory().setStatisticsEnabled(true);
        extendedCamelContext.getProcessorExchangeFactory().setStatisticsEnabled(true);
        extendedCamelContext.getAsyncProcessorAwaitManager().getStatistics().setStatisticsEnabled(true);

        context.getInflightRepository().setInflightBrowseEnabled(true);
        context.setBacklogTracing(true);
    }
}
