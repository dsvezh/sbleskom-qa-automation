package ru.sbleskom.api;

import io.qameta.allure.Step;
import io.restassured.filter.cookie.CookieFilter;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import ru.sbleskom.api.models.AddToCartRequest;

import java.net.URI;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.URLENC;
import static ru.sbleskom.api.specs.ApiSpecs.baseUri;
import static ru.sbleskom.api.specs.ApiSpecs.jsonResponse;
import static ru.sbleskom.api.specs.ApiSpecs.request;

public class CartApi {

    private static final String CART_PATH = "/cart_items";
    private static final String CART_JSON_PATH = CART_PATH + ".json";
    private static final String CSRF_COOKIE = "x_csrf_token";

    private final CookieFilter cookies = new CookieFilter();
    private final String origin;
    private String csrfToken;

    public CartApi() {
        URI uri = URI.create(baseUri());
        origin = uri.getScheme() + "://" + uri.getRawAuthority();
    }

    @Step("Получение корзины GET /cart_items.json")
    public ValidatableResponse getCart() {
        Response response = cartRequest()
                .when()
                .get(CART_JSON_PATH);
        return validate(response);
    }

    @Step("Получение корзины GET /cart_items/v2.json")
    public ValidatableResponse getCartV2() {
        Response response = cartRequest()
                .when()
                .get(CART_PATH + "/v2.json");
        return validate(response);
    }

    @Step("Добавление товара в корзину POST /cart_items.json")
    public ValidatableResponse addItem(AddToCartRequest item) {
        Response response = mutationRequest()
                .contentType(URLENC)
                .formParam("variant_ids[" + item.getVariantId() + "]", item.getQuantity())
                .formParam("lang", "")
                .when()
                .post(CART_JSON_PATH);
        return validate(response);
    }

    @Step("Удаление товара из корзины DELETE /cart_items/{variantId}.json")
    public ValidatableResponse deleteItem(long variantId) {
        Response response = mutationRequest()
                .pathParam("variantId", variantId)
                .when()
                .delete(CART_PATH + "/{variantId}.json");
        return validate(response);
    }

    @Step("Подготовить запрос к корзине с cookies гостевой сессии")
    private RequestSpecification cartRequest() {
        return given(request())
                .filter(cookies);
    }

    @Step("Подготовить запрос на изменение корзины")
    private RequestSpecification mutationRequest() {
        if (csrfToken == null || csrfToken.isBlank()) {
            throw new IllegalStateException("Read the guest cart before modifying it to obtain a CSRF token");
        }
        return cartRequest()
                .header("X-CSRF-Token", csrfToken)
                .header("X-Requested-With", "XMLHttpRequest")
                .header("Origin", origin)
                .header("Referer", origin + CART_PATH);
    }

    @Step("Обработать CSRF-токен и проверить тип ответа корзины")
    private ValidatableResponse validate(Response response) {
        String updatedCsrfToken = response.getCookie(CSRF_COOKIE);
        if (updatedCsrfToken != null) {
            csrfToken = updatedCsrfToken;
        }
        return response.then()
                .spec(jsonResponse());
    }
}
