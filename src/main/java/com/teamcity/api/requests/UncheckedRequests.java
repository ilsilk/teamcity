package com.teamcity.api.requests;

import com.teamcity.api.requests.unchecked.UncheckedBuildTypes;
import com.teamcity.api.requests.unchecked.UncheckedProjects;
import com.teamcity.api.requests.unchecked.UncheckedUsers;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;

@Getter
public class UncheckedRequests {

    private final UncheckedProjects projectRequest;
    private final UncheckedUsers userRequest;
    private final UncheckedBuildTypes buildTypeRequest;

    public UncheckedRequests(RequestSpecification spec) {
        projectRequest = new UncheckedProjects(spec);
        userRequest = new UncheckedUsers(spec);
        buildTypeRequest = new UncheckedBuildTypes(spec);
    }

}
