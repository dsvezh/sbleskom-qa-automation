package ru.sbleskom.api.fixtures;

import ru.sbleskom.api.CartApi;
import ru.sbleskom.api.models.cart.AddToCartRequest;
import ru.sbleskom.api.models.cart.CartItem;
import ru.sbleskom.api.models.cart.CartResponse;

public class CartFixture implements AutoCloseable {
    private final CartApi cartApi;

    public CartFixture(CartApi cartApi) {
        this.cartApi = cartApi;
        cartApi.getCart();
    }

    public CartResponse addItem(AddToCartRequest item) {
        return cartApi.addItem(item);
    }

    @Override
    public void close() {
        CartResponse cart = cartApi.getCartV2();
        for (CartItem item : cart.items()) {
            cartApi.deleteItem(item.variantId());
        }
    }
}
