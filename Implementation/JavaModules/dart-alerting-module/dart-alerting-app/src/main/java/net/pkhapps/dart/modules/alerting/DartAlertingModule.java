package net.pkhapps.dart.modules.alerting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.integration.annotation.IntegrationComponentScan;

@SpringBootApplication
@IntegrationComponentScan
public class DartAlertingModule {

    public static void main(String[] args) {
        SpringApplication.run(DartAlertingModule.class, args);
    }
}
