package ru.sbleskom.web.pages;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.exactOwnText;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.webdriver;
import static com.codeborne.selenide.WebDriverConditions.url;

public class CartPage {
    private final SelenideElement cartHeading = $(".checkout__main-title");
    private final ElementsCollection cartItems = $$(".cart-item[data-item-id]");
    private final String itemTitle = ".item-title";
    private final String itemQuantity = ".product-qtt__qtt";

    @Step("Проверить открытие корзины с заголовком: {expectedHeading}")
    public CartPage shouldHaveCartOpened(String expectedHeading) {
        webdriver().shouldHave(url(Configuration.baseUrl + "/cart_items"));
        cartHeading.shouldBe(visible).shouldHave(exactText(expectedHeading));
        return this;
    }

    @Step("Проверить товар в корзине: {expectedTitle}, количество {expectedQuantity}, число позиций {expectedCartSize}")
    public CartPage shouldHaveProductInCart(String expectedProductId, String expectedTitle,
                                           int expectedQuantity, int expectedCartSize) {
        SelenideElement cartItem = cartItems.shouldHave(size(expectedCartSize))
                .findBy(attribute("data-product-id", expectedProductId));
        cartItem.shouldBe(visible);
        cartItem.$(itemTitle).shouldBe(visible).shouldHave(exactOwnText(expectedTitle));
        cartItem.$(itemQuantity).shouldBe(visible).shouldHave(value(String.valueOf(expectedQuantity)));
        return this;
    }
}
