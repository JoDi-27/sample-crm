package com.example.samplecrm;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
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

    @Test
    void searchByNameIgnoringCase() { // spec 005 AC-1
        create("/companies", body("name", "Acme Corp"));
        create("/companies", body("name", "ACME Labs"));
        create("/companies", body("name", "Big Acme Ltd"));
        create("/companies", body("name", "Globex"));

        assertThat(names(getList("/companies?name={name}", "acme")))
                .containsExactlyInAnyOrder("Acme Corp", "ACME Labs", "Big Acme Ltd");
    }

    @Test
    void listWithoutNameReturnsAll() { // spec 005 AC-2
        create("/companies", body("name", "Acme Corp"));
        create("/companies", body("name", "Globex"));
        create("/companies", body("name", "Initech"));

        assertThat(names(getList("/companies"))).containsExactlyInAnyOrder("Acme Corp", "Globex", "Initech");
    }

    @Test
    void searchWithNoMatchIsEmpty() { // spec 005 AC-3
        create("/companies", body("name", "Acme Corp"));
        create("/companies", body("name", "Globex"));

        assertThat(getList("/companies?name={name}", "zzz")).isEmpty();
    }

    @Test
    void nameTextMatchesWildcardsLiterally() { // spec 005 AC-4
        create("/companies", body("name", "A_B Corp"));
        create("/companies", body("name", "AxB Corp"));
        create("/companies", body("name", "50% Off"));
        create("/companies", body("name", "50 Off Ltd"));
        create("/companies", body("name", "A\\B Corp"));
        create("/companies", body("name", "AB Corp"));

        assertThat(names(getList("/companies?name={name}", "a_b"))).containsExactly("A_B Corp");
        assertThat(names(getList("/companies?name={name}", "50%"))).containsExactly("50% Off");
        assertThat(names(getList("/companies?name={name}", "a\\b"))).containsExactly("A\\B Corp");
    }

    @Test
    void listsAreNotPaged() { // spec 005 AC-5
        for (int i = 1; i <= 25; i++) {
            create("/companies", body("name", "Acme " + i));
        }
        long globex = create("/companies", body("name", "Globex"));

        assertThat(names(getList("/companies?name={name}", "acme")))
                .hasSize(25)
                .allSatisfy(name -> assertThat((String) name).startsWith("Acme "));

        delete("/companies/" + globex);
        assertThat(getList("/companies")).hasSize(25);
    }

    @Test
    void emptyNameTextNarrowsNothing() { // spec 005 AC-6
        create("/companies", body("name", "Acme Corp"));
        create("/companies", body("name", "Globex"));

        assertThat(names(getList("/companies?name="))).containsExactlyInAnyOrder("Acme Corp", "Globex");
    }

    private static List<Object> names(List<Map<String, Object>> companies) {
        return companies.stream().map(company -> company.get("name")).toList();
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
