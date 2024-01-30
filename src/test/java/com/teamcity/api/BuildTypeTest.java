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

import static com.teamcity.api.enums.Endpoint.*;
import static com.teamcity.api.generators.TestDataGenerator.generate;

@Feature("Build type")
public class BuildTypeTest extends BaseApiTest {

    private static final int BUILD_TYPE_ID_CHARACTERS_LIMIT = 225;

    @Test(description = "User should be able to create build type", groups = {"Regression"})
    public void userCreatesBuildTypeTest() {
        checkedSuperUser.getRequest(USERS).create(testData.get(USERS));
        checkedSuperUser.getRequest(PROJECTS).create(testData.get(PROJECTS));

        var checkedBuildTypeRequest = new CheckedBase(Specifications.getSpec()
                .authSpec(testData.get(USERS)), BUILD_TYPES);
        var buildType = (BuildType) checkedBuildTypeRequest.create(testData.get(BUILD_TYPES));

        softy.assertThat(buildType.getId()).as("buildTypeId").isEqualTo(((BuildType) testData.get(BUILD_TYPES)).getId());
    }

    @Test(description = "User should not be able to create two build types with the same id", groups = {"Regression"})
    public void userCreatesTwoBuildTypesWithSameIdTest() {
        checkedSuperUser.getRequest(USERS).create(testData.get(USERS));
        checkedSuperUser.getRequest(PROJECTS).create(testData.get(PROJECTS));

        var checkedBuildTypeRequest = new CheckedBase(Specifications.getSpec()
                .authSpec(testData.get(USERS)), BUILD_TYPES);
        checkedBuildTypeRequest.create(testData.get(BUILD_TYPES));

        var secondTestData = generate();
        var buildTypeTestData = (BuildType) testData.get(BUILD_TYPES);
        var secondBuildTypeTestData = (BuildType) secondTestData.get(BUILD_TYPES);
        secondBuildTypeTestData.setId(buildTypeTestData.getId());
        secondBuildTypeTestData.setProject(buildTypeTestData.getProject());

        var uncheckedBuildTypeRequest = new UncheckedBase(Specifications.getSpec()
                .authSpec(testData.get(USERS)), BUILD_TYPES);
        uncheckedBuildTypeRequest.create(secondTestData.get(BUILD_TYPES))
                .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test(description = "User should not be able to create build type with id exceeding the limit", groups = {"Regression"})
    public void userCreatesBuildTypeWithIdExceedingLimitTest() {
        checkedSuperUser.getRequest(USERS).create(testData.get(USERS));
        checkedSuperUser.getRequest(PROJECTS).create(testData.get(PROJECTS));

        var buildTypeTestData = (BuildType) testData.get(BUILD_TYPES);
        buildTypeTestData.setId(RandomData.getString(BUILD_TYPE_ID_CHARACTERS_LIMIT + 1));

        var uncheckedBuildTypeRequest = new UncheckedBase(Specifications.getSpec()
                .authSpec(testData.get(USERS)), BUILD_TYPES);
        uncheckedBuildTypeRequest.create(testData.get(BUILD_TYPES))
                .then().assertThat().statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);

        buildTypeTestData.setId(RandomData.getString(BUILD_TYPE_ID_CHARACTERS_LIMIT));

        var checkedBuildTypeRequest = new CheckedBase(Specifications.getSpec()
                .authSpec(testData.get(USERS)), BUILD_TYPES);
        checkedBuildTypeRequest.create(testData.get(BUILD_TYPES));
    }

    @Test(description = "Unauthorized user should not be able to create build type", groups = {"Regression"})
    public void unauthorizedUserCreatesBuildTypeTest() {
        checkedSuperUser.getRequest(PROJECTS).create(testData.get(PROJECTS));

        var uncheckedBuildTypeRequest = new UncheckedBase(Specifications.getSpec()
                .unauthSpec(), BUILD_TYPES);
        uncheckedBuildTypeRequest.create(testData.get(BUILD_TYPES))
                .then().assertThat().statusCode(HttpStatus.SC_UNAUTHORIZED);

        uncheckedSuperUser.getRequest(BUILD_TYPES).read(((BuildType) testData.get(BUILD_TYPES)).getId())
                .then().assertThat().statusCode(HttpStatus.SC_NOT_FOUND)
                .body(Matchers.containsString("Could not find the entity requested"));
    }

    @Test(description = "User should be able to delete build type", groups = {"Regression"})
    public void userDeletesBuildTypeTest() {
        checkedSuperUser.getRequest(USERS).create(testData.get(USERS));
        checkedSuperUser.getRequest(PROJECTS).create(testData.get(PROJECTS));

        var checkedBuildTypeRequest = new CheckedBase(Specifications.getSpec()
                .authSpec(testData.get(USERS)), BUILD_TYPES);
        checkedBuildTypeRequest.create(testData.get(BUILD_TYPES));
        checkedBuildTypeRequest.delete(((BuildType) testData.get(BUILD_TYPES)).getId());

        var uncheckedBuildTypeRequest = new UncheckedBase(Specifications.getSpec()
                .authSpec(testData.get(USERS)), BUILD_TYPES);
        uncheckedBuildTypeRequest.read(((BuildType) testData.get(BUILD_TYPES)).getId())
                .then().assertThat().statusCode(HttpStatus.SC_NOT_FOUND)
                .body(Matchers.containsString("Could not find the entity requested"));
    }

    @Test(description = "Project admin should be able to create build type for their project", groups = {"Regression"})
    public void projectAdminCreatesBuildTypeTest() {
        checkedSuperUser.getRequest(PROJECTS).create(testData.get(PROJECTS));

        var userTestData = (User) testData.get(USERS);
        var projectTestData = (NewProjectDescription) testData.get(PROJECTS);
        userTestData.setRoles((Roles) generate(Roles.class, UserRole.PROJECT_ADMIN, "p:" + projectTestData.getId()));

        checkedSuperUser.getRequest(USERS).create(testData.get(USERS));

        var checkedBuildTypeRequest = new CheckedBase(Specifications.getSpec()
                .authSpec(testData.get(USERS)), BUILD_TYPES);
        var buildType = (BuildType) checkedBuildTypeRequest.create(testData.get(BUILD_TYPES));

        softy.assertThat(buildType.getId()).as("buildTypeId").isEqualTo(((BuildType) testData.get(BUILD_TYPES)).getId());
    }

    @Test(description = "Project admin should not be able to create build type for not their project", groups = {"Regression"})
    public void projectAdminCreatesBuildTypeForAnotherUserProjectTest() {
        var secondTestData = generate();
        checkedSuperUser.getRequest(PROJECTS).create(testData.get(PROJECTS));
        checkedSuperUser.getRequest(PROJECTS).create(secondTestData.get(PROJECTS));

        var userTestData = (User) testData.get(USERS);
        var secondUserTestData = (User) secondTestData.get(USERS);
        var projectTestData = (NewProjectDescription) testData.get(PROJECTS);
        var secondProjectTestData = (NewProjectDescription) secondTestData.get(PROJECTS);
        userTestData.setRoles((Roles) generate(Roles.class, UserRole.PROJECT_ADMIN, "p:" + projectTestData.getId()));
        secondUserTestData.setRoles((Roles) generate(Roles.class, UserRole.PROJECT_ADMIN, "p:" + secondProjectTestData.getId()));

        checkedSuperUser.getRequest(USERS).create(testData.get(USERS));
        checkedSuperUser.getRequest(USERS).create(secondTestData.get(USERS));

        var uncheckedBuildTypeRequest = new UncheckedBase(Specifications.getSpec()
                .authSpec(testData.get(USERS)), BUILD_TYPES);
        uncheckedBuildTypeRequest.create(secondTestData.get(BUILD_TYPES))
                .then().assertThat().statusCode(HttpStatus.SC_FORBIDDEN)
                .body(Matchers.containsString("Access denied"));
    }

}
