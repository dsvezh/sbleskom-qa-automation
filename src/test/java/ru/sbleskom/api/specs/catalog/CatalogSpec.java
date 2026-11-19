package ru.sbleskom.api.specs.catalog;

import io.restassured.specification.ResponseSpecification;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;
import static ru.sbleskom.api.specs.BaseSpec.successfulJsonResponse;

public final class CatalogSpec {
    public static final ResponseSpecification SUCCESSFUL_COLLECTION_RESPONSE = successfulJsonResponse()
            .expectBody(matchesJsonSchemaInClasspath("schemas/catalog/collection-response.json"))
            .expectBody("status", equalTo("ok"))
            .build();

    private CatalogSpec() {
    }
}
