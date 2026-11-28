package ru.sbleskom.api.fixtures;

import ru.sbleskom.api.CartApi;
import ru.sbleskom.api.models.cart.CartItemModel;
import ru.sbleskom.api.models.cart.CartResponseModel;

public class CartFixture implements AutoCloseable {
    private final CartApi cartApi;

    public CartFixture(CartApi cartApi) {
        this.cartApi = cartApi;
        cartApi.getCartV2();
    }

    @Override
    public void close() {
        CartResponseModel cart = cartApi.getCartV2();
        for (CartItemModel item : cart.items()) {
            cartApi.deleteItem(item.variantId());
        }
    }
}
