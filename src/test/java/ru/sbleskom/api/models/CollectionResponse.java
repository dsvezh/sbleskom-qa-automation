package ru.sbleskom.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

import java.util.List;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CollectionResponse {
    private String status;
    private int count;
    private List<Product> products;
}
