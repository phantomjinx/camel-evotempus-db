package io.hawt.service;

import java.util.List;

import org.bson.Document;
import org.bson.json.JsonWriterSettings;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

@Component
public class ServiceBean {
    ObjectMapper mapper = new ObjectMapper();
    JsonWriterSettings settings = JsonWriterSettings.builder().indent(true).build();

    private String prettyString(List<Document> docs) throws JsonMappingException, JsonProcessingException {
        ArrayNode array = mapper.createArrayNode();

        for (Document interval : docs) {
            array.add(mapper.readTree(interval.toJson(settings)));
        }

        return array.toPrettyString();
    }

    public String intervals(List<Document> intervals) throws JsonMappingException, JsonProcessingException {
        return prettyString(intervals);
    }

    public String subjects(List<Document> subjects) throws JsonMappingException, JsonProcessingException {
        return prettyString(subjects);
    }
}
