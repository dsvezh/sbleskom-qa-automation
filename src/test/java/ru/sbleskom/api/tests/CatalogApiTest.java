package ru.sbleskom.api.tests;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import ru.sbleskom.api.ApiClient;
import ru.sbleskom.api.models.catalog.CollectionResponse;
import ru.sbleskom.api.models.catalog.Product;

import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic("SBLESKOM")
@Feature("Каталог")
@Tag("api")
@DisplayName("API каталога")
class CatalogApiTest {
    private final ApiClient api = new ApiClient();

    @Test
    @DisplayName("GET возвращает товары коллекции «Каталог»")
    void collectionShouldContainProducts() {
        CollectionResponse collection = step("Запросить коллекцию «Каталог»",
                () -> api.catalog.getCollection("all"));

        step("Проверить общее число товаров, ID и название первого товара", () -> {
            assertTrue(!collection.products().isEmpty(), "Коллекция «Каталог» должна содержать товары");
            Product firstProduct = collection.products().get(0);
            assertAll(
                    () -> assertTrue(collection.count() >= collection.products().size(),
                            "Общее число товаров меньше размера полученной страницы"),
                    () -> assertTrue(firstProduct.id() > 0, "У товара должен быть положительный ID"),
                    () -> assertTrue(!firstProduct.title().isBlank(),
                            "У товара должно быть название")
            );
        });
    }
}
