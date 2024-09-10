package com.teamcity.api.requests.unchecked;

import com.teamcity.api.enums.Endpoint;
import com.teamcity.api.requests.Request;
import com.teamcity.api.requests.SearchInterface;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public final class UncheckedSearch extends Request implements SearchInterface {

    public UncheckedSearch(RequestSpecification spec, Endpoint endpoint) {
        super(spec, endpoint);
    }

    @Override
    @Step("Search models")
    public Response search() {
        return RestAssured.given()
                .spec(spec)
                .get(endpoint.getUrl());
    }

}
