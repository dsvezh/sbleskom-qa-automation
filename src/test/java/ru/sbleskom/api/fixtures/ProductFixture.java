package ru.sbleskom.api.fixtures;

import ru.sbleskom.api.CatalogApi;
import ru.sbleskom.api.models.catalog.CollectionResponseModel;
import ru.sbleskom.api.models.catalog.ProductModel;
import ru.sbleskom.api.models.catalog.ProductVariantModel;

public class ProductFixture {
    private final CatalogApi catalogApi;

    public ProductFixture(CatalogApi catalogApi) {
        this.catalogApi = catalogApi;
    }

    public ProductVariantModel findAvailableVariant(String collectionSlug) {
        CollectionResponseModel collection = catalogApi.getCollection(collectionSlug);

        return collection.products().stream()
                .filter(ProductModel::available)
                .flatMap(product -> product.variants().stream())
                .filter(ProductVariantModel::available)
                .filter(variant -> variant.quantity() != null && variant.quantity() > 0)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "No in-stock product variant in collection: " + collectionSlug));
    }
}
