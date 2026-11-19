package ru.sbleskom.api.tests;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import ru.sbleskom.api.CatalogApi;
import ru.sbleskom.api.models.CollectionResponse;
import ru.sbleskom.api.models.Product;

import static io.qameta.allure.Allure.step;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic("SBLESKOM")
@Feature("Каталог")
@Tag("api")
@DisplayName("API каталога")
class CatalogApiTest {
    @Test
    @DisplayName("GET возвращает товары коллекции «Каталог»")
    void collectionShouldContainProducts() {
        CollectionResponse collection = step("Запросить каталог и убедиться, что в ответе есть товары",
                () -> new CatalogApi().getCollection("all")
                        .statusCode(200)
                        .body("status", equalTo("ok"))
                        .body("products", hasSize(greaterThan(0)))
                        .extract().as(CollectionResponse.class));

        step("Проверить общее число товаров, ID и название первого товара", () -> {
            Product firstProduct = collection.getProducts().get(0);
            assertAll(
                    () -> assertTrue(collection.getCount() >= collection.getProducts().size(),
                            "Общее число товаров меньше размера полученной страницы"),
                    () -> assertTrue(firstProduct.getId() > 0, "У товара должен быть положительный ID"),
                    () -> assertTrue(firstProduct.getTitle() != null && !firstProduct.getTitle().isBlank(),
                            "У товара должно быть название")
            );
        });
    }
}
