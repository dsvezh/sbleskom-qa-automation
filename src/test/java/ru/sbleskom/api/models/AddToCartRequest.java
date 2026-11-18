package ru.sbleskom.api.models;

import lombok.Value;

@Value
public class AddToCartRequest {
    long variantId;
    int quantity;
}
