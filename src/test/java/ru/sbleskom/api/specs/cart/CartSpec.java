package ru.sbleskom.api.specs.cart;

import io.restassured.filter.cookie.CookieFilter;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static ru.sbleskom.api.specs.BaseSpec.request;
import static ru.sbleskom.api.specs.BaseSpec.successfulJsonResponse;

public final class CartSpec {
    public static final ResponseSpecification SUCCESSFUL_LEGACY_CART_RESPONSE = successfulJsonResponse()
            .expectBody(matchesJsonSchemaInClasspath("schemas/cart/legacy-cart-response.json"))
            .build();

    public static final ResponseSpecification SUCCESSFUL_CART_RESPONSE = successfulJsonResponse()
            .expectBody(matchesJsonSchemaInClasspath("schemas/cart/cart-response.json"))
            .build();

    private CartSpec() {
    }

    public static RequestSpecification cartRequest(CookieFilter cookies) {
        return given(request()).filter(cookies);
    }

    public static RequestSpecification cartMutationRequest(CookieFilter cookies, String csrfToken,
                                                           String origin, String referer) {
        return cartRequest(cookies)
                .header("X-CSRF-Token", csrfToken)
                .header("X-Requested-With", "XMLHttpRequest")
                .header("Origin", origin)
                .header("Referer", referer);
    }
}
