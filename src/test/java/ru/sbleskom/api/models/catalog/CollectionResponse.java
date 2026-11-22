package ru.sbleskom.api.models.catalog;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CollectionResponse(
        String status,
        int count,
        List<Product> products
) {}
