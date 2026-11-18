package ru.sbleskom.api.fixtures;

import ru.sbleskom.api.CatalogApi;
import ru.sbleskom.api.models.CollectionResponse;
import ru.sbleskom.api.models.Product;
import ru.sbleskom.api.models.ProductVariant;

public class ProductFixture {
    private final CatalogApi catalogApi = new CatalogApi();

    public ProductVariant findAvailableVariant(String collectionSlug) {
        CollectionResponse collection = catalogApi.getCollection(collectionSlug)
                .statusCode(200)
                .extract().as(CollectionResponse.class);

        return collection.getProducts().stream()
                .filter(Product::isAvailable)
                .flatMap(product -> product.getVariants().stream())
                .filter(ProductVariant::isAvailable)
                .filter(variant -> variant.getQuantity() != null && variant.getQuantity() > 0)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "No in-stock product variant in collection: " + collectionSlug));
    }
}
