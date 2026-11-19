package ru.sbleskom.api;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;
import static ru.sbleskom.api.specs.ApiSpecs.jsonResponse;
import static ru.sbleskom.api.specs.ApiSpecs.request;

public class CatalogApi {

    @Step("Получение коллекции GET /collection/{slug}.json")
    public ValidatableResponse getCollection(String slug) {
        return given(request())
                .pathParam("slug", slug)
                .when()
                .get("/collection/{slug}.json")
                .then()
                .spec(jsonResponse());
    }
}
