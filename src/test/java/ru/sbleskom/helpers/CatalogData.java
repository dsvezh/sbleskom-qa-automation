package ru.sbleskom.helpers;

import com.codeborne.selenide.Configuration;
import org.openqa.selenium.json.Json;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.codeborne.selenide.WebDriverRunner.getWebDriver;

/** Тот же публичный источник вариантов, который использует Products.initInstance магазина. */
public class CatalogData {
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> variants(String productId) {
        if (!productId.matches("\\d+")) {
            throw new AssertionError("Некорректный ID первого товара: " + productId);
        }
        String cookies = getWebDriver().manage().getCookies().stream()
                .map(cookie -> cookie.getName() + "=" + cookie.getValue())
                .collect(Collectors.joining("; "));
        HttpRequest request = HttpRequest.newBuilder(URI.create(Configuration.baseUrl)
                        .resolve("/products_by_id/" + productId + ".json"))
                .timeout(Duration.ofSeconds(30))
                .header("Cookie", cookies).header("Accept", "application/json").GET().build();
        try {
            HttpResponse<String> response = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(30)).build()
                    .send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new AssertionError("Ошибка подготовки данных: источник вариантов вернул HTTP "
                        + response.statusCode());
            }
            Map<String, Object> body = new Json().toType(response.body(), Map.class);
            List<Map<String, Object>> products = (List<Map<String, Object>>) body.get("products");
            if (products == null || products.size() != 1
                    || !productId.equals(products.get(0).get("id").toString())) {
                throw new AssertionError("Ошибка подготовки данных: не получен первый товар " + productId);
            }
            return (List<Map<String, Object>>) products.get(0).get("variants");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new AssertionError("Прервано получение вариантов первого товара", e);
        } catch (IOException | RuntimeException e) {
            throw new AssertionError("Ошибка подготовки данных: не удалось получить варианты " + productId, e);
        }
    }
}
