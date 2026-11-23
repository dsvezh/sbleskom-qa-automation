package ru.sbleskom.api.specs.cart;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.cookie.CookieFilter;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static ru.sbleskom.api.specs.BaseSpec.BASE_REQUEST_SPEC;
import static ru.sbleskom.api.specs.BaseSpec.successfulJsonResponse;

public final class CartSpec {
    public static final ResponseSpecification SUCCESSFUL_LEGACY_CART_RESPONSE = successfulJsonResponse()
            .expectBody(matchesJsonSchemaInClasspath("schemas/cart/legacy-cart-response.json"))
            .build();

    public static final ResponseSpecification SUCCESSFUL_CART_RESPONSE = successfulJsonResponse()
            .expectBody(matchesJsonSchemaInClasspath("schemas/cart/cart-response.json"))
            .build();

    public static RequestSpecification cartRequestSpec(CookieFilter cookies) {
        return new RequestSpecBuilder()
                .addRequestSpecification(BASE_REQUEST_SPEC)
                .addFilter(cookies)
                .build();
    }

    public static RequestSpecification cartMutationRequestSpec(CookieFilter cookies, String csrfToken,
                                                               String origin, String referer) {
        return new RequestSpecBuilder()
                .addRequestSpecification(cartRequestSpec(cookies))
                .addHeader("X-CSRF-Token", csrfToken)
                .addHeader("X-Requested-With", "XMLHttpRequest")
                .addHeader("Origin", origin)
                .addHeader("Referer", referer)
                .build();
    }
}
