package ru.sbleskom.api.specs;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;

import static io.restassured.http.ContentType.JSON;

public final class BaseSpec {
    public static final String BASE_URI = System.getProperty("baseUrl", "https://sbleskom.ru");

    public static RequestSpecification request() {
        return new RequestSpecBuilder()
                .setBaseUri(BASE_URI)
                .setAccept(JSON)
                .addFilter(new AllureRestAssured()
                        .setRequestTemplate("request.ftl")
                        .setResponseTemplate("response.ftl"))
                .build();
    }

    public static ResponseSpecBuilder successfulJsonResponse() {
        return new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType(JSON);
    }
}
