package ru.sbleskom.api.tests;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import ru.sbleskom.api.CartApi;
import ru.sbleskom.api.fixtures.CartFixture;
import ru.sbleskom.api.fixtures.ProductFixture;
import ru.sbleskom.api.models.AddToCartRequest;
import ru.sbleskom.api.models.CartItem;
import ru.sbleskom.api.models.CartResponse;
import ru.sbleskom.api.models.ProductVariant;

import static io.qameta.allure.Allure.step;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic("SBLESKOM")
@Feature("Корзина")
@Tag("api")
@DisplayName("API корзины")
class CartApiTest {
    private final ProductFixture products = new ProductFixture();

    @Test
    @DisplayName("GET возвращает пустую корзину для нового гостя")
    void guestCartShouldBeEmpty() {
        step("Запросить корзину нового гостя и проверить, что ответ не содержит позиций",
                () -> new CartApi().getCart()
                        .statusCode(200)
                        .body("items_count", equalTo(0))
                        .body("order_lines", hasSize(0)));
    }

    @Test
    @DisplayName("POST добавляет выбранный вариант товара в корзину")
    void itemShouldBeAddedToCart() {
        ProductVariant variant = step("Подобрать доступный вариант товара в каталоге",
                () -> products.findAvailableVariant("all"));
        AddToCartRequest request = new AddToCartRequest(variant.getId(), 1);

        try (CartFixture fixture = step("Открыть новую гостевую корзину", CartFixture::new)) {
            CartResponse cart = step("Добавить одну единицу товара и проверить количество в ответе",
                    () -> fixture.api().addItem(request)
                            .statusCode(200)
                            .body("items_count", equalTo(1))
                            .body("items[0].quantity", equalTo(request.getQuantity()))
                            .extract().as(CartResponse.class));

            step("Сверить ID товара, ID варианта и количество в модели корзины",
                    () -> assertSingleItem(cart, variant, request.getQuantity()));
        }
    }

    @Test
    @DisplayName("GET возвращает ранее добавленный вариант в той же гостевой сессии")
    void cartShouldReturnPreviouslyAddedItem() {
        ProductVariant variant = step("Подобрать доступный вариант товара в каталоге",
                () -> products.findAvailableVariant("all"));
        AddToCartRequest request = new AddToCartRequest(variant.getId(), 1);

        try (CartFixture fixture = step("Открыть новую гостевую корзину", CartFixture::new)) {
            step("Добавить выбранный вариант в корзину", () -> {
                fixture.addItem(request);
            });

            CartResponse cart = step("Прочитать корзину в той же сессии и проверить количество в ответе",
                    () -> fixture.api().getCartV2()
                            .statusCode(200)
                            .body("items_count", equalTo(1))
                            .body("items[0].quantity", equalTo(request.getQuantity()))
                            .extract().as(CartResponse.class));

            step("Сверить ID товара, ID варианта и количество в модели корзины",
                    () -> assertSingleItem(cart, variant, request.getQuantity()));
        }
    }

    @Test
    @DisplayName("DELETE удаляет выбранный вариант, и следующий GET возвращает пустую корзину")
    void itemShouldBeDeletedFromCart() {
        ProductVariant variant = step("Подобрать доступный вариант товара в каталоге",
                () -> products.findAvailableVariant("all"));
        AddToCartRequest request = new AddToCartRequest(variant.getId(), 1);

        try (CartFixture fixture = step("Открыть новую гостевую корзину", CartFixture::new)) {
            step("Добавить выбранный вариант и убедиться, что он появился в корзине", () -> {
                CartResponse beforeDeletion = fixture.addItem(request);
                assertSingleItem(beforeDeletion, variant, request.getQuantity());
            });

            step("Удалить выбранный вариант и проверить, что DELETE вернул пустую корзину", () -> {
                fixture.api().deleteItem(variant.getId())
                        .statusCode(200)
                        .body("items_count", equalTo(0))
                        .body("items", hasSize(0));
            });

            step("Повторно прочитать корзину через GET и подтвердить, что она пуста", () -> {
                CartResponse afterDeletion = fixture.api().getCart()
                        .statusCode(200)
                        .body("items_count", equalTo(0))
                        .body("order_lines", hasSize(0))
                        .extract().as(CartResponse.class);
                assertTrue(afterDeletion.getItems().isEmpty(), "Удалённый вариант остался в корзине");
            });
        }
    }

    private void assertSingleItem(CartResponse cart, ProductVariant variant, int quantity) {
        assertEquals(1, cart.getItems().size(), "В корзине должна быть одна позиция");
        CartItem item = cart.getItems().get(0);
        assertAll(
                () -> assertEquals(variant.getProductId(), item.getProductId(), "ID товара"),
                () -> assertEquals(variant.getId(), item.getVariantId(), "ID варианта"),
                () -> assertEquals(quantity, item.getQuantity(), "Количество товара")
        );
    }
}
