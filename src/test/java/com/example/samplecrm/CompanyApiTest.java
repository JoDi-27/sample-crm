package com.example.samplecrm;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

class CompanyApiTest extends ApiTestBase {

    private static final Map<String, Object> ACME = body("name", "Acme", "website", "https://acme.example");

    @Test
    void createAndRead() { // AC-1
        long id = create("/companies", ACME);

        assertThat(get("/companies/" + id))
                .containsEntry("id", (int) id)
                .containsEntry("name", "Acme")
                .containsEntry("website", "https://acme.example");
    }

    @Test
    void createWithoutWebsite() { // R1: website is optional
        long id = create("/companies", body("name", "Globex"));

        assertThat(get("/companies/" + id)).containsEntry("name", "Globex").containsEntry("website", null);
    }

    @Test
    void update() { // AC-5
        long id = create("/companies", ACME);
        Map<String, Object> changed = body("name", "Acme Corp", "website", "https://acme-corp.example");

        assertThat(put("/companies/" + id, changed))
                .containsEntry("id", (int) id)
                .containsEntry("name", "Acme Corp")
                .containsEntry("website", "https://acme-corp.example");
        assertThat(get("/companies/" + id))
                .containsEntry("name", "Acme Corp")
                .containsEntry("website", "https://acme-corp.example");
    }

    @Test
    void delete() { // AC-6
        long id = create("/companies", ACME);

        delete("/companies/" + id);

        assertNotFound(HttpMethod.GET, "/companies/" + id, null);
    }

    @Test
    void unknownId() { // AC-7
        long existing = create("/companies", ACME);

        assertNotFound(HttpMethod.GET, "/companies/999", null);
        assertNotFound(HttpMethod.PUT, "/companies/999", body("name", "Other"));
        assertNotFound(HttpMethod.DELETE, "/companies/999", null);

        assertThat(count("companies")).isEqualTo(1);
        assertThat(get("/companies/" + existing)).containsEntry("name", "Acme");
    }

    @Test
    void listAll() { // AC-8
        create("/companies", body("name", "Acme"));
        create("/companies", body("name", "Globex"));
        create("/companies", body("name", "Initech"));

        assertThat(getList("/companies"))
                .extracting(company -> company.get("name"))
                .containsExactlyInAnyOrder("Acme", "Globex", "Initech");
    }

    static Stream<Arguments> badCompanies() {
        return Stream.of(
                Arguments.of(with(ACME, "name", ABSENT), "name"),
                Arguments.of(with(ACME, "name", "  "), "name"),
                Arguments.of(with(ACME, "name", "x".repeat(201)), "name"),
                Arguments.of(with(ACME, "website", "x".repeat(501)), "website"));
    }

    @ParameterizedTest
    @MethodSource("badCompanies")
    void badInputOnCreate(Map<String, Object> request, String field) { // AC-16
        assertThat(badInputFields(send(HttpMethod.POST, "/companies", request))).containsExactly(field);
        assertThat(count("companies")).isZero();
    }

    @ParameterizedTest
    @MethodSource("badCompanies")
    void badInputOnUpdate(Map<String, Object> request, String field) { // AC-16
        long id = create("/companies", ACME);

        assertThat(badInputFields(send(HttpMethod.PUT, "/companies/" + id, request))).containsExactly(field);
        assertThat(get("/companies/" + id)).containsEntry("name", "Acme").containsEntry("website", "https://acme.example");
    }

    @Test
    void unreadableBody() { // R16: malformed JSON is bad input too
        client.post().uri("/companies").contentType(MediaType.APPLICATION_JSON).body("{\"name\": ")
                .exchange()
                .expectStatus().isBadRequest()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON);
        assertThat(count("companies")).isZero();
    }
}
