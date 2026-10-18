package com.example.samplecrm;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.web.servlet.client.RestTestClient;

/** Base for API tests: the whole application on a random port, against PostgreSQL in a container. */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureRestTestClient
@Import(TestcontainersConfiguration.class)
public abstract class ApiTestBase {

    @Autowired
    protected RestTestClient client;

    @Autowired
    protected JdbcClient jdbc;

    @BeforeEach
    void cleanTables() {
        jdbc.sql("""
                DO $$
                DECLARE t text;
                BEGIN
                  FOR t IN SELECT tablename FROM pg_tables
                           WHERE schemaname = 'public' AND tablename <> 'flyway_schema_history'
                  LOOP
                    EXECUTE format('TRUNCATE TABLE %I RESTART IDENTITY CASCADE', t);
                  END LOOP;
                END $$
                """).update();
    }

    protected long count(String table) {
        return jdbc.sql("SELECT count(*) FROM " + table).query(Long.class).single();
    }
}
