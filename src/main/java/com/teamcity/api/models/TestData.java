package com.teamcity.api.models;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class TestData {

    private NewProjectDescription project;
    private User user;
    private BuildType buildType;

}
