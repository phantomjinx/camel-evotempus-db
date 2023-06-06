package io.hawt.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.bson.Document;

public class JsonProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        Document doc = exchange.getIn().getBody(Document.class);
        exchange.getMessage().setBody(doc.toJson());
    }
}
