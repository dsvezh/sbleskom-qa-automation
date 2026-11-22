package ru.sbleskom.api.models.cart;

public record AddToCartRequestModel(
        long variantId,
        int quantity
) {}
