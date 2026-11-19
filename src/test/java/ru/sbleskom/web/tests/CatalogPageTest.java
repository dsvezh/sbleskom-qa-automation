package ru.sbleskom.web.tests;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import ru.sbleskom.web.pages.CatalogPage;
import ru.sbleskom.web.pages.ProductPage;

import java.util.List;

import static io.qameta.allure.SeverityLevel.CRITICAL;

@Epic("SBLESKOM")
@Feature("Каталог")
@Tag("ui")
@DisplayName("Каталог")
public class CatalogPageTest extends TestBase {
    private final CatalogPage catalogPage = new CatalogPage();
    private final ProductPage productPage = new ProductPage();

    @Test
    @Story("Добавление товара в корзину из каталога")
    @DisplayName("Первый товар каталога добавляется в корзину с параметрами по умолчанию")
    @Owner("d.svezhov")
    @Severity(CRITICAL)
    void firstProductShouldBeAddedToCart() {
        catalogPage.openCatalogPage()
                .shouldBeOpened("/collection/all", "Каталог");
        String expectedTitle = catalogPage.getFirstProductTitle();
        String expectedProductId = catalogPage.getFirstProductId();

        catalogPage.addFirstProductToCart()
                .shouldHaveCartOpened("Корзина")
                .shouldHaveProductInCart(expectedProductId, expectedTitle, 1, 1);
    }

    @Test
    @Story("Переход в подкатегорию из каталога")
    @DisplayName("Из каталога открывается выбранная подкатегория с товарами")
    @Owner("d.svezhov")
    @Severity(CRITICAL)
    void subcategoryShouldOpenFromCatalog() {
        catalogPage.openCatalogPage()
                .openSubcategory("Кольца", "Обручальные кольца")
                .shouldBeOpened("/collection/svadebnye-koltsa", "Обручальные кольца")
                .captureInitialProducts()
                .shouldShowProducts()
                .shouldShowProductTitles()
                .shouldShowProductMedia()
                .shouldShowProductPrices("^[1-9][0-9]* руб$");
    }

    @Test
    @Story("Подгрузка товаров при прокрутке каталога")
    @DisplayName("Прокрутка добавляет новые товары, сохраняет прежние и не дублирует ID")
    @Owner("d.svezhov")
    @Severity(CRITICAL)
    void catalogShouldAppendProductsOnScroll() {
        catalogPage.openCatalogPage();
        List<String> initialIds = catalogPage.getProductIds();

        catalogPage.scrollToNextProducts()
                .shouldAppendProducts(initialIds);
    }

    @Test
    @Story("Переход в карточку товара из каталога")
    @DisplayName("Нажатие на название открывает карточку выбранного товара с совпадающим названием")
    @Owner("d.svezhov")
    @Severity(CRITICAL)
    void productShouldOpenByTitle() {
        catalogPage.openCatalogPage();
        String expectedTitle = catalogPage.getFirstProductTitle();
        String expectedUrl = catalogPage.getFirstProductUrl();

        catalogPage.openFirstProductByTitle();

        productPage.shouldBeOpened(expectedUrl)
                .shouldHaveTitle(expectedTitle);
    }

    @Test
    @Story("Переход в карточку товара из каталога")
    @DisplayName("Нажатие на изображение или видео открывает карточку того же товара")
    @Owner("d.svezhov")
    @Severity(CRITICAL)
    void productShouldOpenByMedia() {
        catalogPage.openCatalogPage();
        String expectedTitle = catalogPage.getFirstProductTitle();
        String expectedUrl = catalogPage.getFirstProductUrl();

        catalogPage.openFirstProductByMedia();

        productPage.shouldBeOpened(expectedUrl)
                .shouldHaveTitle(expectedTitle);
    }

    @Test
    @Story("Отображение товаров в каталоге")
    @DisplayName("Все товары первой загруженной порции имеют название, изображение или видео и цену")
    @Owner("d.svezhov")
    @Severity(CRITICAL)
    void catalogShouldShowProducts() {
        catalogPage.openCatalogPage()
                .captureInitialProducts()
                .shouldShowProducts()
                .shouldShowProductTitles()
                .shouldShowProductMedia()
                .shouldShowProductPrices("^[1-9][0-9]* руб$");
    }
}
