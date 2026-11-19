package ru.sbleskom.api.fixtures;

import ru.sbleskom.api.CatalogApi;
import ru.sbleskom.api.models.catalog.CollectionResponse;
import ru.sbleskom.api.models.catalog.Product;
import ru.sbleskom.api.models.catalog.ProductVariant;

public class ProductFixture {
    private final CatalogApi catalogApi;

    public ProductFixture(CatalogApi catalogApi) {
        this.catalogApi = catalogApi;
    }

    public ProductVariant findAvailableVariant(String collectionSlug) {
        CollectionResponse collection = catalogApi.getCollection(collectionSlug);

        return collection.products().stream()
                .filter(Product::available)
                .flatMap(product -> product.variants().stream())
                .filter(ProductVariant::available)
                .filter(variant -> variant.quantity() != null && variant.quantity() > 0)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "No in-stock product variant in collection: " + collectionSlug));
    }
}
