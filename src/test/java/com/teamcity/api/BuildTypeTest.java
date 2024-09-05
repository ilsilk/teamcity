package com.teamcity.api;

import com.teamcity.api.enums.UserRole;
import com.teamcity.api.generators.RandomData;
import com.teamcity.api.models.BuildType;
import com.teamcity.api.models.Roles;
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
        checkedSuperUser.getRequest(USERS).create(testData.getUser());
        checkedSuperUser.getRequest(PROJECTS).create(testData.getProject());

        var checkedBuildTypeRequest = new CheckedBase(Specifications.getSpec()
                .authSpec(testData.getUser()), BUILD_TYPES);
        var buildType = (BuildType) checkedBuildTypeRequest.create(testData.getBuildType());

        softy.assertThat(buildType.getId()).as("buildTypeId").isEqualTo(testData.getBuildType().getId());
    }

    @Test(description = "User should not be able to create two build types with the same id", groups = {"Regression"})
    public void userCreatesTwoBuildTypesWithSameIdTest() {
        checkedSuperUser.getRequest(USERS).create(testData.getUser());
        checkedSuperUser.getRequest(PROJECTS).create(testData.getProject());

        var checkedBuildTypeRequest = new CheckedBase(Specifications.getSpec()
                .authSpec(testData.getUser()), BUILD_TYPES);
        checkedBuildTypeRequest.create(testData.getBuildType());

        var secondTestData = generate();
        var buildTypeTestData = testData.getBuildType();
        var secondBuildTypeTestData = secondTestData.getBuildType();
        secondBuildTypeTestData.setId(buildTypeTestData.getId());
        secondBuildTypeTestData.setProject(buildTypeTestData.getProject());

        var uncheckedBuildTypeRequest = new UncheckedBase(Specifications.getSpec()
                .authSpec(testData.getUser()), BUILD_TYPES);
        uncheckedBuildTypeRequest.create(secondTestData.getBuildType())
                .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test(description = "User should not be able to create build type with id exceeding the limit", groups = {"Regression"})
    public void userCreatesBuildTypeWithIdExceedingLimitTest() {
        checkedSuperUser.getRequest(USERS).create(testData.getUser());
        checkedSuperUser.getRequest(PROJECTS).create(testData.getProject());

        var buildTypeTestData = testData.getBuildType();
        buildTypeTestData.setId(RandomData.getString(BUILD_TYPE_ID_CHARACTERS_LIMIT + 1));

        var uncheckedBuildTypeRequest = new UncheckedBase(Specifications.getSpec()
                .authSpec(testData.getUser()), BUILD_TYPES);
        uncheckedBuildTypeRequest.create(testData.getBuildType())
                .then().assertThat().statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);

        buildTypeTestData.setId(RandomData.getString(BUILD_TYPE_ID_CHARACTERS_LIMIT));

        var checkedBuildTypeRequest = new CheckedBase(Specifications.getSpec()
                .authSpec(testData.getUser()), BUILD_TYPES);
        checkedBuildTypeRequest.create(testData.getBuildType());
    }

    @Test(description = "Unauthorized user should not be able to create build type", groups = {"Regression"})
    public void unauthorizedUserCreatesBuildTypeTest() {
        checkedSuperUser.getRequest(PROJECTS).create(testData.getProject());

        var uncheckedBuildTypeRequest = new UncheckedBase(Specifications.getSpec()
                .unauthSpec(), BUILD_TYPES);
        uncheckedBuildTypeRequest.create(testData.getBuildType())
                .then().assertThat().statusCode(HttpStatus.SC_UNAUTHORIZED);

        uncheckedSuperUser.getRequest(BUILD_TYPES).read(testData.getBuildType().getId())
                .then().assertThat().statusCode(HttpStatus.SC_NOT_FOUND)
                .body(Matchers.containsString("Could not find the entity requested"));
    }

    @Test(description = "User should be able to delete build type", groups = {"Regression"})
    public void userDeletesBuildTypeTest() {
        checkedSuperUser.getRequest(USERS).create(testData.getUser());
        checkedSuperUser.getRequest(PROJECTS).create(testData.getProject());

        var checkedBuildTypeRequest = new CheckedBase(Specifications.getSpec()
                .authSpec(testData.getUser()), BUILD_TYPES);
        checkedBuildTypeRequest.create(testData.getBuildType());
        checkedBuildTypeRequest.delete(testData.getBuildType().getId());

        var uncheckedBuildTypeRequest = new UncheckedBase(Specifications.getSpec()
                .authSpec(testData.getUser()), BUILD_TYPES);
        uncheckedBuildTypeRequest.read(testData.getBuildType().getId())
                .then().assertThat().statusCode(HttpStatus.SC_NOT_FOUND)
                .body(Matchers.containsString("Could not find the entity requested"));
    }

    @Test(description = "Project admin should be able to create build type for their project", groups = {"Regression"})
    public void projectAdminCreatesBuildTypeTest() {
        checkedSuperUser.getRequest(PROJECTS).create(testData.getProject());

        var userTestData = testData.getUser();
        var projectTestData = testData.getProject();
        userTestData.setRoles(generate(Roles.class, UserRole.PROJECT_ADMIN, "p:" + projectTestData.getId()));

        checkedSuperUser.getRequest(USERS).create(testData.getUser());

        var checkedBuildTypeRequest = new CheckedBase(Specifications.getSpec()
                .authSpec(testData.getUser()), BUILD_TYPES);
        var buildType = (BuildType) checkedBuildTypeRequest.create(testData.getBuildType());

        softy.assertThat(buildType.getId()).as("buildTypeId").isEqualTo(testData.getBuildType().getId());
    }

    @Test(description = "Project admin should not be able to create build type for not their project", groups = {"Regression"})
    public void projectAdminCreatesBuildTypeForAnotherUserProjectTest() {
        var secondTestData = generate();
        checkedSuperUser.getRequest(PROJECTS).create(testData.getProject());
        checkedSuperUser.getRequest(PROJECTS).create(secondTestData.getProject());

        var userTestData = testData.getUser();
        var secondUserTestData = secondTestData.getUser();
        var projectTestData = testData.getProject();
        var secondProjectTestData = secondTestData.getProject();
        userTestData.setRoles(generate(Roles.class, UserRole.PROJECT_ADMIN, "p:" + projectTestData.getId()));
        secondUserTestData.setRoles(generate(Roles.class, UserRole.PROJECT_ADMIN, "p:" + secondProjectTestData.getId()));

        checkedSuperUser.getRequest(USERS).create(testData.getUser());
        checkedSuperUser.getRequest(USERS).create(secondTestData.getUser());

        var uncheckedBuildTypeRequest = new UncheckedBase(Specifications.getSpec()
                .authSpec(testData.getUser()), BUILD_TYPES);
        uncheckedBuildTypeRequest.create(secondTestData.getBuildType())
                .then().assertThat().statusCode(HttpStatus.SC_FORBIDDEN)
                .body(Matchers.containsString("Access denied"));
    }

}
