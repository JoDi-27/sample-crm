package com.example.samplecrm;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;

class DealListApiTest extends ApiTestBase {

    private long x;
    private long y;
    private long a;
    private long b;

    @BeforeEach
    void createRecords() {
        x = create("/companies", body("name", "X"));
        y = create("/companies", body("name", "Y"));
        a = create("/contacts", body("firstName", "Ana", "lastName", "Silva", "email", "ana@x.example"));
        b = create("/contacts", body("firstName", "Ben", "lastName", "Cole", "email", "ben@x.example"));
        deal("D1", x, a);
        deal("D2", x, b);
        deal("D3", y, a);
    }

    private void deal(String title, long company, long contact) {
        create("/deals", body("title", title, "amount", 100, "companyId", company, "mainContactId", contact));
    }

    private static List<Object> titles(List<Map<String, Object>> deals) {
        return deals.stream().map(deal -> deal.get("title")).toList();
    }

    @Test
    void filterByContact() { // AC-1
        assertThat(titles(getList("/deals?mainContactId=" + a))).containsExactly("D1", "D3");
    }

    @Test
    void filterByCompanyAndContact() { // AC-2
        assertThat(titles(getList("/deals?companyId=" + x + "&mainContactId=" + a))).containsExactly("D1");
    }

    @Test
    void unknownContactReturnsEmpty() { // AC-3
        assertThat(getList("/deals?mainContactId=999999")).isEmpty();
    }

    @Test
    void noContactFilterKeepsResults() { // AC-4, a guard on existing behavior
        assertThat(titles(getList("/deals"))).containsExactly("D1", "D2", "D3");
        assertThat(titles(getList("/deals?companyId=" + x))).containsExactly("D1", "D2");
    }

    @Test
    void badContactFilter() { // AC-5
        assertThat(badInputFields(send(HttpMethod.GET, "/deals?mainContactId=abc", null)))
                .contains("mainContactId");
    }
}
