package com.example.samplecrm;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class SmokeTest extends ApiTestBase {

    @Test
    void startsAgainstPostgres() {
        String version = jdbc.sql("SELECT version()").query(String.class).single();
        assertThat(version).startsWith("PostgreSQL 18");
    }
}
