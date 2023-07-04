package io.hawt.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.bson.Document;
import org.bson.json.JsonWriterSettings;

public class JsonProcessor implements Processor {
    JsonWriterSettings settings = JsonWriterSettings.builder().indent(true).build();

    @Override
    public void process(Exchange exchange) throws Exception {
        Document doc = exchange.getIn().getBody(Document.class);
        exchange.getMessage().setBody(doc.toJson(settings));
    }
}
