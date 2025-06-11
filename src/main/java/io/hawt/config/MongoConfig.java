package io.hawt.config;

import org.apache.camel.component.metrics.MetricsComponent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

import com.codahale.metrics.MetricRegistry;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import io.hawt.Constants;

@Configuration
public class MongoConfig {

    @Value("${evotempus.db.host}")
    private String dbHost;

    @Value("${evotempus.db.user}")
    private String dbUser;

    @Value("${evotempus.db.passwd}")
    private String dbPasswd;

    @Bean("evoTempusBean")
    public MongoClient getMongoClient() {

        String connStr = "mongodb://";
        if (dbUser.length() > 0) {
          connStr = connStr + dbUser + ":" + dbPasswd + "@";
        }

        connStr = connStr + dbHost + ":27017/evotempus";

        if (dbUser.length() > 0) {
          connStr = connStr + "?authSource=admin";
        }

        ConnectionString conn = new ConnectionString(connStr);
        MongoClientSettings settings = MongoClientSettings
                .builder()
                .applyConnectionString(conn)
                .build();
        return MongoClients.create(settings);
    }

    @Bean(name = MetricsComponent.METRIC_REGISTRY_NAME)
    public MetricRegistry getMetricRegistry() {
        System.out.println("Getting new Metric Registry");
        MetricRegistry registry = new MetricRegistry();
        return registry;
    }
}
