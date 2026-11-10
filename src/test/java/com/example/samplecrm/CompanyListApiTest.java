package com.example.samplecrm;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CompanyListApiTest extends ApiTestBase {

    @BeforeEach
    void createCompanies() {
        create("/companies", body("name", "Acme Ltd"));
        create("/companies", body("name", "ACME Labs"));
        create("/companies", body("name", "Globex"));
    }

    @Test
    void filterIgnoresCase() { // AC-1
        assertThat(getList("/companies?name=acme"))
                .extracting(company -> company.get("name"))
                .containsExactly("Acme Ltd", "ACME Labs");
    }

    @Test
    void filterWithNoMatch() { // AC-2
        assertThat(getList("/companies?name=zzz")).isEmpty();
    }

    @Test
    void noFilterReturnsAll() { // AC-3, a guard on existing behavior
        assertThat(getList("/companies")).hasSize(3);
        assertThat(getList("/companies?name=")).hasSize(3);
    }
}
