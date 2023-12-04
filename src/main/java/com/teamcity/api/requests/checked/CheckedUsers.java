package com.teamcity.api.requests.checked;

import com.teamcity.api.models.User;
import com.teamcity.api.requests.CrudInterface;
import com.teamcity.api.requests.Request;
import com.teamcity.api.requests.unchecked.UncheckedUsers;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;

public class CheckedUsers extends Request implements CrudInterface {

    public CheckedUsers(RequestSpecification spec) {
        super(spec);
    }

    @Override
    public User create(Object obj) {
        return new UncheckedUsers(spec)
                .create(obj)
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .extract().as(User.class);
    }

    @Override
    public User read(String id) {
        return new UncheckedUsers(spec)
                .read(id)
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .extract().as(User.class);
    }

    @Override
    public User update(String id, Object obj) {
        return new UncheckedUsers(spec)
                .update(id, obj)
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .extract().as(User.class);
    }

    @Override
    public String delete(String id) {
        return new UncheckedUsers(spec)
                .delete(id)
                .then().assertThat().statusCode(HttpStatus.SC_NO_CONTENT)
                .extract().asString();
    }
}
