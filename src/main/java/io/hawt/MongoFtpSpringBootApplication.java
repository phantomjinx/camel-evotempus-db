package io.hawt;

import org.apache.camel.CamelContext;
import org.apache.camel.component.metrics.MetricsComponent;
import org.apache.camel.component.metrics.routepolicy.MetricsRoutePolicyFactory;
import org.apache.camel.spring.boot.CamelContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import com.codahale.metrics.MetricRegistry;

@SpringBootApplication
@ComponentScan(basePackages="io.hawt")
public class MongoFtpSpringBootApplication {

    @Autowired
    private MetricRegistry metricRegistry;

    /**
     * A main method to start this application.
     * @throws Exception 
     */
    public static void main(String[] args) throws Exception {
        SpringApplication.run(MongoFtpSpringBootApplication.class, args);
    }

    @Bean
    CamelContextConfiguration contextConfiguration() {
        return new CamelContextConfiguration() {

            @Override
            public void beforeApplicationStart(CamelContext context) {
                System.out.println("Configuring camel metrics on all routes");
                MetricsRoutePolicyFactory fac = new MetricsRoutePolicyFactory();
                fac.setMetricsRegistry(metricRegistry);
                context.addRoutePolicyFactory(fac);
            }

            @Override
            public void afterApplicationStart(CamelContext camelContext) {
                // TODO Auto-generated method stub
                
            }
        };
    }
}
