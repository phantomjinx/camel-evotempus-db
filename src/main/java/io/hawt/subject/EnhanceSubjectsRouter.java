package io.hawt.subject;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Comparator;

import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.bson.Document;
import org.springframework.stereotype.Component;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import io.hawt.Constants;
import io.hawt.Utils;
import io.hawt.processor.FilenameProcessor;
import io.hawt.processor.IntervalQueryProcessor;

@Component
public class EnhanceSubjectsRouter extends RouteBuilder implements Constants {

    private class DocComparator implements Comparator<Document> {

        @Override
        public int compare(Document d1, Document d2) {
            String d1Kind = d1.getString("kind");
            int kind1Idx = Interval.interval(d1Kind.toUpperCase()).ordinal();
            BigDecimal d1From = new BigDecimal(d1.get("from").toString());

            String d2Kind = d2.getString("kind");
            int kind2Idx = Interval.interval(d2Kind.toUpperCase()).ordinal();
            BigDecimal d2From = new BigDecimal(d2.get("from").toString());

            if (kind1Idx < kind2Idx)
                return -1;
            else if (kind1Idx > kind2Idx)
                return 1;

            // kind is the same
            return d1From.compareTo(d2From);
        }
    }

    private class EnhanceAggregationStrategy implements AggregationStrategy {

        DocComparator comparator = new DocComparator();

        public Exchange aggregate(Exchange subjectExchange, Exchange intervalsExchange) {
            if (subjectExchange == null) {
                return subjectExchange;
            }

            JsonObject subject = Utils.convertBody(subjectExchange.getIn());

            Document[] intervalDocs = intervalsExchange.getIn().getBody(Document[].class);
            if (intervalDocs == null) {
                throw new IllegalStateException("Intervals is empty");
            }

            Arrays.sort(intervalDocs, comparator);
            
            JsonArray livedDuring = new JsonArray();
            for (Document intervalDoc : intervalDocs) {
                String name = intervalDoc.get("name").toString();
                if (GEOLOGICAL_TIMESCALE.equals(name))
                    continue;
                String kind = intervalDoc.get("kind").toString();
                JsonObject interval = new JsonObject();
                interval.addProperty("name", name);
                interval.addProperty("kind", kind);

                livedDuring.add(interval);
            }

            subject.add("livedDuring", livedDuring);

            subjectExchange.getIn().setBody(subject);
            return subjectExchange;
        }
    }

    @Override
    public void configure() {
        Utils.enableStatsAndInflightBrowse(getContext());

        from("direct:enhanceSubject")
            .process(new IntervalQueryProcessor())
            .enrich("mongodb:evoTempusBean?database=evotempus&collection=intervals&operation=findAll", new EnhanceAggregationStrategy())
            .log("Subject Json: ${body}")
            .process(new FilenameProcessor(SUBJECTS))
            .to("file://" + ENHANCED_DEST_DIR)
            .group("io.hawt.subject");
    }

}
