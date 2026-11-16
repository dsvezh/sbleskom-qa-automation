package ru.sbleskom.web.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class HomePage {
    private final SelenideElement heading = $("main h1.heading");
    private final ElementsCollection menuItems = $(".header__menu").$$("a.header__menu-item");
    private final ElementsCollection sectionTitles = $("main").$$(".simple-title__title a, .centered-title__title a");

    @Step("Открыть главную страницу")
    public HomePage openHomePage() {
        open("/");
        return this;
    }

    @Step("Перейти в раздел «{sectionName}» через основное меню")
    public void openMenuSection(String sectionName) {
        menuItems.findBy(exactText(sectionName)).shouldBe(visible).click();
    }

    @Step("Проверить заголовок главной страницы: {expectedHeading}")
    public HomePage shouldHaveHeading(String expectedHeading) {
        heading.shouldBe(visible).shouldHave(exactText(expectedHeading));
        return this;
    }

    @Step("Проверить пункты основного меню")
    public HomePage shouldHaveMenuSections(String... sections) {
        for (String section : sections) {
            menuItems.findBy(exactText(section)).shouldBe(visible);
        }
        return this;
    }

    @Step("Проверить разделы главной страницы")
    public HomePage shouldHaveSections(String... sections) {
        for (String section : sections) {
            sectionTitles.findBy(exactText(section)).shouldBe(visible);
        }
        return this;
    }
}
