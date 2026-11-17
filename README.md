# sbleskom-qa-automation

Тест `CatalogPageTest.firstProductShouldBeAddedToCart` открывает `/collection/all`
и нажимает «В корзину» у первого товара. Ожидает добавления товара или появления
уточняющего окна. Если окно появилось, повторно нажимает «В корзину», сохраняя
параметры по умолчанию. В корзине проверяет единственную позицию:
ID товара, точное название и количество `1`.
Используются Page Object `CatalogPage` и `CartPage`.

```powershell
.\gradlew.bat test --tests ru.sbleskom.web.tests.CatalogPageTest.firstProductShouldBeAddedToCart "-Dselenide.browser=chrome" "-DremoteUrl="
```

Тест `CatalogPageTest.subcategoryShouldOpenFromCatalog` открывает каталог,
раскрывает раздел «Кольца» нажатием на стрелку и выбирает «Обручальные кольца».
Проверяет адрес `/collection/svadebnye-koltsa`, видимый заголовок «Обручальные кольца»
и наличие видимых карточек товаров в открытой подкатегории.

```powershell
.\gradlew.bat test --tests ru.sbleskom.web.tests.CatalogPageTest.subcategoryShouldOpenFromCatalog "-Dselenide.browser=chrome" "-DremoteUrl="
```

Тест `CatalogPageTest.catalogShouldAppendProductsOnScroll` сохраняет ID товаров,
прокручивает страницу вниз и ожидает увеличения числа товаров. Затем проверяет,
что появились новые ID, все прежние сохранились, ID непустые и не повторяются.
ID берутся из кнопок `.js-card-add-to-cart[data-product-id]` внутри карточек:
скрипт подгрузки создаёт новые обёртки карточек без атрибута `data-product-id`.

```powershell
.\gradlew.bat test --tests ru.sbleskom.web.tests.CatalogPageTest.catalogShouldAppendProductsOnScroll "-Dselenide.browser=chrome" "-DremoteUrl="
```

Базовый Java-проект для автоматизации тестирования сайта https://sbleskom.ru.

Стек: Java 17, Gradle, JUnit 5.

Тест `ru.sbleskom.web.tests.HomePageTest` открывает главную страницу и проверяет
видимость заголовка, пунктов меню «Каталог», «Новинки», «Магазины», «Блог SBLESKOM»
и разделов «Новинки», «Новости», «Каталог», «Для особых случаев», «Команда».

Тест `catalogShouldOpenFromHomePage` нажимает «Каталог» в основном меню главной
страницы и проверяет адрес `/collection/all` и видимый заголовок «Каталог».

Отдельные тесты проверяют переходы через то же меню в «Новинки»
(`/collection/novinki-2`), «Магазины» (`/page/address`) и «Блог SBLESKOM»
(`/page/blog-index`). Каждый проверяет адрес и видимый заголовок раздела.

Запуск только теста перехода:

```powershell
.\gradlew.bat test --tests ru.sbleskom.web.tests.HomePageTest.catalogShouldOpenFromHomePage "-Dselenide.browser=chrome" "-DremoteUrl="
```

```powershell
.\gradlew.bat test --tests ru.sbleskom.web.tests.HomePageTest "-Dselenide.browser=chrome" "-DremoteUrl="
```

Используется Page Object: меню главной страницы описано в `HomePage`,
проверки разделов — в `CatalogPage`, `ShopsPage` и `BlogPage`.
«Новинки» используют `CatalogPage`, поскольку имеют ту же структуру каталога.
Названия пунктов меню, ожидаемые пути и заголовки передаются из тестов.

Тест `CatalogPageTest.catalogShouldShowProducts` открывает каталог напрямую,
явно фиксирует первую загруженную порцию карточек через `captureInitialProducts()`
до прокрутки и проверяет у каждой видимые непустое название,
загруженное изображение или видимое видео с загруженным кадром без ошибок
(`readyState >= 2`, `videoWidth > 0`, `error == null`). Цена проверяется целиком:
положительное целое число и валюта `руб`; ожидаемый формат передаётся из теста.
Для загрузки медиа тест прокручивает страницу к каждой зафиксированной карточке;
подгруженные при прокрутке товары в проверку не включаются.
Проверки наличия, названий, медиа и цен напрямую используют сохранённую порцию.
Перед ними в тесте вызывается `captureInitialProducts()`.
При открытии каталога или подкатегории порция сбрасывается.

```powershell
.\gradlew.bat test --tests ru.sbleskom.web.tests.CatalogPageTest.catalogShouldShowProducts "-Dselenide.browser=chrome" "-DremoteUrl="
```

Скриншот, исходный HTML, логи и существующее условное видео сохраняет `TestBase`.
Локальный запуск видео не создаёт; удалённый запуск для проверки этого теста не требуется.

Тест `CatalogPageTest.productShouldOpenByTitle` сохраняет название и адрес первого
товара каталога, нажимает на его название и проверяет адрес открытой карточки
и точное совпадение видимого названия с названием в каталоге.

Тест `CatalogPageTest.productShouldOpenByMedia` выполняет те же проверки после
нажатия на видимую область изображения или видео первого товара.

```powershell
.\gradlew.bat test --tests ru.sbleskom.web.tests.CatalogPageTest.productShouldOpenByTitle "-Dselenide.browser=chrome" "-DremoteUrl="
```

```powershell
.\gradlew.bat test --tests ru.sbleskom.web.tests.CatalogPageTest.productShouldOpenByMedia "-Dselenide.browser=chrome" "-DremoteUrl="
```

