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
    private final CheckedSearch<Project> checkedProjectsSearchRequest = new CheckedSearch<>(Specifications.getSpec()
            .superUserSpec(), PROJECTS);
    private final CheckedSearch<User> checkedUsersSearchRequest = new CheckedSearch<>(Specifications.getSpec()
            .superUserSpec(), USERS);
    private final CheckedSearch<BuildType> checkedBuildTypesSearchRequest = new CheckedSearch<>(Specifications.getSpec()
            .superUserSpec(), BUILD_TYPES);

    @Test(description = "User should be able to search models", groups = {"Regression"})
    public void searchTest() {
        var initialProjectSize = checkedProjectsSearchRequest.search().size();
        var initialUsersSize = checkedUsersSearchRequest.search().size();
        var initialBuildTypesSize = checkedBuildTypesSearchRequest.search().size();

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

        var projects = checkedProjectsSearchRequest.search().stream().map(Project::getName).toList();
        var users = checkedUsersSearchRequest.search().stream().map(User::getUsername).toList();
        var buildTypes = checkedBuildTypesSearchRequest.search().stream().map(BuildType::getName).toList();

        softy.assertThat(projects).as("projects")
                .hasSize(initialProjectSize + CREATED_MODELS_COUNT).containsAll(createdProjects);
        softy.assertThat(users).as("users")
                .hasSize(initialUsersSize + CREATED_MODELS_COUNT).containsAll(createdUsers);
        softy.assertThat(buildTypes).as("buildTypes")
                .hasSize(initialBuildTypesSize + CREATED_MODELS_COUNT).containsAll(createdBuildTypes);
    }

}
