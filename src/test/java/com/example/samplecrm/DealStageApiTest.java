package com.example.samplecrm;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;

/** Spec 006: a stage on every deal. */
class DealStageApiTest extends ApiTestBase {

    private long acme;

    @BeforeEach
    void createCompany() {
        acme = create("/companies", body("name", "Acme"));
    }

    private Map<String, Object> renewal() {
        return body("title", "Annual renewal", "amount", new BigDecimal("1000"), "companyId", acme);
    }

    private long createWithStage(String stage) {
        return create("/deals", with(renewal(), "stage", stage));
    }

    private String stageOf(long id) {
        return (String) get("/deals/" + id).get("stage");
    }

    @Test
    void newDealStartsOpen() { // AC-1
        long id = create("/deals", renewal());

        assertThat(stageOf(id)).isEqualTo("open");
    }

    @Test
    void createWithStage() { // AC-6
        long id = createWithStage("lost");

        assertThat(stageOf(id)).isEqualTo("lost");
    }

    @Test
    void updateToWon() { // AC-2
        long id = create("/deals", renewal());

        put("/deals/" + id, with(renewal(), "stage", "won"));

        assertThat(stageOf(id)).isEqualTo("won");
    }

    @Test
    void wonDealCanBeReopened() { // AC-4
        long id = createWithStage("won");

        put("/deals/" + id, with(renewal(), "stage", "open"));

        assertThat(stageOf(id)).isEqualTo("open");
    }

    @Test
    void updateWithoutStageKeepsIt() { // AC-7
        long id = createWithStage("won");

        put("/deals/" + id, with(renewal(), "amount", new BigDecimal("2500")));

        Map<String, Object> deal = get("/deals/" + id);
        assertThat(deal).containsEntry("stage", "won");
        assertThat(new BigDecimal(deal.get("amount").toString())).isEqualByComparingTo("2500");
    }

    @Test
    void updateWithNullStageKeepsIt() { // AC-11
        long id = createWithStage("won");

        put("/deals/" + id, with(renewal(), "stage", null));

        assertThat(stageOf(id)).isEqualTo("won");
    }

    @Test
    void unknownStageOnCreateIsBadInput() { // AC-10
        assertThat(badInputFields(send(HttpMethod.POST, "/deals", with(renewal(), "stage", "maybe"))))
                .containsExactly("stage");
        assertThat(badInputFields(send(HttpMethod.POST, "/deals", with(renewal(), "stage", "Won"))))
                .containsExactly("stage");
        assertThat(count("deals")).isZero();
    }

    @Test
    void unknownStageOnUpdateIsBadInput() { // AC-3
        long id = createWithStage("won");

        assertThat(badInputFields(send(HttpMethod.PUT, "/deals/" + id,
                with(with(renewal(), "title", "Changed"), "stage", "maybe"))))
                .containsExactly("stage");

        Map<String, Object> deal = get("/deals/" + id);
        assertThat(deal).containsEntry("stage", "won").containsEntry("title", "Annual renewal");
    }

    @Test
    void everyResponseShowsStage() { // AC-13
        Map<String, Object> created = json(send(HttpMethod.POST, "/deals", renewal()).expectStatus().isCreated());
        assertThat(created).containsEntry("stage", "open");

        Map<String, Object> updated = put("/deals/" + id(created), with(renewal(), "stage", "won"));
        assertThat(updated).containsEntry("stage", "won");

        assertThat(getList("/deals")).singleElement().satisfies(deal -> assertThat(deal).containsEntry("stage", "won"));
    }
}
