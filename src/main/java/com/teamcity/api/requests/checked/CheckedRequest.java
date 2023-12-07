package com.teamcity.api.requests.checked;

import com.teamcity.api.enums.Endpoint;
import com.teamcity.api.models.BaseModel;
import com.teamcity.api.requests.CrudInterface;
import com.teamcity.api.requests.Request;
import com.teamcity.api.requests.unchecked.UncheckedRequest;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;

public class CheckedRequest extends Request implements CrudInterface {

    private final Class<? extends BaseModel> modelClass;

    public CheckedRequest(RequestSpecification spec, Endpoint endpoint) {
        super(spec, endpoint.getUrl());
        this.modelClass = endpoint.getModelClass();
    }

    @Override
    public Object create(Object obj) {
        return new UncheckedRequest(spec, url)
                .create(obj)
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .extract().as(modelClass);
    }

    @Override
    public Object read(String id) {
        return new UncheckedRequest(spec, url)
                .read(id)
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .extract().as(modelClass);
    }

    @Override
    public Object update(String id, Object obj) {
        return new UncheckedRequest(spec, url)
                .update(id, obj)
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .extract().as(modelClass);
    }

    @Override
    public String delete(String id) {
        return new UncheckedRequest(spec, url)
                .delete(id)
                .then().assertThat().statusCode(HttpStatus.SC_NO_CONTENT)
                .extract().asString();
    }

}
