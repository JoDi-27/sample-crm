package com.example.samplecrm;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpMethod;

class DealListApiTest extends ApiTestBase {

    private long x;
    private long y;
    private long contactA;
    private long contactB;

    @BeforeEach
    void createRecords() {
        x = create("/companies", body("name", "X"));
        y = create("/companies", body("name", "Y"));
        contactA = contact("Ana");
        contactB = contact("Bruno");
    }

    private long contact(String firstName) {
        return create("/contacts", body("firstName", firstName, "lastName", "Test",
                "email", firstName.toLowerCase() + "@example.org"));
    }

    private void deal(String title, long companyId, long contactId) {
        create("/deals", body("title", title, "amount", 100, "companyId", companyId, "mainContactId", contactId));
    }

    private static List<Object> titles(List<Map<String, Object>> deals) {
        return deals.stream().map(deal -> deal.get("title")).toList();
    }

    @Test
    void filterByContact() { // AC-1
        deal("D1", x, contactA);
        deal("D2", x, contactB);
        deal("D3", y, contactA);

        assertThat(titles(getList("/deals?mainContactId=" + contactA))).containsExactly("D1", "D3");
    }

    @Test
    void filterByCompanyAndContact() { // AC-2
        deal("AX", x, contactA);
        deal("AY", y, contactA);
        deal("BX", x, contactB);

        assertThat(titles(getList("/deals?companyId=" + x + "&mainContactId=" + contactA))).containsExactly("AX");
    }

    @Test
    void unknownContactIsEmpty() { // AC-3
        deal("D1", x, contactA);

        assertThat(getList("/deals?mainContactId=999")).isEmpty();
    }

    @Test
    void noContactFilterUnchanged() { // AC-4, a guard on existing behavior
        deal("D1", x, contactA);
        deal("D2", y, contactB);
        deal("D3", x, contactB);

        assertThat(titles(getList("/deals"))).containsExactly("D1", "D2", "D3");
        assertThat(titles(getList("/deals?companyId=" + x))).containsExactly("D1", "D3");
    }

    @ParameterizedTest
    @ValueSource(strings = {"abc", "1.5"})
    void invalidContactFilter(String value) { // AC-5
        deal("D1", x, contactA);

        assertThat(badInputFields(send(HttpMethod.GET, "/deals?mainContactId=" + value, null)))
                .containsExactly("mainContactId");
        assertThat(count("deals")).isEqualTo(1);
    }
}
