package com.example.samplecrm;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpMethod;

class DealApiTest extends ApiTestBase {

    private long acme;
    private long ana;

    @BeforeEach
    void createCompanyAndContact() {
        acme = create("/companies", body("name", "Acme"));
        ana = create("/contacts",
                body("firstName", "Ana", "lastName", "Silva", "email", "ana@acme.example", "companyId", acme));
    }

    private Map<String, Object> renewal() {
        return body("title", "Annual renewal", "amount", new BigDecimal("12500.50"),
                "companyId", acme, "mainContactId", ana);
    }

    private static BigDecimal amount(Map<String, Object> deal) {
        return new BigDecimal(deal.get("amount").toString());
    }

    @Test
    void createAndRead() { // AC-4
        long id = create("/deals", renewal());

        Map<String, Object> deal = get("/deals/" + id);
        assertThat(deal)
                .containsEntry("id", (int) id)
                .containsEntry("title", "Annual renewal")
                .containsEntry("companyId", (int) acme)
                .containsEntry("mainContactId", (int) ana);
        assertThat(amount(deal)).isEqualByComparingTo("12500.50");
    }

    @Test
    void createWithoutMainContact() { // R3: main contact is optional
        long id = create("/deals", with(renewal(), "mainContactId", ABSENT));

        assertThat(get("/deals/" + id)).containsEntry("mainContactId", null);
    }

    @Test
    void update() { // AC-5
        long globex = create("/companies", body("name", "Globex"));
        long id = create("/deals", renewal());
        Map<String, Object> changed = body("title", "Two-year renewal", "amount", new BigDecimal("30000"),
                "companyId", globex, "mainContactId", null);

        Map<String, Object> updated = put("/deals/" + id, changed);
        assertThat(updated).containsEntry("title", "Two-year renewal").containsEntry("companyId", (int) globex);
        assertThat(amount(updated)).isEqualByComparingTo("30000");

        Map<String, Object> read = get("/deals/" + id);
        assertThat(read)
                .containsEntry("title", "Two-year renewal")
                .containsEntry("companyId", (int) globex)
                .containsEntry("mainContactId", null);
        assertThat(amount(read)).isEqualByComparingTo("30000");
    }

    @Test
    void delete() { // AC-6
        long id = create("/deals", renewal());

        delete("/deals/" + id);

        assertNotFound(HttpMethod.GET, "/deals/" + id, null);
    }

    @Test
    void unknownId() { // AC-7
        long existing = create("/deals", renewal());

        assertNotFound(HttpMethod.GET, "/deals/999", null);
        assertNotFound(HttpMethod.PUT, "/deals/999", renewal());
        assertNotFound(HttpMethod.DELETE, "/deals/999", null);

        assertThat(count("deals")).isEqualTo(1);
        assertThat(get("/deals/" + existing)).containsEntry("title", "Annual renewal");
    }

    @Test
    void listAllAndByCompany() { // AC-9
        long globex = create("/companies", body("name", "Globex"));
        create("/deals", body("title", "Acme one", "amount", 100, "companyId", acme));
        create("/deals", body("title", "Acme two", "amount", 200, "companyId", acme));
        create("/deals", body("title", "Globex one", "amount", 300, "companyId", globex));

        assertThat(getList("/deals")).extracting(deal -> deal.get("title"))
                .containsExactlyInAnyOrder("Acme one", "Acme two", "Globex one");
        assertThat(getList("/deals?companyId=" + acme)).extracting(deal -> deal.get("title"))
                .containsExactlyInAnyOrder("Acme one", "Acme two");
    }

    /** Bad deals for both create and update. Ids 1 are the company and contact made before each test. */
    static Stream<Arguments> badDeals() {
        Map<String, Object> valid = body("title", "Annual renewal", "amount", 100, "companyId", 1, "mainContactId", 1);
        return Stream.of(
                Arguments.of(with(valid, "title", ABSENT), "title"),
                Arguments.of(with(valid, "title", " "), "title"),
                Arguments.of(with(valid, "title", "x".repeat(201)), "title"),
                Arguments.of(with(valid, "amount", ABSENT), "amount"),
                Arguments.of(with(valid, "amount", -1), "amount"),
                Arguments.of(with(valid, "amount", new BigDecimal("10.001")), "amount"),
                Arguments.of(with(valid, "amount", "lots"), "amount"),
                Arguments.of(with(valid, "companyId", 999), "companyId"),
                Arguments.of(with(valid, "mainContactId", 999), "mainContactId"));
    }

    static Stream<Arguments> badNewDeals() {
        Map<String, Object> valid = body("title", "Annual renewal", "amount", 100, "companyId", 1, "mainContactId", 1);
        return Stream.concat(badDeals(), Stream.of(Arguments.of(with(valid, "companyId", ABSENT), "companyId")));
    }

    @ParameterizedTest
    @MethodSource("badNewDeals")
    void badInputOnCreate(Map<String, Object> request, String field) { // AC-16
        assertThat(badInputFields(send(HttpMethod.POST, "/deals", request))).containsExactly(field);
        assertThat(count("deals")).isZero();
    }

    @ParameterizedTest
    @MethodSource("badDeals")
    void badInputOnUpdate(Map<String, Object> request, String field) { // AC-16
        long id = create("/deals", renewal());

        assertThat(badInputFields(send(HttpMethod.PUT, "/deals/" + id, request))).containsExactly(field);
        Map<String, Object> deal = get("/deals/" + id);
        assertThat(deal).containsEntry("title", "Annual renewal").containsEntry("companyId", (int) acme)
                .containsEntry("mainContactId", (int) ana);
        assertThat(amount(deal)).isEqualByComparingTo("12500.50");
    }

    @Test
    void zeroAmountIsAccepted() { // R16: only negative amounts are wrong
        long id = create("/deals", with(renewal(), "amount", 0));

        assertThat(amount(get("/deals/" + id))).isEqualByComparingTo("0");
    }

    @Test
    void deleteCompanyKeepsDeals() { // AC-19
        long id = create("/deals", renewal());

        delete("/companies/" + acme);

        assertThat(get("/deals/" + id)).containsEntry("title", "Annual renewal").containsEntry("companyId", null);
    }

    @Test
    void deleteContactKeepsDeals() { // AC-20
        long id = create("/deals", renewal());

        delete("/contacts/" + ana);

        assertThat(get("/deals/" + id))
                .containsEntry("title", "Annual renewal")
                .containsEntry("companyId", (int) acme)
                .containsEntry("mainContactId", null);
    }

    @Test
    void updateAfterCompanyDeleted() { // AC-25
        long id = create("/deals", renewal());
        delete("/companies/" + acme);

        Map<String, Object> updated = put("/deals/" + id,
                body("title", "Annual renewal", "amount", new BigDecimal("15000"), "mainContactId", ana));

        assertThat(updated).containsEntry("companyId", null);
        assertThat(amount(updated)).isEqualByComparingTo("15000");
        assertThat(get("/deals/" + id)).containsEntry("companyId", null);
    }
}
