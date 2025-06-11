package io.hawt;

import org.apache.camel.CamelContext;
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
public class EvotempusSpringBootApplication {

    @Autowired
    private MetricRegistry metricRegistry;

    /**
     * A main method to start this application.
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        String dbHost = System.getProperty("evotempus.db.host","");
        if (dbHost.length() > 0) {
            System.out.println("Info: evotempus.db.host property was specified as " + dbHost);
        } else {
            System.out.println("Info: no evotempus.db.host property was specified. Default will be used.");
        }

        String dbUser = System.getProperty("evotempus.db.user","");
        if (dbUser.length() > 0) {
            System.out.println("Info: evotempus.db.user property was specified as " + dbUser);
        } else {
            System.out.println("Info: no evotempus.db.user property was specified. Default will be used.");
        }

        String dbPasswd = System.getProperty("evotempus.db.passwd","");
        if (dbPasswd.length() > 0) {
            System.out.println("Info: evotempus.db.passwd property was specified.");
        } else {
            System.out.println("Info: no evotempus.db.passwd property was specified. Default will be used.");
        }

        String destDir = System.getProperty("evotempus.dest.dir","");
        if (destDir.length() > 0) {
            System.out.println("Info: evotempus.dest.dir property was specified as " + destDir);
        } else {
            System.out.println("Info: no evotempus.dest.dir property was specified.");
            System.exit(1);
        }

        SpringApplication.run(EvotempusSpringBootApplication.class, args);
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
                Utils.enableStatsAndInflightBrowse(context);
            }

            @Override
            public void afterApplicationStart(CamelContext camelContext) {
                // TODO Auto-generated method stub

            }
        };
    }
}
