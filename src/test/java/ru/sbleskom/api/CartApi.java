package ru.sbleskom.api;

import io.qameta.allure.Step;
import io.restassured.filter.Filter;
import io.restassured.filter.cookie.CookieFilter;
import io.restassured.response.Response;
import ru.sbleskom.api.models.cart.AddToCartRequestModel;
import ru.sbleskom.api.models.cart.CartResponseModel;

import java.net.URI;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.URLENC;
import static ru.sbleskom.api.specs.BaseSpec.BASE_URI;
import static ru.sbleskom.api.specs.cart.CartSpec.SUCCESSFUL_CART_RESPONSE;
import static ru.sbleskom.api.specs.cart.CartSpec.cartMutationRequestSpec;
import static ru.sbleskom.api.specs.cart.CartSpec.cartRequestSpec;

public class CartApi {

    private static final String CART_PATH = "/cart_items";
    private static final String CART_JSON_PATH = CART_PATH + ".json";
    private static final String CSRF_COOKIE = "x_csrf_token";

    private final CookieFilter cookies = new CookieFilter();
    private final String origin;
    private String csrfToken;
    private final Filter captureCsrfToken = (request, responseSpec, context) -> {
        Response response = context.next(request, responseSpec);
        String updatedCsrfToken = response.getCookie(CSRF_COOKIE);
        if (updatedCsrfToken != null) {
            csrfToken = updatedCsrfToken;
        }
        return response;
    };

    public CartApi() {
        URI uri = URI.create(BASE_URI);
        origin = uri.getScheme() + "://" + uri.getRawAuthority();
    }

    @Step("Получение корзины GET /cart_items/v2.json")
    public CartResponseModel getCartV2() {
        return given(cartRequestSpec(cookies))
                .filter(captureCsrfToken)
                .when()
                .get(CART_PATH + "/v2.json")
                .then()
                .spec(SUCCESSFUL_CART_RESPONSE)
                .extract()
                .as(CartResponseModel.class);
    }

    @Step("Добавление товара в корзину POST /cart_items.json")
    public CartResponseModel addItem(AddToCartRequestModel item) {
        return given(cartMutationRequestSpec(cookies, requireCsrfToken(), origin, origin + CART_PATH))
                .filter(captureCsrfToken)
                .contentType(URLENC)
                .formParam("variant_ids[" + item.variantId() + "]", item.quantity())
                .formParam("lang", "")
                .when()
                .post(CART_JSON_PATH)
                .then()
                .spec(SUCCESSFUL_CART_RESPONSE)
                .extract()
                .as(CartResponseModel.class);
    }

    @Step("Удаление товара из корзины DELETE /cart_items/{variantId}.json")
    public CartResponseModel deleteItem(long variantId) {
        return given(cartMutationRequestSpec(cookies, requireCsrfToken(), origin, origin + CART_PATH))
                .filter(captureCsrfToken)
                .pathParam("variantId", variantId)
                .when()
                .delete(CART_PATH + "/{variantId}.json")
                .then()
                .spec(SUCCESSFUL_CART_RESPONSE)
                .extract()
                .as(CartResponseModel.class);
    }

    private String requireCsrfToken() {
        if (csrfToken == null || csrfToken.isBlank()) {
            throw new IllegalStateException("Read the guest cart before modifying it to obtain a CSRF token");
        }
        return csrfToken;
    }
}
