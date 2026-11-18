package ru.sbleskom.api;

import io.restassured.response.ValidatableResponse;
import ru.sbleskom.api.specs.ApiSpecs;

import static io.restassured.RestAssured.given;

public class CatalogApi {
    public ValidatableResponse getCollection(String slug) {
        return given()
                .spec(ApiSpecs.request())
                .pathParam("slug", slug)
                .when()
                .get("/collection/{slug}.json")
                .then()
                .spec(ApiSpecs.jsonResponse());
    }
}
