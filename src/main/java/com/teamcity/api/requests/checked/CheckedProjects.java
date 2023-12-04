package com.teamcity.api.requests.checked;

import com.teamcity.api.models.Project;
import com.teamcity.api.requests.CrudInterface;
import com.teamcity.api.requests.Request;
import com.teamcity.api.requests.unchecked.UncheckedProjects;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;

public class CheckedProjects extends Request implements CrudInterface {

    public CheckedProjects(RequestSpecification spec) {
        super(spec);
    }

    @Override
    public Project create(Object obj) {
        return new UncheckedProjects(spec)
                .create(obj)
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .extract().as(Project.class);
    }

    @Override
    public Project read(String id) {
        return new UncheckedProjects(spec)
                .read(id)
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .extract().as(Project.class);
    }

    @Override
    public Project update(String id, Object obj) {
        return new UncheckedProjects(spec)
                .update(id, obj)
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .extract().as(Project.class);
    }

    @Override
    public String delete(String id) {
        return new UncheckedProjects(spec)
                .delete(id)
                .then().assertThat().statusCode(HttpStatus.SC_NO_CONTENT)
                .extract().asString();
    }

}
