package com.teamcity.api.requests.unchecked;

import com.teamcity.api.requests.CrudInterface;
import com.teamcity.api.requests.Request;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class UncheckedBuilds extends Request implements CrudInterface {

    private static final String BUILDS_ENDPOINT = "/app/rest/builds";

    public UncheckedBuilds(RequestSpecification spec) {
        super(spec);
    }

    @Override
    public Object create(Object obj) {
        return null;
    }

    @Override
    public Response read(String id) {
        return RestAssured.given()
                .spec(spec)
                .get(BUILDS_ENDPOINT + "/id:" + id);
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
