package io.hawt.processor;

import java.io.File;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import io.hawt.Constants;
import io.hawt.Utils;

public class FilenameProcessor implements Processor, Constants {

    private Gson gson;
    private String baseDir;

    public FilenameProcessor(String baseDir) {
        this.baseDir = baseDir;
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        JsonObject jsonObject = Utils.convertBody(exchange.getIn());
        String name = jsonObject.get("name").getAsString();
        String kind = jsonObject.get("kind").getAsString();

        jsonObject.remove("_id");
        jsonObject.remove("tags");
        jsonObject.remove("version");

        String filename = baseDir + File.separator + kind + File.separator + name + ".json";
        exchange.getMessage().setHeader(Exchange.FILE_NAME, filename);
        exchange.getMessage().setBody(gson.toJson(jsonObject));
    }
}
