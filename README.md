# sbleskom-qa-automation

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
фиксирует первую загруженную порцию карточек до прокрутки и проверяет у каждой видимые непустое название,
загруженное изображение или видимое видео с загруженным кадром без ошибок
(`readyState >= 2`, `videoWidth > 0`, `error == null`). Цена проверяется целиком:
положительное целое число и валюта `руб`; ожидаемый формат передаётся из теста.
Для загрузки медиа тест прокручивает страницу к каждой зафиксированной карточке;
подгруженные при прокрутке товары в проверку не включаются.

```powershell
.\gradlew.bat test --tests ru.sbleskom.web.tests.CatalogPageTest.catalogShouldShowProducts "-Dselenide.browser=chrome" "-DremoteUrl="
```

Скриншот, исходный HTML, логи и существующее условное видео сохраняет `TestBase`.
Локальный запуск видео не создаёт; удалённый запуск для проверки этого теста не требуется.

