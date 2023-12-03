package com.teamcity.api.requests;

import com.teamcity.api.requests.checked.CheckedBuildType;
import com.teamcity.api.requests.checked.CheckedProject;
import com.teamcity.api.requests.checked.CheckedUser;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;

@Getter
public class CheckedRequests {

    private final CheckedProject projectRequest;
    private final CheckedUser userRequest;
    private final CheckedBuildType buildTypeRequest;

    public CheckedRequests(RequestSpecification spec) {
        projectRequest = new CheckedProject(spec);
        userRequest = new CheckedUser(spec);
        buildTypeRequest = new CheckedBuildType(spec);
    }

}
