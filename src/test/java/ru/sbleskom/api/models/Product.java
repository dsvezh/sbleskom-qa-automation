package ru.sbleskom.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

import java.util.List;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Product {
    private long id;
    private String title;
    private boolean available;
    private List<ProductVariant> variants;
}
