package com.teamcity.api.requests.checked;

import com.teamcity.api.enums.Endpoint;
import com.teamcity.api.generators.TestDataStorage;
import com.teamcity.api.models.BaseModel;
import com.teamcity.api.requests.CrudInterface;
import com.teamcity.api.requests.Request;
import com.teamcity.api.requests.unchecked.UncheckedRequest;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;

public class CheckedRequest extends Request implements CrudInterface {

    public CheckedRequest(RequestSpecification spec, Endpoint endpoint) {
        super(spec, endpoint);
    }

    @Override
    public BaseModel create(Object obj) {
        var model = new UncheckedRequest(spec, endpoint)
                .create(obj)
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .extract().as(endpoint.getModelClass());
        return TestDataStorage.getStorage().addCreatedEntity(endpoint, model);
    }

    @Override
    public BaseModel read(String id) {
        return new UncheckedRequest(spec, endpoint)
                .read(id)
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .extract().as(endpoint.getModelClass());
    }

    @Override
    public BaseModel update(String id, Object obj) {
        return new UncheckedRequest(spec, endpoint)
                .update(id, obj)
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .extract().as(endpoint.getModelClass());
    }

    @Override
    public String delete(String id) {
        return new UncheckedRequest(spec, endpoint)
                .delete(id)
                .then().assertThat().statusCode(HttpStatus.SC_NO_CONTENT)
                .extract().asString();
    }

}
