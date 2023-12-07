package com.teamcity.api.requests.unchecked;

import com.teamcity.api.enums.Endpoint;
import com.teamcity.api.requests.CrudInterface;
import com.teamcity.api.requests.Request;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class UncheckedRequest extends Request implements CrudInterface {

    public UncheckedRequest(RequestSpecification spec, String url) {
        super(spec, url);
    }

    public UncheckedRequest(RequestSpecification spec, Endpoint endpoint) {
        this(spec, endpoint.getUrl());
    }

    @Override
    public Response create(Object obj) {
        return RestAssured.given()
                .spec(spec)
                .body(obj)
                .post(url);
    }

    @Override
    public Response read(String id) {
        return RestAssured.given()
                .spec(spec)
                .get(url + "/id:" + id);
    }

    @Override
    public Response update(String id, Object obj) {
        return RestAssured.given()
                .spec(spec)
                .body(obj)
                .put(url + "/id:" + id);
    }

    @Override
    public Response delete(String id) {
        return RestAssured.given()
                .spec(spec)
                .delete(url + "/id:" + id);
    }

}
