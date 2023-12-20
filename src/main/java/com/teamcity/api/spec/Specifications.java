package com.teamcity.api.spec;

import com.teamcity.api.config.Config;
import com.teamcity.api.models.User;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import java.util.List;

public final class Specifications {

    private static Specifications spec;

    private Specifications() {
    }

    public static Specifications getSpec() {
        if (spec == null) {
            spec = new Specifications();
        }
        return spec;
    }

    public RequestSpecification unauthSpec() {
        return reqBuilder()
                .setBaseUri("http://" + Config.getProperty("host"))
                .build();
    }

    public RequestSpecification authSpec(User user) {
        return reqBuilder()
                .setBaseUri("http://%s:%s@%s".formatted(user.getUsername(), user.getPassword(), Config.getProperty("host")))
                .build();
    }

    public RequestSpecification superUserSpec() {
        return reqBuilder()
                .setBaseUri("http://:%s@%s".formatted(Config.getProperty("superUserToken"), Config.getProperty("host")))
                .build();
    }

    private RequestSpecBuilder reqBuilder() {
        return new RequestSpecBuilder()
                // Фильтр для отображения реквестов и респонсов в Allure репорте
                .addFilters(List.of(new RequestLoggingFilter(), new ResponseLoggingFilter(), new AllureRestAssured()))
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON);
    }

}
