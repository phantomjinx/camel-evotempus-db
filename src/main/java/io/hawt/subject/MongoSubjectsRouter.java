package io.hawt.subject;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import io.hawt.Constants;
import io.hawt.processor.FilenameProcessor;
import io.hawt.processor.JsonProcessor;

@Component
public class MongoSubjectsRouter extends RouteBuilder implements Constants {

    
    @Override
    public void configure() {
        from("timer://start?repeatCount=1")
            .group("io.hawt.subject")
            .routeId("subjectsToFiles")
            .to("mongodb:evoTempusBean?database=evotempus&collection=subjects&operation=findAll")
            .split(body())
            .process(new JsonProcessor())
            .log("Subject Json: ${body}")
            .delay(250) // artificial slow down
            .process(new FilenameProcessor(SUBJECTS))
            .to("file://" + RAW_DEST_DIR)
            .to("direct:enhanceSubject");
        
        /**
         *  Adds rest route for subjects
         *  Uses 'findAll' operation and outputs as List<Document>
         *  subjects method in @ServiceBean is then responsible for
         *  converting list into json array (pretty printing!)
         */
        from("direct:getSubjects")
            .group("io.hawt.subject")
            .routeId("subjectsToRest")
            .to("mongodb:evoTempusBean?database=evotempus&collection=subjects&operation=findAll")
            .log("Subject Json: ${body}")
            .to("bean:serviceBean?method=subjects(${body})");


        rest("/subjects")
            .get()
            .routeId("restSubjects")
            .produces("application/json")
            .to("direct:getSubjects");
    }

}
