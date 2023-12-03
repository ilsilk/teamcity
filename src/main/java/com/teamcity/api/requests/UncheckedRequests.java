package com.teamcity.api.requests;

import com.teamcity.api.requests.unchecked.UncheckedBuildType;
import com.teamcity.api.requests.unchecked.UncheckedProject;
import com.teamcity.api.requests.unchecked.UncheckedUser;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;

@Getter
public class UncheckedRequests {

    private final UncheckedProject projectRequest;
    private final UncheckedUser userRequest;
    private final UncheckedBuildType buildTypeRequest;

    public UncheckedRequests(RequestSpecification spec) {
        projectRequest = new UncheckedProject(spec);
        userRequest = new UncheckedUser(spec);
        buildTypeRequest = new UncheckedBuildType(spec);
    }

}
