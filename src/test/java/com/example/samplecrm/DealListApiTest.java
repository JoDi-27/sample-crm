package com.example.samplecrm;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpMethod;

class DealListApiTest extends ApiTestBase {

    private long x;
    private long y;
    private long a;
    private long b;
    private long d1;
    private long d2;
    private long d3;

    private long contact(String firstName, long companyId) {
        return create("/contacts", body("firstName", firstName, "lastName", "Test",
                "email", firstName.toLowerCase() + "@example.org", "companyId", companyId));
    }

    private long deal(String title, long companyId, long contactId) {
        return create("/deals", body("title", title, "amount", 100, "companyId", companyId, "mainContactId", contactId));
    }

    @BeforeEach
    void createDeals() {
        x = create("/companies", body("name", "X"));
        y = create("/companies", body("name", "Y"));
        a = contact("Ann", x);
        b = contact("Bob", x);
        d1 = deal("D1", x, a);
        d2 = deal("D2", x, b);
        d3 = deal("D3", y, a);
    }

    private static java.util.List<Object> ids(java.util.List<java.util.Map<String, Object>> deals) {
        return deals.stream().map(deal -> deal.get("title")).toList();
    }

    @Test
    void filterByContact() { // AC-1
        assertThat(ids(getList("/deals?contactId=" + a))).containsExactly("D1", "D3");
    }

    @Test
    void filterByCompanyAndContact() { // AC-2
        assertThat(ids(getList("/deals?companyId=" + x + "&contactId=" + a))).containsExactly("D1");
    }

    @Test
    void unknownContactIsEmpty() { // AC-3
        assertThat(getList("/deals?contactId=999999")).isEmpty();
    }

    @Test
    void noContactFilterUnchanged() { // AC-4, a guard on existing behavior
        assertThat(ids(getList("/deals"))).containsExactly("D1", "D2", "D3");
        assertThat(ids(getList("/deals?companyId=" + x))).containsExactly("D1", "D2");
    }

    @ParameterizedTest
    @ValueSource(strings = {"abc", "1.5"})
    void invalidContactFilter(String value) { // AC-5
        assertThat(badInputFields(send(HttpMethod.GET, "/deals?contactId=" + value, null))).containsExactly("contactId");
        assertThat(getList("/deals")).hasSize(3);
    }
}
