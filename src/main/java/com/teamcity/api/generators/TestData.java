package com.teamcity.api.generators;

import com.teamcity.api.models.BuildType;
import com.teamcity.api.models.NewProjectDescription;
import com.teamcity.api.models.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TestData {

    private NewProjectDescription project;
    private User user;
    private BuildType buildType;

}
