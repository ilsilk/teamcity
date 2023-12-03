package com.teamcity.api;

import com.teamcity.api.enums.RoleEnum;
import com.teamcity.api.generators.RandomData;
import com.teamcity.api.generators.TestDataGenerator;
import com.teamcity.api.requests.checked.CheckedBuildType;
import com.teamcity.api.requests.unchecked.UncheckedBuildType;
import com.teamcity.api.spec.Specifications;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

public class BuildTypeTest extends BaseApiTest {

    private static final int BUILD_TYPE_ID_CHARACTERS_LIMIT = 225;

    @Test(description = "User should be able to create build type")
    public void userCreatesBuildTypeTest() {
        checkedSuperUser.getUserRequest().create(testData.getUser());

        checkedSuperUser.getProjectRequest().create(testData.getProject());

        var buildType = new CheckedBuildType(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(testData.getBuildType());

        softy.assertThat(buildType.getId()).isEqualTo(testData.getBuildType().getId());
    }

    @Test(description = "User should not be able to create two build types with the same id")
    public void userCreatesTwoBuildTypesWithSameIdTest() {
        var firstTestData = testData;
        var secondTestData = testDataStorage.addTestData();

        checkedSuperUser.getUserRequest().create(firstTestData.getUser());

        checkedSuperUser.getProjectRequest().create(firstTestData.getProject());

        new CheckedBuildType(Specifications.getSpec()
                .authSpec(firstTestData.getUser()))
                .create(firstTestData.getBuildType());

        secondTestData.getBuildType().setId(firstTestData.getBuildType().getId());
        secondTestData.getBuildType().setProject(firstTestData.getBuildType().getProject());

        new UncheckedBuildType(Specifications.getSpec()
                .authSpec(firstTestData.getUser()))
                .create(secondTestData.getBuildType())
                .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test(description = "User should not be able to create build type with id exceeding the limit")
    public void userCreatesBuildTypeWithIdExceedingLimitTest() {
        checkedSuperUser.getUserRequest().create(testData.getUser());

        checkedSuperUser.getProjectRequest().create(testData.getProject());

        testData.getBuildType().setId(RandomData.getString(BUILD_TYPE_ID_CHARACTERS_LIMIT + 1));

        new UncheckedBuildType(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(testData.getBuildType())
                .then().assertThat().statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);

        testData.getBuildType().setId(RandomData.getString(BUILD_TYPE_ID_CHARACTERS_LIMIT));

        new CheckedBuildType(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(testData.getBuildType());
    }

    @Test(description = "Unauthorized user should not be able to create build type")
    public void unauthorizedUserCreatesBuildTypeTest() {
        checkedSuperUser.getProjectRequest().create(testData.getProject());

        new UncheckedBuildType(Specifications.getSpec().unauthSpec())
                .create(testData.getBuildType())
                .then().assertThat().statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body(Matchers.containsString("Authentication required"));

        uncheckedSuperUser.getBuildTypeRequest()
                .read(testData.getBuildType().getId())
                .then().assertThat().statusCode(HttpStatus.SC_NOT_FOUND)
                .body(Matchers.containsString("Could not find the entity requested"));
    }

    @Test(description = "User should be able to delete build type")
    public void userDeletesBuildTypeTest() {
        checkedSuperUser.getUserRequest().create(testData.getUser());

        checkedSuperUser.getProjectRequest().create(testData.getProject());

        new CheckedBuildType(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(testData.getBuildType());

        new CheckedBuildType(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .delete(testData.getBuildType().getId());

        new UncheckedBuildType(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .read(testData.getBuildType().getId())
                .then().assertThat().statusCode(HttpStatus.SC_NOT_FOUND)
                .body(Matchers.containsString("Could not find the entity requested"));
    }

    @Test(description = "Project admin should be able to create build type for their project")
    public void projectAdminCreatesBuildTypeTest() {
        checkedSuperUser.getProjectRequest().create(testData.getProject());

        testData.getUser().setRoles(TestDataGenerator.generateRoles(
                RoleEnum.PROJECT_ADMIN, "p:" + testData.getProject().getId()));

        checkedSuperUser.getUserRequest().create(testData.getUser());

        var buildType = new CheckedBuildType(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(testData.getBuildType());

        softy.assertThat(buildType.getId()).isEqualTo(testData.getBuildType().getId());
    }

    @Test(description = "Project admin should not be able to create build type for not their project")
    public void projectAdminCreatesBuildTypeForAnotherUserProjectTest() {
        var firstTestData = testData;
        var secondTestData = testDataStorage.addTestData();

        checkedSuperUser.getProjectRequest().create(firstTestData.getProject());
        checkedSuperUser.getProjectRequest().create(secondTestData.getProject());

        firstTestData.getUser().setRoles(TestDataGenerator.generateRoles(
                RoleEnum.PROJECT_ADMIN, "p:" + firstTestData.getProject().getId()));
        secondTestData.getUser().setRoles(TestDataGenerator.generateRoles(
                RoleEnum.PROJECT_ADMIN, "p:" + secondTestData.getProject().getId()));

        checkedSuperUser.getUserRequest().create(firstTestData.getUser());
        checkedSuperUser.getUserRequest().create(secondTestData.getUser());

        new UncheckedBuildType(Specifications.getSpec()
                .authSpec(firstTestData.getUser()))
                .create(secondTestData.getBuildType())
                .then().assertThat().statusCode(HttpStatus.SC_FORBIDDEN)
                .body(Matchers.containsString("Access denied"));
    }

}
