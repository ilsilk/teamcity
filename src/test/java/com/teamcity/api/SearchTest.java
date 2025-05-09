package com.teamcity.api;

import com.teamcity.api.models.BuildType;
import com.teamcity.api.models.Project;
import com.teamcity.api.models.User;
import com.teamcity.api.requests.checked.CheckedBase;
import io.qameta.allure.Feature;
import org.testng.annotations.Test;

import java.util.ArrayList;

import static com.teamcity.api.enums.Endpoint.BUILD_TYPES;
import static com.teamcity.api.enums.Endpoint.PROJECTS;
import static com.teamcity.api.enums.Endpoint.USERS;
import static com.teamcity.api.generators.TestDataGenerator.generate;

@Feature("Search")
public class SearchTest extends BaseApiTest {

    private static final int CREATED_MODELS_COUNT = 3;
    private final CheckedBase<Project> checkedProjectRequest = checkedSuperUser.getRequest(PROJECTS);
    private final CheckedBase<User> checkedUserRequest = checkedSuperUser.getRequest(USERS);
    private final CheckedBase<BuildType> checkedBuildTypeRequest = checkedSuperUser.getRequest(BUILD_TYPES);

    @Test(description = "User should be able to search models", groups = {"Regression"})
    public void searchTest() {
        var createdProjects = new ArrayList<String>();
        var createdUsers = new ArrayList<String>();
        var createdBuildTypes = new ArrayList<String>();

        for (var i = 0; i < CREATED_MODELS_COUNT; i++) {
            createdProjects.add(checkedProjectRequest.create(testData.get().getNewProjectDescription()).getName());
            createdUsers.add(checkedUserRequest.create(testData.get().getUser()).getUsername());
            createdBuildTypes.add(checkedBuildTypeRequest.create(testData.get().getBuildType()).getName());
            testData.set(generate());
        }

        var projects = checkedProjectRequest.search().stream().map(Project::getName).toList();
        var users = checkedUserRequest.search().stream().map(User::getUsername).toList();
        var buildTypes = checkedBuildTypeRequest.search().stream().map(BuildType::getName).toList();

        softy.assertThat(projects).as("projects").containsAll(createdProjects);
        softy.assertThat(users).as("users").containsAll(createdUsers);
        softy.assertThat(buildTypes).as("buildTypes").containsAll(createdBuildTypes);
    }

}
