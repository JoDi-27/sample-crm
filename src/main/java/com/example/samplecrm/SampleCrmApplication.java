package com.example.samplecrm;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "sample-crm", version = "0.1.0",
        description = "Companies, contacts, and deals for a small sales team."))
public class SampleCrmApplication {

    public static void main(String[] args) {
        SpringApplication.run(SampleCrmApplication.class, args);
    }
}
