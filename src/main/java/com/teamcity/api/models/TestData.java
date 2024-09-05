package com.teamcity.api.models;

import lombok.Data;

@Data
public class TestData {

    private NewProjectDescription project;
    private User user;
    private BuildType buildType;

}
