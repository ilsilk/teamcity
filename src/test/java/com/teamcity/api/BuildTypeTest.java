package com.teamcity.api;

import com.teamcity.api.enums.RoleEnum;
import com.teamcity.api.generators.TestDataGenerator;
import com.teamcity.api.requests.checked.CheckedBuildType;
import com.teamcity.api.requests.unchecked.UncheckedBuildType;
import com.teamcity.api.spec.Specifications;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

public class BuildTypeTest extends BaseApiTest {

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
