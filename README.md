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

Скриншот, исходный HTML, логи и существующее условное видео сохраняет `TestBase`.
Локальный запуск видео не создаёт; удалённый запуск для проверки этого теста не требуется.

