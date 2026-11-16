package ru.sbleskom.web.pages;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.webdriver;
import static com.codeborne.selenide.WebDriverConditions.url;

public class CatalogPage {
    private final SelenideElement heading = $(".catalog__title h1");

    @Step("Проверить открытие каталога по пути {expectedPath} с заголовком: {expectedHeading}")
    public CatalogPage shouldBeOpened(String expectedPath, String expectedHeading) {
        webdriver().shouldHave(url(Configuration.baseUrl + expectedPath));
        heading.shouldBe(visible).shouldHave(exactText(expectedHeading));
        return this;
    }
}
