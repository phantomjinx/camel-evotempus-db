package io.hawt.processor;

import java.io.File;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import io.hawt.Constants;
import io.hawt.Utils;

public class HTMLFilenameProcessor implements Processor, Constants {

    private String baseDir;

    public HTMLFilenameProcessor(String baseDir) {
        this.baseDir = baseDir;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        JsonObject jsonObject = Utils.convertBody(exchange.getIn());
        String name = jsonObject.get("name").getAsString();
        String kind = jsonObject.get("kind").getAsString();

        jsonObject.remove("_id");
        jsonObject.remove("tags");
        jsonObject.remove("version");

        String filename = baseDir + File.separator + kind + File.separator + name + ".html";
        exchange.getMessage().setHeader(Exchange.FILE_NAME, filename);

        StringBuilder builder = new StringBuilder();
        builder.append("<HTML>");
        builder.append("<p><ul>");
        for(Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            builder.append("<li>");
            builder.append(entry.getKey() + ": " + entry.getValue());
            builder.append("</li>");
        }
        builder.append("</p></ul>");
        builder.append("</HTML>");

        exchange.getMessage().setBody(builder.toString());
    }
}
