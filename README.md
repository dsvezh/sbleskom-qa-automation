# sokolov-qa-automation

Базовый Java-проект для автоматизации тестирования сайта https://sokolov.ru.

Стек: Java 17, Gradle, JUnit 5.

## Структура

```text
src/main/java/ru/sokolov/          — общий код проекта
src/main/resources/               — ресурсы общего кода
src/test/java/ru/sokolov/
├── tests/                        — тесты
├── pages/                        — Page Object страниц сайта
├── components/                   — общие компоненты страниц
└── utils/                        — вспомогательный код
src/test/resources/               — тестовые данные и ресурсы
```

## Запуск

Установить JDK 17 и настроить `JAVA_HOME`.

Windows PowerShell:

```powershell
.\gradlew.bat clean test
```

Linux / macOS:

```bash
sh gradlew clean test
```

При первом запуске Gradle и зависимости скачиваются из интернета.
HTML-отчёт: `build/reports/tests/test/index.html`.

Адрес сайта доступен тестам через `System.getProperty("baseUrl")`.
По умолчанию используется `https://sokolov.ru`. Переопределение:

```powershell
.\gradlew.bat '-DbaseUrl=https://sokolov.ru' test
```

`ProjectSetupTest` проверяет запуск JUnit. Браузерные тесты и библиотека
управления браузером пока не добавлены.
