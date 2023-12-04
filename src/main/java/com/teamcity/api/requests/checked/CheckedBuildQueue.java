package com.teamcity.api.requests.checked;

import com.teamcity.api.models.Build;
import com.teamcity.api.requests.CrudInterface;
import com.teamcity.api.requests.Request;
import com.teamcity.api.requests.unchecked.UncheckedBuildQueue;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;

public class CheckedBuildQueue extends Request implements CrudInterface {

    public CheckedBuildQueue(RequestSpecification spec) {
        super(spec);
    }

    @Override
    public Build create(Object obj) {
        return new UncheckedBuildQueue(spec)
                .create(obj)
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .extract().as(Build.class);
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
