package com.example.samplecrm;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;

/** Spec 006: listing deals by stage. */
class DealStageListApiTest extends ApiTestBase {

    private long acme;
    private long globex;

    @BeforeEach
    void createCompanies() {
        acme = create("/companies", body("name", "Acme"));
        globex = create("/companies", body("name", "Globex"));
    }

    private void deal(String title, long companyId, String stage) {
        create("/deals", body("title", title, "amount", 100, "companyId", companyId, "stage", stage));
    }

    private static List<Object> titles(List<Map<String, Object>> deals) {
        return deals.stream().map(deal -> deal.get("title")).toList();
    }

    @Test
    void listByStageAndCompany() { // AC-5
        deal("Acme open", acme, "open");
        deal("Acme won", acme, "won");
        deal("Acme lost", acme, "lost");
        deal("Globex open", globex, "open");
        deal("Globex won", globex, "won");

        assertThat(titles(getList("/deals?stage=open"))).containsExactlyInAnyOrder("Acme open", "Globex open");
        assertThat(titles(getList("/deals?stage=won"))).containsExactlyInAnyOrder("Acme won", "Globex won");
        assertThat(titles(getList("/deals?stage=open&companyId=" + acme))).containsExactly("Acme open");
        assertThat(titles(getList("/deals?stage=lost&companyId=" + acme))).containsExactly("Acme lost");
    }

    @Test
    void noMatchIsEmpty() { // AC-12
        deal("Acme open", acme, "open");
        deal("Globex lost", globex, "lost");

        assertThat(getList("/deals?stage=lost&companyId=" + acme)).isEmpty();
    }

    @Test
    void unknownStageOnListIsBadInput() { // AC-8
        deal("Acme open", acme, "open");

        assertThat(badInputFields(send(HttpMethod.GET, "/deals?stage=maybe", null))).containsExactly("stage");
        assertThat(badInputFields(send(HttpMethod.GET, "/deals?stage=Open&companyId=" + acme, null)))
                .containsExactly("stage");
    }
}
