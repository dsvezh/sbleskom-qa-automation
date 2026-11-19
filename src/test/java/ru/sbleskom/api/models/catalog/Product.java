package ru.sbleskom.api.models.catalog;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Product(long id, String title, boolean available, List<ProductVariant> variants) {
}
