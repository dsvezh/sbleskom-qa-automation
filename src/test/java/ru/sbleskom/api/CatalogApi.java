package ru.sbleskom.api;

import io.qameta.allure.Step;
import ru.sbleskom.api.models.catalog.CollectionResponseModel;

import static io.restassured.RestAssured.given;
import static ru.sbleskom.api.specs.catalog.CatalogSpec.CATALOG_REQUEST_SPEC;
import static ru.sbleskom.api.specs.catalog.CatalogSpec.SUCCESSFUL_COLLECTION_RESPONSE;

public class CatalogApi {

    @Step("Получение коллекции GET /collection/{slug}.json")
    public CollectionResponseModel getCollection(String slug) {
        return given(CATALOG_REQUEST_SPEC)
                .pathParam("slug", slug)
                .when()
                .get("/collection/{slug}.json")
                .then()
                .spec(SUCCESSFUL_COLLECTION_RESPONSE)
                .extract()
                .as(CollectionResponseModel.class);
    }
}
