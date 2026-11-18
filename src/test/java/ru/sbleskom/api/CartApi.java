package ru.sbleskom.api;

import io.restassured.filter.cookie.CookieFilter;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import ru.sbleskom.api.models.AddToCartRequest;
import ru.sbleskom.api.specs.ApiSpecs;

import java.net.URI;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.URLENC;

public class CartApi {
    private static final String CART_PATH = "/cart_items";
    private static final String CART_JSON_PATH = CART_PATH + ".json";
    private static final String CSRF_COOKIE = "x_csrf_token";

    private final CookieFilter cookies = new CookieFilter();
    private final String origin;
    private String csrfToken;

    public CartApi() {
        URI baseUri = URI.create(ApiSpecs.baseUri());
        origin = baseUri.getScheme() + "://" + baseUri.getRawAuthority();
    }

    public ValidatableResponse getCart() {
        return validate(request().get(CART_JSON_PATH));
    }

    public ValidatableResponse getCartV2() {
        return validate(request().get(CART_PATH + "/v2.json"));
    }

    public ValidatableResponse addItem(long variantId, int quantity) {
        return addItem(new AddToCartRequest(variantId, quantity));
    }

    public ValidatableResponse addItem(AddToCartRequest item) {
        Response response = mutationRequest()
                .contentType(URLENC)
                .formParam("variant_ids[" + item.getVariantId() + "]", item.getQuantity())
                .formParam("lang", "")
                .post(CART_JSON_PATH);
        return validate(response);
    }

    public ValidatableResponse deleteItem(long variantId) {
        Response response = mutationRequest()
                .pathParam("variantId", variantId)
                .delete(CART_PATH + "/{variantId}.json");
        return validate(response);
    }

    private RequestSpecification request() {
        return given().spec(ApiSpecs.request()).filter(cookies);
    }

    private RequestSpecification mutationRequest() {
        if (csrfToken == null || csrfToken.isBlank()) {
            throw new IllegalStateException("Read the guest cart before modifying it to obtain a CSRF token");
        }
        return request()
                .header("X-CSRF-Token", csrfToken)
                .header("X-Requested-With", "XMLHttpRequest")
                .header("Origin", origin)
                .header("Referer", origin + CART_PATH);
    }

    private ValidatableResponse validate(Response response) {
        String updatedCsrfToken = response.getCookie(CSRF_COOKIE);
        if (updatedCsrfToken != null) {
            csrfToken = updatedCsrfToken;
        }
        return response.then().spec(ApiSpecs.jsonResponse());
    }
}
