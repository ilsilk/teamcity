package com.teamcity.api.generators;

import com.teamcity.api.models.BuildType;
import com.teamcity.api.models.NewProjectDescription;
import com.teamcity.api.models.User;
import com.teamcity.api.requests.UncheckedRequests;
import com.teamcity.api.spec.Specifications;
import lombok.Builder;
import lombok.Data;

import static com.teamcity.api.enums.Endpoint.*;

@Data
@Builder
public class TestData {

    private NewProjectDescription project;
    private User user;
    private BuildType buildType;

    public void delete() {
        var uncheckedSuperUser = new UncheckedRequests(Specifications.getSpec().superUserSpec());
        uncheckedSuperUser.getRequest(PROJECTS).delete(project.getId());
        uncheckedSuperUser.getRequest(USERS).delete(user.getUsername());
        uncheckedSuperUser.getRequest(BUILD_TYPES).delete(buildType.getId());
    }

}
