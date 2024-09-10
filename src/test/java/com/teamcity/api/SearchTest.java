package com.teamcity.api;

import com.teamcity.api.models.BuildType;
import com.teamcity.api.models.Project;
import com.teamcity.api.models.User;
import com.teamcity.api.requests.checked.CheckedBase;
import com.teamcity.api.requests.checked.CheckedSearch;
import com.teamcity.api.spec.Specifications;
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

    @Test(description = "User should be able to search models", groups = {"Regression"})
    public void searchTest() {
        var initialProjectSize = new CheckedSearch<>(Specifications.getSpec()
                .superUserSpec(), PROJECTS).search().size();
        var initialUsersSize = new CheckedSearch<>(Specifications.getSpec()
                .superUserSpec(), USERS).search().size();
        var initialBuildTypesSize = new CheckedSearch<>(Specifications.getSpec()
                .superUserSpec(), BUILD_TYPES).search().size();

        var createdProjects = new ArrayList<String>();
        var createdUsers = new ArrayList<String>();
        var createdBuildTypes = new ArrayList<String>();

        for (var i = 0; i < CREATED_MODELS_COUNT; i++) {
            createdProjects.add(new CheckedBase<Project>(Specifications.getSpec()
                    .superUserSpec(), PROJECTS).create(testData.getProject()).getName());
            createdUsers.add(new CheckedBase<User>(Specifications.getSpec()
                    .superUserSpec(), USERS).create(testData.getUser()).getUsername());
            createdBuildTypes.add(new CheckedBase<BuildType>(Specifications.getSpec()
                    .superUserSpec(), BUILD_TYPES).create(testData.getBuildType()).getName());
            testData = generate();
        }

        var projects = new CheckedSearch<Project>(Specifications.getSpec()
                .superUserSpec(), PROJECTS).search().stream().map(Project::getName).toList();
        var users = new CheckedSearch<User>(Specifications.getSpec()
                .superUserSpec(), USERS).search().stream().map(User::getUsername).toList();
        var buildTypes = new CheckedSearch<BuildType>(Specifications.getSpec()
                .superUserSpec(), BUILD_TYPES).search().stream().map(BuildType::getName).toList();

        softy.assertThat(projects).as("projects")
                .hasSize(initialProjectSize + CREATED_MODELS_COUNT).containsAll(createdProjects);
        softy.assertThat(users).as("users")
                .hasSize(initialUsersSize + CREATED_MODELS_COUNT).containsAll(createdUsers);
        softy.assertThat(buildTypes).as("buildTypes")
                .hasSize(initialBuildTypesSize + CREATED_MODELS_COUNT).containsAll(createdBuildTypes);
    }

}
