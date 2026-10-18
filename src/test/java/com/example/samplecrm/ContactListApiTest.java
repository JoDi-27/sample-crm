package com.example.samplecrm;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.http.HttpMethod;

class ContactListApiTest extends ApiTestBase {

    private long contact(String firstName, String lastName, Long companyId) {
        return create("/contacts", body("firstName", firstName, "lastName", lastName,
                "email", firstName.toLowerCase() + "@example.org", "companyId", companyId));
    }

    private void contacts(int howMany) {
        for (int i = 1; i <= howMany; i++) {
            contact("Person" + i, "Number" + i, null);
        }
    }

    @SuppressWarnings("unchecked")
    private static List<Map<String, Object>> items(Map<String, Object> page) {
        return (List<Map<String, Object>>) page.get("items");
    }

    private static List<String> names(Map<String, Object> page) {
        return items(page).stream().map(contact -> contact.get("firstName") + " " + contact.get("lastName")).toList();
    }

    @Test
    void pageAndStableOrder() { // AC-10
        contacts(5);

        Map<String, Object> first = get("/contacts?page=1&size=2");
        Map<String, Object> again = get("/contacts?page=1&size=2");

        assertThat(items(first)).hasSize(2);
        assertThat(first).containsEntry("page", 1).containsEntry("size", 2).containsEntry("total", 5);
        assertThat(items(again)).isEqualTo(items(first));
        assertThat(items(get("/contacts?page=2&size=2"))).hasSize(2).doesNotContainAnyElementsOf(items(first));
    }

    @Test
    void defaults() { // AC-11
        contacts(25);

        Map<String, Object> page = get("/contacts");

        assertThat(items(page)).hasSize(20);
        assertThat(page).containsEntry("page", 1).containsEntry("size", 20).containsEntry("total", 25);
    }

    @ParameterizedTest
    @CsvSource({"page=0, page", "size=0, size", "size=101, size", "page=abc, page"})
    void invalidPaging(String query, String parameter) { // AC-12
        assertThat(badInputFields(send(HttpMethod.GET, "/contacts?" + query, null))).containsExactly(parameter);
    }

    @Test
    void largestPageSizeIsAccepted() { // R12: 100 is allowed
        assertThat(get("/contacts?size=100")).containsEntry("size", 100);
    }

    @Test
    void byCompany() { // AC-13
        long acme = create("/companies", body("name", "Acme"));
        long globex = create("/companies", body("name", "Globex"));
        contact("Ana", "Silva", acme);
        contact("Bruno", "Costa", acme);
        contact("Carla", "Mendes", globex);
        contact("Diana", "Lopes", null);

        Map<String, Object> page = get("/contacts?companyId=" + acme);

        assertThat(names(page)).containsExactlyInAnyOrder("Ana Silva", "Bruno Costa");
        assertThat(page).containsEntry("total", 2);
    }

    @Test
    void byName() { // AC-14
        contact("Ana", "Silva", null);
        contact("Bruno", "Silvano", null);
        contact("Carla", "Mendes", null);

        Map<String, Object> page = get("/contacts?name=silv");

        assertThat(names(page)).containsExactlyInAnyOrder("Ana Silva", "Bruno Silvano");
        assertThat(page).containsEntry("total", 2);
    }

    @Test
    void byNameMatchesFirstNameIgnoringCase() { // R14: first or last name
        contact("Ana", "Silva", null);
        contact("Mariana", "Costa", null);
        contact("Carla", "Mendes", null);

        assertThat(names(get("/contacts?name=ANA"))).containsExactlyInAnyOrder("Ana Silva", "Mariana Costa");
    }

    @Test
    void byCompanyAndName() { // R13 and R14 together
        long acme = create("/companies", body("name", "Acme"));
        contact("Ana", "Silva", acme);
        contact("Bruno", "Silvano", null);

        assertThat(names(get("/contacts?companyId=" + acme + "&name=silv"))).containsExactly("Ana Silva");
    }

    @Test
    void pastLastPage() { // AC-15
        contacts(3);

        Map<String, Object> page = get("/contacts?page=5&size=2");

        assertThat(items(page)).isEmpty();
        assertThat(page).containsEntry("total", 3);
    }
}
