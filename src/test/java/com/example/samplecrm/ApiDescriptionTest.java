package com.example.samplecrm;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class ApiDescriptionTest extends ApiTestBase {

    @Test
    @SuppressWarnings("unchecked")
    void listsEveryOperation() { // AC-21
        Map<String, Object> description = get("/v3/api-docs");

        assertThat((String) description.get("openapi")).startsWith("3.");
        assertThat((Map<String, Object>) description.get("info")).containsEntry("title", "sample-crm");

        Map<String, Map<String, Object>> paths = (Map<String, Map<String, Object>>) description.get("paths");
        for (String type : List.of("companies", "contacts", "deals")) {
            assertThat(paths.get("/" + type)).as("/" + type).containsKeys("get", "post");
            assertThat(paths.get("/" + type + "/{id}")).as("/" + type + "/{id}").containsKeys("get", "put", "delete");
        }

        assertThat(parameterNames(paths.get("/contacts").get("get")))
                .containsExactlyInAnyOrder("page", "size", "companyId", "name");
        assertThat(parameterNames(paths.get("/deals").get("get"))).containsExactly("companyId");
    }

    @SuppressWarnings("unchecked")
    private static List<Object> parameterNames(Object operation) {
        List<Map<String, Object>> parameters = (List<Map<String, Object>>) ((Map<String, Object>) operation)
                .get("parameters");
        return parameters.stream().map(parameter -> parameter.get("name")).toList();
    }
}
