package com.teamcity.api.requests;

import io.restassured.specification.RequestSpecification;

public class Request {

    protected final RequestSpecification spec;
    protected final String url;

    public Request(RequestSpecification spec, String url) {
        this.spec = spec;
        this.url = url;
    }

}
