package com.teamcity.api.requests.unchecked;

import com.teamcity.api.requests.CrudInterface;
import com.teamcity.api.requests.Request;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class UncheckedBuildQueue extends Request implements CrudInterface {

    private static final String BUILD_QUEUE_ENDPOINT = "/app/rest/buildQueue";

    public UncheckedBuildQueue(RequestSpecification spec) {
        super(spec);
    }

    @Override
    public Response create(Object obj) {
        return RestAssured.given()
                .spec(spec)
                .body(obj)
                .post(BUILD_QUEUE_ENDPOINT);
    }

    @Override
    public Object read(String id) {
        return null;
    }

    @Override
    public Object update(String id, Object obj) {
        return null;
    }

    @Override
    public Object delete(String id) {
        return null;
    }

}
