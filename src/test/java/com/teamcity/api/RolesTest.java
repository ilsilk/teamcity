package com.teamcity.api;

import com.teamcity.api.enums.RoleEnum;
import com.teamcity.api.generators.TestDataGenerator;
import com.teamcity.api.requests.checked.CheckedBuildType;
import com.teamcity.api.requests.checked.CheckedProject;
import com.teamcity.api.requests.unchecked.UncheckedBuildType;
import com.teamcity.api.requests.unchecked.UncheckedProject;
import com.teamcity.api.spec.Specifications;
import org.apache.http.HttpStatus;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.containsString;

public class RolesTest extends BaseApiTest {

    @Test
    public void unauthorizedUser() {
        var testData = testDataStorage.addTestData();

        new UncheckedProject(Specifications.getSpec().unauthSpec())
                .create(testData.getProject())
                .then().assertThat().statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body(containsString("Authentication required"));

        uncheckedSuperUser.getProjectRequest()
                .read(testData.getProject().getId())
                .then().assertThat().statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    public void systemAdminTest() {
        var testData = testDataStorage.addTestData();

        checkedSuperUser.getUserRequest().create(testData.getUser());

        var project = new CheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(testData.getProject());

        softy.assertThat(project.getId()).isEqualTo(testData.getProject().getId());
    }

    @Test
    public void projectAdminTest() {
        var testData = testDataStorage.addTestData();

        checkedSuperUser.getProjectRequest().create(testData.getProject());

        testData.getUser().setRoles(TestDataGenerator.generateRoles(RoleEnum.PROJECT_ADMIN,
                "p:" + testData.getProject().getId()));

        checkedSuperUser.getUserRequest().create(testData.getUser());

        var buildType = new CheckedBuildType(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(testData.getBuildType());

        softy.assertThat(buildType.getId()).isEqualTo(testData.getBuildType().getId());
    }

    @Test
    public void projectAdminTestNegative() {
        var firstTestData = testDataStorage.addTestData();
        var secondTestData = testDataStorage.addTestData();

        checkedSuperUser.getProjectRequest().create(firstTestData.getProject());
        checkedSuperUser.getProjectRequest().create(secondTestData.getProject());

        firstTestData.getUser().setRoles(TestDataGenerator.generateRoles(RoleEnum.PROJECT_ADMIN,
                "p:" + firstTestData.getProject().getId()));
        secondTestData.getUser().setRoles(TestDataGenerator.generateRoles(RoleEnum.PROJECT_ADMIN,
                "p:" + secondTestData.getProject().getId()));

        checkedSuperUser.getUserRequest().create(firstTestData.getUser());
        checkedSuperUser.getUserRequest().create(secondTestData.getUser());

        new UncheckedBuildType(Specifications.getSpec()
                .authSpec(firstTestData.getUser()))
                .create(secondTestData.getBuildType())
                .then().assertThat().statusCode(HttpStatus.SC_FORBIDDEN);
    }

}
