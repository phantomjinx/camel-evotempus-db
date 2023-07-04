package io.hawt.interval;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import io.hawt.Constants;
import io.hawt.processor.FilenameProcessor;
import io.hawt.processor.JsonProcessor;

@Component
public class MongoIntervalsRouter extends RouteBuilder implements Constants {

    @Override
    public void configure() {
        restConfiguration().component("servlet")
            .host("localhost").port(10020);

        from("timer://start?repeatCount=1")
            .group("io.hawt.interval")
            .routeId("intervalsToFiles")
            .to("mongodb:evoTempusBean?database=evotempus&collection=intervals&operation=findAll")
            .to("metrics:timer:simple.timer?action=start")
            .split(body())
            .process(new JsonProcessor())
            .log("Interval Json: ${body}")
            .process(new FilenameProcessor(INTERVALS))
            .to("metrics:timer:simple.timer?action=stop")
            .to("file://" + ENHANCED_DEST_DIR);

        /**
         *  Adds rest route for intervals
         *  Uses 'findAll' operation and outputs as List<Document>
         *  intervals method in @ServiceBean is then responsible for
         *  converting list into json array (pretty printing!)
         */
        from("direct:getIntervals")
            .group("io.hawt.interval")
            .routeId("intervalsToRest")
            .to("mongodb:evoTempusBean?database=evotempus&collection=intervals&operation=findAll&outputType=DocumentList")
            .log("Intervals Json: ${body}")
            .to("bean:serviceBean?method=intervals(${body})");

        rest("/intervals")
            .get()
            .routeId("restIntervals")
            .produces("application/json")
            .to("direct:getIntervals");
    }
}
