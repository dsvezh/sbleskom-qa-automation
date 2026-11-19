package ru.sbleskom.api.models.cart;

public record AddToCartRequest(long variantId, int quantity) {
}
