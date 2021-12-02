package me.manaki.deepup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class DeepUpApplication {

    public static void main(String[] args) {
        SpringApplication.run(DeepUpApplication.class, args);
    }

}
