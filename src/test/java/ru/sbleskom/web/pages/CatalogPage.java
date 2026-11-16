package ru.sbleskom.web.pages;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebElementCondition;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.image;
import static com.codeborne.selenide.Condition.match;
import static com.codeborne.selenide.Condition.matchText;
import static com.codeborne.selenide.Condition.or;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.webdriver;
import static com.codeborne.selenide.WebDriverConditions.url;

public class CatalogPage {
    private final SelenideElement heading = $(".catalog__title h1");
    private final ElementsCollection products = $(".catalog__list").$$(".catalog__item[data-product-id]");
    private final String productTitle = ".catalog__item-title";
    private final String productMedia = ".card-images__box img, .catalog__item-images video";
    private final String productPrice = ".catalog__item-price";
    private final WebElementCondition loadedVideo = match("видео с загруженным кадром без ошибок", element -> {
        String readyState = element.getDomProperty("readyState");
        String videoWidth = element.getDomProperty("videoWidth");
        return "video".equals(element.getTagName())
                && readyState != null && readyState.matches("[234]")
                && videoWidth != null && videoWidth.matches("[1-9][0-9]*")
                && element.getDomProperty("error") == null;
    });
    private ElementsCollection initialProducts;

    @Step("Открыть каталог")
    public CatalogPage openCatalogPage() {
        open("/collection/all");
        initialProducts = products.shouldHave(sizeGreaterThan(0)).snapshot();
        return this;
    }

    @Step("Проверить наличие товаров в каталоге")
    public CatalogPage shouldShowProducts() {
        initialProducts.shouldHave(sizeGreaterThan(0));
        for (SelenideElement product : initialProducts) {
            product.shouldBe(visible);
        }
        return this;
    }

    @Step("Проверить непустые названия всех товаров первой загруженной порции")
    public CatalogPage shouldShowProductTitles() {
        for (SelenideElement product : initialProducts) {
            product.$(productTitle).shouldBe(visible).shouldHave(matchText("\\S"));
        }
        return this;
    }

    @Step("Проверить изображения или видео всех товаров первой загруженной порции")
    public CatalogPage shouldShowProductMedia() {
        for (SelenideElement product : initialProducts) {
            product.scrollIntoView("{block: 'center'}");
            product.$$(productMedia).findBy(visible)
                    .shouldBe(or("загруженное изображение или видео", image, loadedVideo));
        }
        return this;
    }

    @Step("Проверить цены всех товаров первой загруженной порции по формату: {expectedPricePattern}")
    public CatalogPage shouldShowProductPrices(String expectedPricePattern) {
        for (SelenideElement product : initialProducts) {
            product.$(productPrice).shouldBe(visible).shouldHave(matchText(expectedPricePattern));
        }
        return this;
    }

    @Step("Проверить открытие каталога по пути {expectedPath} с заголовком: {expectedHeading}")
    public CatalogPage shouldBeOpened(String expectedPath, String expectedHeading) {
        webdriver().shouldHave(url(Configuration.baseUrl + expectedPath));
        heading.shouldBe(visible).shouldHave(exactText(expectedHeading));
        return this;
    }
}
