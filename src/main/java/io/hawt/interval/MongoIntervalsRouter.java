package io.hawt.interval;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import io.hawt.Constants;
import io.hawt.Utils;
import io.hawt.processor.FilenameProcessor;
import io.hawt.processor.JsonProcessor;

@Component
public class MongoIntervalsRouter extends RouteBuilder implements Constants {

    @Override
    public void configure() {
        Utils.enableStatsAndInflightBrowse(getContext());

        from("timer://start?repeatCount=1")
            .to("mongodb:evoTempusBean?database=evotempus&collection=intervals&operation=findAll")
            .group("io.hawt.interval")
            .to("metrics:timer:simple.timer?action=start")
            .split(body())
            .process(new JsonProcessor())
            .log("Interval Json: ${body}")
            .process(new FilenameProcessor(INTERVALS))
            .to("metrics:timer:simple.timer?action=stop")
            .to("file://" + ENHANCED_DEST_DIR);
    }
}
