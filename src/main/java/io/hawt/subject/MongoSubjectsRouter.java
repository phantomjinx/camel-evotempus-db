package io.hawt.subject;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import io.hawt.Constants;
import io.hawt.Utils;
import io.hawt.processor.JsonProcessor;
import io.hawt.processor.FilenameProcessor;

@Component
public class MongoSubjectsRouter extends RouteBuilder implements Constants {

    
    @Override
    public void configure() {
        Utils.enableStatsAndInflightBrowse(getContext());

        from("timer://start?repeatCount=1")
            .to("mongodb:evoTempusBean?database=evotempus&collection=subjects&operation=findAll")
            .group("io.hawt.subject")
            .split(body())
            .process(new JsonProcessor())
            .log("Subject Json: ${body}")
            .delay(250) // artificial slow down
            .process(new FilenameProcessor(SUBJECTS))
            .to("file://" + RAW_DEST_DIR)
            .to("direct:enhanceSubject");
    }

}
