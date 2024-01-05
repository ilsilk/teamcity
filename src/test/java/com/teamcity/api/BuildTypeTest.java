package com.teamcity.api;

import com.teamcity.api.enums.UserRole;
import com.teamcity.api.generators.RandomData;
import com.teamcity.api.generators.TestDataGenerator;
import com.teamcity.api.models.BuildType;
import com.teamcity.api.requests.checked.CheckedBase;
import com.teamcity.api.requests.unchecked.UncheckedBase;
import com.teamcity.api.spec.Specifications;
import io.qameta.allure.Feature;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import static com.teamcity.api.enums.Endpoint.*;

@Feature("Build type")
public class BuildTypeTest extends BaseApiTest {

    private static final int BUILD_TYPE_ID_CHARACTERS_LIMIT = 225;

    @Test(description = "User should be able to create build type")
    public void userCreatesBuildTypeTest() {
        checkedSuperUser.getRequest(USERS).create(testData.getUser());
        checkedSuperUser.getRequest(PROJECTS).create(testData.getProject());

        var checkedBuildTypeRequest = new CheckedBase(Specifications.getSpec()
                .authSpec(testData.getUser()), BUILD_TYPES);
        var buildType = (BuildType) checkedBuildTypeRequest.create(testData.getBuildType());

        softy.assertThat(buildType.getId()).as("buildTypeId").isEqualTo(testData.getBuildType().getId());
    }

    @Test(description = "User should not be able to create two build types with the same id")
    public void userCreatesTwoBuildTypesWithSameIdTest() {
        var firstTestData = testData;
        var secondTestData = TestDataGenerator.generate();

        checkedSuperUser.getRequest(USERS).create(testData.getUser());
        checkedSuperUser.getRequest(PROJECTS).create(testData.getProject());

        var checkedBuildTypeRequest = new CheckedBase(Specifications.getSpec()
                .authSpec(testData.getUser()), BUILD_TYPES);
        checkedBuildTypeRequest.create(firstTestData.getBuildType());

        secondTestData.getBuildType().setId(firstTestData.getBuildType().getId());
        secondTestData.getBuildType().setProject(firstTestData.getBuildType().getProject());

        var uncheckedBuildTypeRequest = new UncheckedBase(Specifications.getSpec()
                .authSpec(firstTestData.getUser()), BUILD_TYPES);
        uncheckedBuildTypeRequest.create(secondTestData.getBuildType())
                .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test(description = "User should not be able to create build type with id exceeding the limit")
    public void userCreatesBuildTypeWithIdExceedingLimitTest() {
        checkedSuperUser.getRequest(USERS).create(testData.getUser());
        checkedSuperUser.getRequest(PROJECTS).create(testData.getProject());

        testData.getBuildType().setId(RandomData.getString(BUILD_TYPE_ID_CHARACTERS_LIMIT + 1));

        var uncheckedBuildTypeRequest = new UncheckedBase(Specifications.getSpec()
                .authSpec(testData.getUser()), BUILD_TYPES);
        uncheckedBuildTypeRequest.create(testData.getBuildType())
                .then().assertThat().statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);

        testData.getBuildType().setId(RandomData.getString(BUILD_TYPE_ID_CHARACTERS_LIMIT));

        var checkedBuildTypeRequest = new CheckedBase(Specifications.getSpec()
                .authSpec(testData.getUser()), BUILD_TYPES);
        checkedBuildTypeRequest.create(testData.getBuildType());
    }

    @Test(description = "Unauthorized user should not be able to create build type")
    public void unauthorizedUserCreatesBuildTypeTest() {
        checkedSuperUser.getRequest(PROJECTS).create(testData.getProject());

        var uncheckedBuildTypeRequest = new UncheckedBase(Specifications.getSpec()
                .unauthSpec(), BUILD_TYPES);
        uncheckedBuildTypeRequest.create(testData.getBuildType())
                .then().assertThat().statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body(Matchers.containsString("Authentication required"));

        uncheckedSuperUser.getRequest(BUILD_TYPES).read(testData.getBuildType().getId())
                .then().assertThat().statusCode(HttpStatus.SC_NOT_FOUND)
                .body(Matchers.containsString("Could not find the entity requested"));
    }

    @Test(description = "User should be able to delete build type")
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

    @Test(description = "Project admin should be able to create build type for their project")
    public void projectAdminCreatesBuildTypeTest() {
        checkedSuperUser.getRequest(PROJECTS).create(testData.getProject());

        testData.getUser().setRoles(TestDataGenerator.generateRoles(
                UserRole.PROJECT_ADMIN, "p:" + testData.getProject().getId()));

        checkedSuperUser.getRequest(USERS).create(testData.getUser());

        var checkedBuildTypeRequest = new CheckedBase(Specifications.getSpec()
                .authSpec(testData.getUser()), BUILD_TYPES);
        var buildType = (BuildType) checkedBuildTypeRequest.create(testData.getBuildType());

        softy.assertThat(buildType.getId()).as("buildTypeId").isEqualTo(testData.getBuildType().getId());
    }

    @Test(description = "Project admin should not be able to create build type for not their project")
    public void projectAdminCreatesBuildTypeForAnotherUserProjectTest() {
        var firstTestData = testData;
        var secondTestData = TestDataGenerator.generate();

        checkedSuperUser.getRequest(PROJECTS).create(firstTestData.getProject());
        checkedSuperUser.getRequest(PROJECTS).create(secondTestData.getProject());

        firstTestData.getUser().setRoles(TestDataGenerator.generateRoles(
                UserRole.PROJECT_ADMIN, "p:" + firstTestData.getProject().getId()));
        secondTestData.getUser().setRoles(TestDataGenerator.generateRoles(
                UserRole.PROJECT_ADMIN, "p:" + secondTestData.getProject().getId()));

        checkedSuperUser.getRequest(USERS).create(firstTestData.getUser());
        checkedSuperUser.getRequest(USERS).create(secondTestData.getUser());

        var uncheckedBuildTypeRequest = new UncheckedBase(Specifications.getSpec()
                .authSpec(firstTestData.getUser()), BUILD_TYPES);
        uncheckedBuildTypeRequest.create(secondTestData.getBuildType())
                .then().assertThat().statusCode(HttpStatus.SC_FORBIDDEN)
                .body(Matchers.containsString("Access denied"));
    }

}
