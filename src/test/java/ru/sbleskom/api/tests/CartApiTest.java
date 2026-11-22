package ru.sbleskom.api.tests;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import ru.sbleskom.api.ApiClient;
import ru.sbleskom.api.fixtures.CartFixture;
import ru.sbleskom.api.fixtures.ProductFixture;
import ru.sbleskom.api.models.cart.AddToCartRequestModel;
import ru.sbleskom.api.models.cart.CartItemModel;
import ru.sbleskom.api.models.cart.CartResponseModel;
import ru.sbleskom.api.models.cart.LegacyCartResponseModel;
import ru.sbleskom.api.models.catalog.ProductVariantModel;

import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic("SBLESKOM")
@Feature("Корзина")
@Tag("api")
@DisplayName("API корзины")
class CartApiTest {
    private final ApiClient api = new ApiClient();
    private final ProductFixture products = new ProductFixture(api.catalog);

    @Test
    @DisplayName("GET возвращает пустую корзину для нового гостя")
    void guestCartShouldBeEmpty() {
        LegacyCartResponseModel cart = step("Запросить корзину нового гостя", api.cart::getCart);

        step("Проверить, что гостевая корзина пуста", () -> assertAll(
                () -> assertEquals(0, cart.itemsCount(), "Счётчик позиций новой корзины не равен нулю"),
                () -> assertTrue(cart.orderLines().isEmpty(), "В новой корзине не должно быть позиций")));
    }

    @Test
    @DisplayName("POST добавляет выбранный вариант товара в корзину")
    void itemShouldBeAddedToCart() {
        ProductVariantModel variant = step("Подобрать доступный вариант товара в каталоге",
                () -> products.findAvailableVariant("all"));
        AddToCartRequestModel request = new AddToCartRequestModel(variant.id(), 1);

        try (CartFixture fixture = step("Открыть новую гостевую корзину", () -> new CartFixture(api.cart))) {
            CartResponseModel cart = step("Добавить одну единицу товара", () -> api.cart.addItem(request));

            step("Сверить ID товара, ID варианта и количество в модели корзины",
                    () -> assertSingleItem(cart, variant, request.quantity()));
        }
    }

    @Test
    @DisplayName("GET возвращает ранее добавленный вариант в той же гостевой сессии")
    void cartShouldReturnPreviouslyAddedItem() {
        ProductVariantModel variant = step("Подобрать доступный вариант товара в каталоге",
                () -> products.findAvailableVariant("all"));
        AddToCartRequestModel request = new AddToCartRequestModel(variant.id(), 1);

        try (CartFixture fixture = step("Открыть новую гостевую корзину", () -> new CartFixture(api.cart))) {
            step("Добавить выбранный вариант в корзину", () -> {
                fixture.addItem(request);
            });

            CartResponseModel cart = step("Прочитать корзину в той же сессии", api.cart::getCartV2);

            step("Сверить ID товара, ID варианта и количество в модели корзины",
                    () -> assertSingleItem(cart, variant, request.quantity()));
        }
    }

    @Test
    @DisplayName("DELETE удаляет выбранный вариант, и следующий GET возвращает пустую корзину")
    void itemShouldBeDeletedFromCart() {
        ProductVariantModel variant = step("Подобрать доступный вариант товара в каталоге",
                () -> products.findAvailableVariant("all"));
        AddToCartRequestModel request = new AddToCartRequestModel(variant.id(), 1);

        try (CartFixture fixture = step("Открыть новую гостевую корзину", () -> new CartFixture(api.cart))) {
            step("Добавить выбранный вариант и убедиться, что он появился в корзине", () -> {
                CartResponseModel beforeDeletion = fixture.addItem(request);
                assertSingleItem(beforeDeletion, variant, request.quantity());
            });

            step("Удалить выбранный вариант и проверить, что DELETE вернул пустую корзину", () -> {
                CartResponseModel deleted = api.cart.deleteItem(variant.id());
                assertAll(
                        () -> assertEquals(0, deleted.itemsCount(), "Счётчик позиций после DELETE"),
                        () -> assertTrue(deleted.items().isEmpty(), "DELETE должен вернуть пустую корзину"));
            });

            step("Повторно прочитать корзину через GET и подтвердить, что она пуста", () -> {
                LegacyCartResponseModel afterDeletion = api.cart.getCart();
                assertAll(
                        () -> assertEquals(0, afterDeletion.itemsCount(), "Счётчик позиций после повторного GET"),
                        () -> assertTrue(afterDeletion.orderLines().isEmpty(), "Удалённый вариант остался в корзине"));
            });
        }
    }

    private void assertSingleItem(CartResponseModel cart, ProductVariantModel variant, int quantity) {
        assertEquals(1, cart.items().size(), "В корзине должна быть одна позиция");
        CartItemModel item = cart.items().get(0);
        assertAll(
                () -> assertEquals(1, cart.itemsCount(), "Счётчик позиций корзины"),
                () -> assertEquals(variant.productId(), item.productId(), "ID товара"),
                () -> assertEquals(variant.id(), item.variantId(), "ID варианта"),
                () -> assertEquals(quantity, item.quantity(), "Количество товара")
        );
    }
}
