package ru.sbleskom.api.fixtures;

import ru.sbleskom.api.CartApi;
import ru.sbleskom.api.models.AddToCartRequest;
import ru.sbleskom.api.models.CartItem;
import ru.sbleskom.api.models.CartResponse;

public class CartFixture implements AutoCloseable {
    private final CartApi cartApi = new CartApi();

    public CartFixture() {
        cartApi.getCart().statusCode(200);
    }

    public CartApi api() {
        return cartApi;
    }

    public CartResponse addItem(AddToCartRequest item) {
        return cartApi.addItem(item)
                .statusCode(200)
                .extract().as(CartResponse.class);
    }

    @Override
    public void close() {
        CartResponse cart = cartApi.getCartV2()
                .statusCode(200)
                .extract().as(CartResponse.class);
        for (CartItem item : cart.getItems()) {
            cartApi.deleteItem(item.getVariantId()).statusCode(200);
        }
    }
}
