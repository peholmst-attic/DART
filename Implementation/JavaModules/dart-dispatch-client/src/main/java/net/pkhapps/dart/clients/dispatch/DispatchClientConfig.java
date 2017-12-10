package net.pkhapps.dart.clients.dispatch;

import net.pkhapps.nlsmap.api.query.MunicipalityQuery;
import net.pkhapps.nlsmap.jdbc.ConnectionSupplier;
import net.pkhapps.nlsmap.jdbc.SimpleConnectionSupplier;
import net.pkhapps.nlsmap.jdbc.query.JdbcMunicipalityQuery;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Configuration
@EnableAutoConfiguration
@ComponentScan
class DispatchClientConfig {

    @Bean(destroyMethod = "close", initMethod = "open")
    SimpleConnectionSupplier connectionSupplier() {
        SimpleConnectionSupplier connectionSupplier = new SimpleConnectionSupplier();
        connectionSupplier
                .setDatabaseFile(new File(System.getProperty("user.home")), "nlsmap"); // TODO read from somewhere else
        return connectionSupplier;
    }

    @Bean
    MunicipalityQuery municipalityQuery(ConnectionSupplier connectionSupplier) {
        return new JdbcMunicipalityQuery(connectionSupplier);
    }
}
