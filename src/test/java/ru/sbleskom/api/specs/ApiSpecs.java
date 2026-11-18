package ru.sbleskom.api.specs;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.http.ContentType.JSON;

public final class ApiSpecs {
    private static final String BASE_URI = System.getProperty("apiBaseUrl",
            System.getProperty("baseUrl", "https://sbleskom.ru"));

    static {
        RestAssured.baseURI = BASE_URI;
    }

    private ApiSpecs() {
    }

    public static String baseUri() {
        return BASE_URI;
    }

    public static RequestSpecification request() {
        return new RequestSpecBuilder()
                .setBaseUri(BASE_URI)
                .setAccept(JSON)
                .addFilter(new AllureRestAssured()
                        .setRequestTemplate("request.ftl")
                        .setResponseTemplate("response.ftl"))
                .build();
    }

    public static ResponseSpecification jsonResponse() {
        return new ResponseSpecBuilder()
                .expectContentType(JSON)
                .build();
    }
}
