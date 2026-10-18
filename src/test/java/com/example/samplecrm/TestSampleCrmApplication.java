package com.example.samplecrm;

import org.springframework.boot.SpringApplication;

/** Runs the application locally against a PostgreSQL container: {@code ./mvnw spring-boot:test-run}. */
public class TestSampleCrmApplication {

    public static void main(String[] args) {
        SpringApplication.from(SampleCrmApplication::main)
                .with(TestcontainersConfiguration.class)
                .run(args);
    }
}
