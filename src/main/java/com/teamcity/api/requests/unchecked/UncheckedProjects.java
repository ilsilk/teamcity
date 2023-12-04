package com.teamcity.api.requests.unchecked;

import com.teamcity.api.requests.CrudInterface;
import com.teamcity.api.requests.Request;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class UncheckedProjects extends Request implements CrudInterface {

    private static final String PROJECT_ENDPOINT = "/app/rest/projects";

    public UncheckedProjects(RequestSpecification spec) {
        super(spec);
    }

    @Override
    public Response create(Object obj) {
        return RestAssured.given()
                .spec(spec)
                .body(obj)
                .post(PROJECT_ENDPOINT);
    }

    @Override
    public Response read(String id) {
        return RestAssured.given()
                .spec(spec)
                .get(PROJECT_ENDPOINT + "/id:" + id);
    }

    @Override
    public Response update(String id, Object obj) {
        return RestAssured.given()
                .spec(spec)
                .body(obj)
                .put(PROJECT_ENDPOINT + "/id:" + id);
    }

    @Override
    public Response delete(String id) {
        return RestAssured.given()
                .spec(spec)
                .delete(PROJECT_ENDPOINT + "/id:" + id);
    }

}
