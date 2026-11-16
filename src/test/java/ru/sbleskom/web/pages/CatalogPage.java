package ru.sbleskom.web.pages;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebElementCondition;
import io.qameta.allure.Step;

import java.util.HashSet;
import java.util.List;

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
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CatalogPage {
    private final SelenideElement heading = $(".catalog__title h1");
    private final SelenideElement footerBottom = $(".footer_last");
    private final ElementsCollection products = $(".catalog__list").$$(".catalog__item[data-product-id]");
    private final ElementsCollection productButtons = $(".catalog__list")
            .$$(".catalog__item .js-card-add-to-cart[data-product-id]");
    private final String productTitle = ".catalog__item-title";
    private final String productMediaArea = ".card-images__box .slider__by-hover-item";
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

    @Step("Получить название первого товара каталога")
    public String getFirstProductTitle() {
        return products.first().$(productTitle).shouldBe(visible).shouldHave(matchText("\\S")).text();
    }

    @Step("Получить адрес первого товара каталога")
    public String getFirstProductUrl() {
        return products.first().$(productTitle).closest("a").getAttribute("href");
    }

    @Step("Сохранить ID загруженных товаров каталога")
    public List<String> getProductIds() {
        return productButtons.shouldHave(sizeGreaterThan(0)).attributes("data-product-id");
    }

    @Step("Прокрутить каталог до следующей порции товаров")
    public CatalogPage scrollToNextProducts() {
        footerBottom.scrollTo();
        return this;
    }

    @Step("Проверить появление новых товаров, сохранение прежних и уникальность ID")
    public CatalogPage shouldAppendProducts(List<String> initialIds) {
        productButtons.shouldHave(sizeGreaterThan(initialIds.size()));
        List<String> actualIds = getProductIds();
        assertAll(
                () -> assertTrue(actualIds.stream().anyMatch(id -> !initialIds.contains(id)),
                        "Должны появиться товары с новыми ID"),
                () -> assertTrue(actualIds.containsAll(initialIds),
                        "Ранее загруженные товары должны остаться в каталоге"),
                () -> assertTrue(actualIds.stream().allMatch(id -> id != null && !id.isBlank()),
                        "ID каждого товара должен быть непустым"),
                () -> assertEquals(actualIds.size(), new HashSet<>(actualIds).size(),
                        "ID товаров не должны повторяться")
        );
        return this;
    }

    @Step("Открыть первый товар нажатием на название")
    public ProductPage openFirstProductByTitle() {
        products.first().$(productTitle).shouldBe(visible).click();
        return new ProductPage();
    }

    @Step("Открыть первый товар нажатием на изображение или видео")
    public void openFirstProductByMedia() {
        products.first().$$(productMediaArea).findBy(visible).click();
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
