package com.teamcity.api.requests.checked;

import com.teamcity.api.models.BuildType;
import com.teamcity.api.requests.CrudInterface;
import com.teamcity.api.requests.Request;
import com.teamcity.api.requests.unchecked.UncheckedBuildTypes;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;

public class CheckedBuildTypes extends Request implements CrudInterface {

    public CheckedBuildTypes(RequestSpecification spec) {
        super(spec);
    }

    @Override
    public BuildType create(Object obj) {
        return new UncheckedBuildTypes(spec)
                .create(obj)
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .extract().as(BuildType.class);
    }

    @Override
    public BuildType read(String id) {
        return new UncheckedBuildTypes(spec)
                .read(id)
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .extract().as(BuildType.class);
    }

    @Override
    public BuildType update(String id, Object obj) {
        return new UncheckedBuildTypes(spec)
                .update(id, obj)
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .extract().as(BuildType.class);
    }

    @Override
    public String delete(String id) {
        return new UncheckedBuildTypes(spec)
                .delete(id)
                .then().assertThat().statusCode(HttpStatus.SC_NO_CONTENT)
                .extract().asString();
    }

}
