package com.example.samplecrm;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpMethod;

class ContactApiTest extends ApiTestBase {

    /** A well-formed address longer than 254 characters. */
    private static final String LONG_EMAIL = "a".repeat(64) + "@" + ("b".repeat(60) + ".").repeat(4) + "com";

    private long acme;

    @BeforeEach
    void createCompany() {
        acme = create("/companies", body("name", "Acme"));
    }

    private Map<String, Object> ana(Object companyId) {
        return body("firstName", "Ana", "lastName", "Silva", "email", "ana@acme.example",
                "phone", "+351 912 345 678", "companyId", companyId);
    }

    @Test
    void createAtCompanyAndRead() { // AC-2
        long id = create("/contacts", ana(acme));

        assertThat(get("/contacts/" + id))
                .containsEntry("id", (int) id)
                .containsEntry("firstName", "Ana")
                .containsEntry("lastName", "Silva")
                .containsEntry("email", "ana@acme.example")
                .containsEntry("phone", "+351 912 345 678")
                .containsEntry("companyId", (int) acme);
    }

    @Test
    void createWithoutCompany() { // AC-3
        long id = create("/contacts", body("firstName", "Bruno", "lastName", "Costa", "email", "bruno@example.org"));

        assertThat(get("/contacts/" + id))
                .containsEntry("firstName", "Bruno")
                .containsEntry("phone", null)
                .containsEntry("companyId", null);
    }

    @Test
    void update() { // AC-5
        long globex = create("/companies", body("name", "Globex"));
        long id = create("/contacts", ana(acme));
        Map<String, Object> changed = body("firstName", "Ana Maria", "lastName", "Silva Santos",
                "email", "ana.maria@globex.example", "phone", "+351 900 000 000", "companyId", globex);

        assertThat(put("/contacts/" + id, changed))
                .containsEntry("id", (int) id)
                .containsEntry("firstName", "Ana Maria")
                .containsEntry("companyId", (int) globex);
        assertThat(get("/contacts/" + id))
                .containsEntry("firstName", "Ana Maria")
                .containsEntry("lastName", "Silva Santos")
                .containsEntry("email", "ana.maria@globex.example")
                .containsEntry("phone", "+351 900 000 000")
                .containsEntry("companyId", (int) globex);
    }

    @Test
    void delete() { // AC-6
        long id = create("/contacts", ana(acme));

        delete("/contacts/" + id);

        assertNotFound(HttpMethod.GET, "/contacts/" + id, null);
    }

    @Test
    void unknownId() { // AC-7
        long existing = create("/contacts", ana(acme));

        assertNotFound(HttpMethod.GET, "/contacts/999", null);
        assertNotFound(HttpMethod.PUT, "/contacts/999", ana(acme));
        assertNotFound(HttpMethod.DELETE, "/contacts/999", null);

        assertThat(count("contacts")).isEqualTo(1);
        assertThat(get("/contacts/" + existing)).containsEntry("firstName", "Ana");
    }

    static Stream<Arguments> badContacts() {
        Map<String, Object> valid = body("firstName", "Ana", "lastName", "Silva", "email", "ana@acme.example",
                "phone", "+351 912 345 678");
        return Stream.of(
                Arguments.of(with(valid, "firstName", ABSENT), "firstName"),
                Arguments.of(with(valid, "lastName", ""), "lastName"),
                Arguments.of(with(valid, "email", ABSENT), "email"),
                Arguments.of(with(valid, "firstName", "x".repeat(101)), "firstName"),
                Arguments.of(with(valid, "lastName", "x".repeat(101)), "lastName"),
                Arguments.of(with(valid, "email", LONG_EMAIL), "email"),
                Arguments.of(with(valid, "phone", "1".repeat(51)), "phone"),
                Arguments.of(with(valid, "email", "not-an-email"), "email"),
                Arguments.of(with(valid, "companyId", 999), "companyId"));
    }

    @ParameterizedTest
    @MethodSource("badContacts")
    void badInputOnCreate(Map<String, Object> request, String field) { // AC-16
        assertThat(badInputFields(send(HttpMethod.POST, "/contacts", request))).containsExactly(field);
        assertThat(count("contacts")).isZero();
    }

    @ParameterizedTest
    @MethodSource("badContacts")
    void badInputOnUpdate(Map<String, Object> request, String field) { // AC-16
        long id = create("/contacts", ana(acme));

        assertThat(badInputFields(send(HttpMethod.PUT, "/contacts/" + id, request))).containsExactly(field);
        assertThat(get("/contacts/" + id))
                .containsEntry("firstName", "Ana")
                .containsEntry("lastName", "Silva")
                .containsEntry("email", "ana@acme.example")
                .containsEntry("phone", "+351 912 345 678")
                .containsEntry("companyId", (int) acme);
    }

    @Test
    void namesEveryWrongField() { // AC-17
        Map<String, Object> request = body("lastName", "Silva", "email", "not-an-email");

        assertThat(badInputFields(send(HttpMethod.POST, "/contacts", request)))
                .containsExactlyInAnyOrder("firstName", "email");
        assertThat(count("contacts")).isZero();
    }

    @Test
    void namesWrongFieldsAndUnknownCompanyTogether() { // R16: every field, including references
        Map<String, Object> request = body("lastName", "Silva", "email", "ana@acme.example", "companyId", 999);

        assertThat(badInputFields(send(HttpMethod.POST, "/contacts", request)))
                .containsExactlyInAnyOrder("firstName", "companyId");
    }

    @Test
    void deleteCompanyKeepsContacts() { // AC-18
        long ana = create("/contacts", ana(acme));
        long bruno = create("/contacts",
                body("firstName", "Bruno", "lastName", "Costa", "email", "bruno@acme.example", "companyId", acme));

        delete("/companies/" + acme);

        assertThat(get("/contacts/" + ana)).containsEntry("firstName", "Ana").containsEntry("companyId", null);
        assertThat(get("/contacts/" + bruno)).containsEntry("firstName", "Bruno").containsEntry("companyId", null);
    }
}
