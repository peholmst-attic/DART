package net.pkhapps.dart.modules.dispatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.integration.annotation.IntegrationComponentScan;

@SpringBootApplication
@IntegrationComponentScan
public class DartDispatchModule {

    public static void main(String[] args) {
        SpringApplication.run(DartDispatchModule.class, args);
    }
}
