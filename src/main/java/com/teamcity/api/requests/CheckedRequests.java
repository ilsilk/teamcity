package com.teamcity.api.requests;

import com.teamcity.api.requests.checked.CheckedBuildTypes;
import com.teamcity.api.requests.checked.CheckedProjects;
import com.teamcity.api.requests.checked.CheckedUsers;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;

@Getter
public class CheckedRequests {

    private final CheckedProjects projectRequest;
    private final CheckedUsers userRequest;
    private final CheckedBuildTypes buildTypeRequest;

    public CheckedRequests(RequestSpecification spec) {
        projectRequest = new CheckedProjects(spec);
        userRequest = new CheckedUsers(spec);
        buildTypeRequest = new CheckedBuildTypes(spec);
    }

}
