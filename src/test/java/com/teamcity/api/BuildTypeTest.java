package com.teamcity.api;

import com.teamcity.api.enums.UserRole;
import com.teamcity.api.generators.RandomData;
import com.teamcity.api.models.BuildType;
import com.teamcity.api.models.NewProjectDescription;
import com.teamcity.api.models.Roles;
import com.teamcity.api.models.User;
import com.teamcity.api.requests.checked.CheckedBase;
import com.teamcity.api.requests.unchecked.UncheckedBase;
import com.teamcity.api.spec.Specifications;
import io.qameta.allure.Feature;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import static com.teamcity.api.enums.Endpoint.BUILD_TYPES;
import static com.teamcity.api.enums.Endpoint.PROJECTS;
import static com.teamcity.api.enums.Endpoint.USERS;
import static com.teamcity.api.generators.TestDataGenerator.generate;

@Feature("Build type")
public class BuildTypeTest extends BaseApiTest {

    private static final int BUILD_TYPE_ID_CHARACTERS_LIMIT = 225;

    @Test(description = "User should be able to create build type", groups = {"Regression"})
    public void userCreatesBuildTypeTest() {
        checkedSuperUser.getRequest(USERS).create(testData.user());
        checkedSuperUser.getRequest(PROJECTS).create(testData.project());

        var checkedBuildTypeRequest = new CheckedBase(Specifications.getSpec()
                .authSpec(testData.user()), BUILD_TYPES);
        var buildType = (BuildType) checkedBuildTypeRequest.create(testData.buildType());

        softy.assertThat(buildType.id()).as("buildTypeId").isEqualTo(testData.buildType().id());
    }

    @Test(description = "User should not be able to create two build types with the same id", groups = {"Regression"})
    public void userCreatesTwoBuildTypesWithSameIdTest() {
        checkedSuperUser.getRequest(USERS).create(testData.user());
        checkedSuperUser.getRequest(PROJECTS).create(testData.project());

        var checkedBuildTypeRequest = new CheckedBase(Specifications.getSpec()
                .authSpec(testData.user()), BUILD_TYPES);
        checkedBuildTypeRequest.create(testData.buildType());

        var secondTestData = generate();
        var buildTypeTestData = testData.buildType();
        var secondBuildTypeTestData = (BuildType) secondTestData.buildType();
        secondBuildTypeTestData.id(buildTypeTestData.id());
        secondBuildTypeTestData.project(buildTypeTestData.project());

        var uncheckedBuildTypeRequest = new UncheckedBase(Specifications.getSpec()
                .authSpec(testData.user()), BUILD_TYPES);
        uncheckedBuildTypeRequest.create(secondTestData.buildType())
                .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test(description = "User should not be able to create build type with id exceeding the limit", groups = {"Regression"})
    public void userCreatesBuildTypeWithIdExceedingLimitTest() {
        checkedSuperUser.getRequest(USERS).create(testData.user());
        checkedSuperUser.getRequest(PROJECTS).create(testData.project());

        var buildTypeTestData = testData.buildType();
        buildTypeTestData.id(RandomData.getString(BUILD_TYPE_ID_CHARACTERS_LIMIT + 1));

        var uncheckedBuildTypeRequest = new UncheckedBase(Specifications.getSpec()
                .authSpec(testData.user()), BUILD_TYPES);
        uncheckedBuildTypeRequest.create(testData.buildType())
                .then().assertThat().statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);

        buildTypeTestData.id(RandomData.getString(BUILD_TYPE_ID_CHARACTERS_LIMIT));

        var checkedBuildTypeRequest = new CheckedBase(Specifications.getSpec()
                .authSpec(testData.user()), BUILD_TYPES);
        checkedBuildTypeRequest.create(testData.buildType());
    }

    @Test(description = "Unauthorized user should not be able to create build type", groups = {"Regression"})
    public void unauthorizedUserCreatesBuildTypeTest() {
        checkedSuperUser.getRequest(PROJECTS).create(testData.project());

        var uncheckedBuildTypeRequest = new UncheckedBase(Specifications.getSpec()
                .unauthSpec(), BUILD_TYPES);
        uncheckedBuildTypeRequest.create(testData.buildType())
                .then().assertThat().statusCode(HttpStatus.SC_UNAUTHORIZED);

        uncheckedSuperUser.getRequest(BUILD_TYPES).read(testData.buildType().id())
                .then().assertThat().statusCode(HttpStatus.SC_NOT_FOUND)
                .body(Matchers.containsString("Could not find the entity requested"));
    }

    @Test(description = "User should be able to delete build type", groups = {"Regression"})
    public void userDeletesBuildTypeTest() {
        checkedSuperUser.getRequest(USERS).create(testData.user());
        checkedSuperUser.getRequest(PROJECTS).create(testData.project());

        var checkedBuildTypeRequest = new CheckedBase(Specifications.getSpec()
                .authSpec(testData.user()), BUILD_TYPES);
        checkedBuildTypeRequest.create(testData.buildType());
        checkedBuildTypeRequest.delete(testData.buildType().id());

        var uncheckedBuildTypeRequest = new UncheckedBase(Specifications.getSpec()
                .authSpec(testData.user()), BUILD_TYPES);
        uncheckedBuildTypeRequest.read(testData.buildType().id())
                .then().assertThat().statusCode(HttpStatus.SC_NOT_FOUND)
                .body(Matchers.containsString("Could not find the entity requested"));
    }

    @Test(description = "Project admin should be able to create build type for their project", groups = {"Regression"})
    public void projectAdminCreatesBuildTypeTest() {
        checkedSuperUser.getRequest(PROJECTS).create(testData.project());

        var userTestData = testData.user();
        var projectTestData = testData.project();
        userTestData.roles(generate(Roles.class, UserRole.PROJECT_ADMIN, "p:" + projectTestData.id()));

        checkedSuperUser.getRequest(USERS).create(testData.user());

        var checkedBuildTypeRequest = new CheckedBase(Specifications.getSpec()
                .authSpec(testData.user()), BUILD_TYPES);
        var buildType = (BuildType) checkedBuildTypeRequest.create(testData.buildType());

        softy.assertThat(buildType.id()).as("buildTypeId").isEqualTo(testData.buildType().id());
    }

    @Test(description = "Project admin should not be able to create build type for not their project", groups = {"Regression"})
    public void projectAdminCreatesBuildTypeForAnotherUserProjectTest() {
        var secondTestData = generate();
        checkedSuperUser.getRequest(PROJECTS).create(testData.project());
        checkedSuperUser.getRequest(PROJECTS).create(secondTestData.project());

        var userTestData = testData.user();
        var secondUserTestData = (User) secondTestData.user();
        var projectTestData = testData.project();
        var secondProjectTestData = (NewProjectDescription) secondTestData.project();
        userTestData.roles(generate(Roles.class, UserRole.PROJECT_ADMIN, "p:" + projectTestData.id()));
        secondUserTestData.roles(generate(Roles.class, UserRole.PROJECT_ADMIN, "p:" + secondProjectTestData.id()));

        checkedSuperUser.getRequest(USERS).create(testData.user());
        checkedSuperUser.getRequest(USERS).create(secondTestData.user());

        var uncheckedBuildTypeRequest = new UncheckedBase(Specifications.getSpec()
                .authSpec(testData.user()), BUILD_TYPES);
        uncheckedBuildTypeRequest.create(secondTestData.buildType())
                .then().assertThat().statusCode(HttpStatus.SC_FORBIDDEN)
                .body(Matchers.containsString("Access denied"));
    }

}
