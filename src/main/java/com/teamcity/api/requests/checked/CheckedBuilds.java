package com.teamcity.api.requests.checked;

import com.teamcity.api.models.Build;
import com.teamcity.api.requests.CrudInterface;
import com.teamcity.api.requests.Request;
import com.teamcity.api.requests.unchecked.UncheckedBuilds;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;

public class CheckedBuilds extends Request implements CrudInterface {

    public CheckedBuilds(RequestSpecification spec) {
        super(spec);
    }

    @Override
    public Object create(Object obj) {
        return null;
    }

    @Override
    public Build read(String id) {
        return new UncheckedBuilds(spec)
                .read(id)
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .extract().as(Build.class);
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
