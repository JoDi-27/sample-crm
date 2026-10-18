package com.example.samplecrm;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.springframework.test.web.servlet.client.RestTestClient.ResponseSpec;

/** Base for API tests: the whole application on a random port, against PostgreSQL in a container. */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureRestTestClient
@Import(TestcontainersConfiguration.class)
public abstract class ApiTestBase {

    private static final ParameterizedTypeReference<Map<String, Object>> MAP = new ParameterizedTypeReference<>() {};
    private static final ParameterizedTypeReference<List<Map<String, Object>>> LIST =
            new ParameterizedTypeReference<>() {};

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

    /** A JSON object from alternating keys and values; values may be null. */
    protected static Map<String, Object> body(Object... keysAndValues) {
        Map<String, Object> map = new LinkedHashMap<>();
        for (int i = 0; i < keysAndValues.length; i += 2) {
            map.put((String) keysAndValues[i], keysAndValues[i + 1]);
        }
        return map;
    }

    /** A copy of {@code base} with one key replaced, or removed when {@code value} is {@link #ABSENT}. */
    protected static Map<String, Object> with(Map<String, Object> base, String key, Object value) {
        Map<String, Object> copy = new LinkedHashMap<>(base);
        if (value == ABSENT) {
            copy.remove(key);
        } else {
            copy.put(key, value);
        }
        return copy;
    }

    protected static final Object ABSENT = new Object();

    protected ResponseSpec send(HttpMethod method, String uri, Object body) {
        var request = client.method(method).uri(uri);
        if (body != null) {
            request.contentType(MediaType.APPLICATION_JSON).body(body);
        }
        return request.exchange();
    }

    protected Map<String, Object> get(String uri) {
        return json(send(HttpMethod.GET, uri, null).expectStatus().isOk());
    }

    protected List<Map<String, Object>> getList(String uri) {
        return send(HttpMethod.GET, uri, null).expectStatus().isOk()
                .expectBody(LIST).returnResult().getResponseBody();
    }

    /** Creates a record and returns its identifier, checking the 201 answer and its Location header. */
    protected long create(String uri, Map<String, Object> body) {
        Map<String, Object> created = json(send(HttpMethod.POST, uri, body)
                .expectStatus().isCreated()
                .expectHeader().valueMatches("Location", ".*" + uri + "/\\d+$"));
        return id(created);
    }

    protected Map<String, Object> put(String uri, Map<String, Object> body) {
        return json(send(HttpMethod.PUT, uri, body).expectStatus().isOk());
    }

    protected void delete(String uri) {
        send(HttpMethod.DELETE, uri, null).expectStatus().isNoContent();
    }

    protected void assertNotFound(HttpMethod method, String uri, Object body) {
        send(method, uri, body).expectStatus().isNotFound()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON);
    }

    /** Asserts a 400 problem answer and returns the fields named in its errors, each with a reason. */
    protected List<String> badInputFields(ResponseSpec response) {
        Map<String, Object> problem = json(response.expectStatus().isBadRequest()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON));
        assertThat(problem).containsEntry("status", 400);
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> errors = (List<Map<String, Object>>) problem.get("errors");
        assertThat(errors).isNotEmpty().allSatisfy(error -> assertThat((String) error.get("message")).isNotBlank());
        return errors.stream().map(error -> (String) error.get("field")).toList();
    }

    protected Map<String, Object> json(ResponseSpec response) {
        return response.expectBody(MAP).returnResult().getResponseBody();
    }

    protected static long id(Map<String, Object> record) {
        return ((Number) record.get("id")).longValue();
    }

    protected long count(String table) {
        return jdbc.sql("SELECT count(*) FROM " + table).query(Long.class).single();
    }
}
