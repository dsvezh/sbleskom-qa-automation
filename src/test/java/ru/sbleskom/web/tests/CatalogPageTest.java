package ru.sbleskom.web.tests;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.sbleskom.web.pages.CatalogPage;

import static io.qameta.allure.SeverityLevel.CRITICAL;

@Epic("SBLESKOM")
@Feature("Каталог")
@DisplayName("Каталог")
public class CatalogPageTest extends TestBase {

    @Test
    @Story("Отображение товаров в каталоге")
    @DisplayName("Все товары первой загруженной порции имеют название, изображение или видео и цену")
    @Owner("d.svezhov")
    @Severity(CRITICAL)
    void catalogShouldShowProducts() {
        new CatalogPage()
                .openCatalogPage()
                .shouldShowProducts()
                .shouldShowProductTitles()
                .shouldShowProductMedia()
                .shouldShowProductPrices("^[1-9][0-9]* руб$");
    }
}
