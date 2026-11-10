package com.example.samplecrm;

import static org.assertj.core.api.Assertions.assertThat;

import javax.sql.DataSource;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Spec 006 AC-9: deals stored before stages were added get the stage open. Runs the migrations on a schema of its
 * own, dropped before and after, so the application's schema is not touched and a reused database cannot skip them.
 */
class DealStageMigrationTest extends ApiTestBase {

    private static final String SCHEMA = "stage_migration_test";

    @Autowired
    private DataSource dataSource;

    @BeforeEach
    @AfterEach
    void dropSchema() {
        jdbc.sql("DROP SCHEMA IF EXISTS " + SCHEMA + " CASCADE").update();
    }

    private Flyway flyway(String target) {
        var config = Flyway.configure().dataSource(dataSource).schemas(SCHEMA).locations("classpath:db/migration");
        if (target != null) {
            config.target(target);
        }
        return config.load();
    }

    @Test
    void existingDealsBecomeOpen() {
        flyway("3").migrate();
        jdbc.sql("INSERT INTO " + SCHEMA + ".deals (title, amount) VALUES ('Old deal', 100)").update();

        flyway(null).migrate();

        assertThat(jdbc.sql("SELECT stage FROM " + SCHEMA + ".deals WHERE title = 'Old deal'")
                .query(String.class).single()).isEqualTo("open");
    }
}
