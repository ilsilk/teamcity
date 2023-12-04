package com.teamcity.api.requests.unchecked;

import com.teamcity.api.requests.CrudInterface;
import com.teamcity.api.requests.Request;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class UncheckedUsers extends Request implements CrudInterface {

    private static final String USER_ENDPOINT = "/app/rest/users";

    public UncheckedUsers(RequestSpecification spec) {
        super(spec);
    }

    @Override
    public Response create(Object obj) {
        return RestAssured.given()
                .spec(spec)
                .body(obj)
                .post(USER_ENDPOINT);
    }

    @Override
    public Response read(String id) {
        return RestAssured.given()
                .spec(spec)
                .get(USER_ENDPOINT + "/username:" + id);
    }

    @Override
    public Response update(String id, Object obj) {
        return RestAssured.given()
                .spec(spec)
                .body(obj)
                .put(USER_ENDPOINT + "/username:" + id);
    }

    @Override
    public Response delete(String id) {
        return RestAssured.given()
                .spec(spec)
                .delete(USER_ENDPOINT + "/username:" + id);
    }

}
