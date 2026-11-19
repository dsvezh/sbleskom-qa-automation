package ru.sbleskom.api;

import io.qameta.allure.Step;
import io.restassured.filter.cookie.CookieFilter;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import ru.sbleskom.api.models.cart.AddToCartRequest;
import ru.sbleskom.api.models.cart.CartResponse;
import ru.sbleskom.api.models.cart.LegacyCartResponse;

import java.net.URI;

import static io.restassured.http.ContentType.URLENC;
import static ru.sbleskom.api.specs.BaseSpec.baseUri;
import static ru.sbleskom.api.specs.cart.CartSpec.SUCCESSFUL_CART_RESPONSE;
import static ru.sbleskom.api.specs.cart.CartSpec.SUCCESSFUL_LEGACY_CART_RESPONSE;
import static ru.sbleskom.api.specs.cart.CartSpec.cartMutationRequest;
import static ru.sbleskom.api.specs.cart.CartSpec.cartRequest;

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
    public LegacyCartResponse getCart() {
        Response response = cartRequest(cookies)
                .when()
                .get(CART_JSON_PATH);
        return readResponse(response, SUCCESSFUL_LEGACY_CART_RESPONSE, LegacyCartResponse.class);
    }

    @Step("Получение корзины GET /cart_items/v2.json")
    public CartResponse getCartV2() {
        Response response = cartRequest(cookies)
                .when()
                .get(CART_PATH + "/v2.json");
        return readResponse(response, SUCCESSFUL_CART_RESPONSE, CartResponse.class);
    }

    @Step("Добавление товара в корзину POST /cart_items.json")
    public CartResponse addItem(AddToCartRequest item) {
        Response response = mutationRequest()
                .contentType(URLENC)
                .formParam("variant_ids[" + item.variantId() + "]", item.quantity())
                .formParam("lang", "")
                .when()
                .post(CART_JSON_PATH);
        return readResponse(response, SUCCESSFUL_CART_RESPONSE, CartResponse.class);
    }

    @Step("Удаление товара из корзины DELETE /cart_items/{variantId}.json")
    public CartResponse deleteItem(long variantId) {
        Response response = mutationRequest()
                .pathParam("variantId", variantId)
                .when()
                .delete(CART_PATH + "/{variantId}.json");
        return readResponse(response, SUCCESSFUL_CART_RESPONSE, CartResponse.class);
    }

    private RequestSpecification mutationRequest() {
        if (csrfToken == null || csrfToken.isBlank()) {
            throw new IllegalStateException("Read the guest cart before modifying it to obtain a CSRF token");
        }
        return cartMutationRequest(cookies, csrfToken, origin, origin + CART_PATH);
    }

    private <T> T readResponse(Response response, ResponseSpecification spec, Class<T> responseType) {
        String updatedCsrfToken = response.getCookie(CSRF_COOKIE);
        if (updatedCsrfToken != null) {
            csrfToken = updatedCsrfToken;
        }
        return response.then()
                .spec(spec)
                .extract().as(responseType);
    }
}
