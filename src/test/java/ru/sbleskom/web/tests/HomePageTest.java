package ru.sbleskom.web.tests;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.sbleskom.web.pages.BlogPage;
import ru.sbleskom.web.pages.CatalogPage;
import ru.sbleskom.web.pages.HomePage;
import ru.sbleskom.web.pages.ShopsPage;

import static io.qameta.allure.SeverityLevel.CRITICAL;

@Epic("SBLESKOM")
@Feature("Главная страница")
@DisplayName("Главная страница")
public class HomePageTest extends TestBase {
    private final HomePage homePage = new HomePage();
    private final CatalogPage catalogPage = new CatalogPage();
    private final ShopsPage shopsPage = new ShopsPage();
    private final BlogPage blogPage = new BlogPage();

    @Test
    @Story("Наличие основных разделов главной страницы")
    @DisplayName("Главная страница содержит основные разделы")
    @Owner("d.svezhov")
    @Severity(CRITICAL)
    void mainSectionsShouldBeVisible() {
        homePage.openHomePage()
                .shouldHaveHeading("Серебряные украшения SBLESKOM")
                .shouldHaveMenuSections("Каталог", "Новинки", "Магазины", "Блог SBLESKOM")
                .shouldHaveSections("Новинки", "НОВОСТИ", "Каталог", "Для особых случаев", "КОМАНДА");
    }

    @Test
    @Story("Переход в каталог из главной страницы")
    @DisplayName("Переход в каталог через основное меню главной страницы")
    @Owner("d.svezhov")
    @Severity(CRITICAL)
    void catalogShouldOpenFromHomePage() {
        homePage.openHomePage()
                .openMenuSection("Каталог");

        catalogPage.shouldBeOpened("/collection/all", "Каталог");
    }

    @Test
    @Story("Переход в новинки из главной страницы")
    @DisplayName("Переход в новинки через основное меню главной страницы")
    @Owner("d.svezhov")
    @Severity(CRITICAL)
    void newArrivalsShouldOpenFromHomePage() {
        homePage.openHomePage()
                .openMenuSection("Новинки");

        catalogPage.shouldBeOpened("/collection/novinki-2", "Новинки");
    }

    @Test
    @Story("Переход в магазины из главной страницы")
    @DisplayName("Переход в магазины через основное меню главной страницы")
    @Owner("d.svezhov")
    @Severity(CRITICAL)
    void shopsShouldOpenFromHomePage() {
        homePage.openHomePage()
                .openMenuSection("Магазины");

        shopsPage.shouldBeOpened("/page/address", "Магазины");
    }

    @Test
    @Story("Переход в блог из главной страницы")
    @DisplayName("Переход в блог SBLESKOM через основное меню главной страницы")
    @Owner("d.svezhov")
    @Severity(CRITICAL)
    void blogShouldOpenFromHomePage() {
        homePage.openHomePage()
                .openMenuSection("Блог SBLESKOM");

        blogPage.shouldBeOpened("/page/blog-index", "Блог SBLESKOM");
    }
}
