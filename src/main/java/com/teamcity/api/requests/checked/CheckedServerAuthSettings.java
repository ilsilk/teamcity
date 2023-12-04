package com.teamcity.api.requests.checked;

import com.teamcity.api.models.ServerAuthSettings;
import com.teamcity.api.requests.CrudInterface;
import com.teamcity.api.requests.Request;
import com.teamcity.api.requests.unchecked.UncheckedServerAuthSettings;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;

public class CheckedServerAuthSettings extends Request implements CrudInterface {

    public CheckedServerAuthSettings(RequestSpecification spec) {
        super(spec);
    }

    @Override
    public Object create(Object obj) {
        return null;
    }

    @Override
    public ServerAuthSettings read(String id) {
        return new UncheckedServerAuthSettings(spec)
                .read(id)
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .extract().as(ServerAuthSettings.class);
    }

    @Override
    public ServerAuthSettings update(String id, Object obj) {
        return new UncheckedServerAuthSettings(spec)
                .update(id, obj)
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .extract().as(ServerAuthSettings.class);
    }

    @Override
    public Object delete(String id) {
        return null;
    }

}
