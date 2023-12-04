package com.teamcity.api.generators;

import com.teamcity.api.models.BuildType;
import com.teamcity.api.models.NewProjectDescription;
import com.teamcity.api.models.User;
import com.teamcity.api.requests.UncheckedRequests;
import com.teamcity.api.spec.Specifications;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TestData {

    private NewProjectDescription project;
    private User user;
    private BuildType buildType;

    public void delete() {
        var uncheckedSuperUser = new UncheckedRequests(Specifications.getSpec().superUserSpec());
        uncheckedSuperUser.getUserRequest().delete(user.getUsername());
        uncheckedSuperUser.getBuildTypeRequest().delete(buildType.getId());
        uncheckedSuperUser.getProjectRequest().delete(project.getId());
    }

}
