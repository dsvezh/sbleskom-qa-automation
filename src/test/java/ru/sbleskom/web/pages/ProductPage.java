package ru.sbleskom.web.pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.webdriver;
import static com.codeborne.selenide.WebDriverConditions.url;

public class ProductPage {
    private final SelenideElement title = $(".card__title");

    @Step("Проверить открытие карточки выбранного товара по адресу: {expectedUrl}")
    public ProductPage shouldBeOpened(String expectedUrl) {
        webdriver().shouldHave(url(expectedUrl));
        title.shouldBe(visible);
        return this;
    }

    @Step("Проверить название товара в карточке: {expectedTitle}")
    public ProductPage shouldHaveTitle(String expectedTitle) {
        title.shouldBe(visible).shouldHave(exactText(expectedTitle));
        return this;
    }
}
