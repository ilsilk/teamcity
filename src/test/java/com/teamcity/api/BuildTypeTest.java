package com.teamcity.api;

import com.teamcity.api.enums.UserRole;
import com.teamcity.api.generators.RandomData;
import com.teamcity.api.generators.TestDataGenerator;
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
        var firstTestData = testData;
        var secondTestData = TestDataGenerator.generate();

        checkedSuperUser.getRequest(USERS).create(testData.get(USERS));
        checkedSuperUser.getRequest(PROJECTS).create(testData.get(PROJECTS));

        var checkedBuildTypeRequest = new CheckedBase(Specifications.getSpec()
                .authSpec(testData.get(USERS)), BUILD_TYPES);
        checkedBuildTypeRequest.create(firstTestData.get(BUILD_TYPES));

        ((BuildType) secondTestData.get(BUILD_TYPES)).setId(((BuildType) firstTestData.get(BUILD_TYPES)).getId());
        ((BuildType) secondTestData.get(BUILD_TYPES)).setProject(((BuildType) firstTestData.get(BUILD_TYPES)).getProject());

        var uncheckedBuildTypeRequest = new UncheckedBase(Specifications.getSpec()
                .authSpec(firstTestData.get(USERS)), BUILD_TYPES);
        uncheckedBuildTypeRequest.create(secondTestData.get(BUILD_TYPES))
                .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test(description = "User should not be able to create build type with id exceeding the limit", groups = {"Regression"})
    public void userCreatesBuildTypeWithIdExceedingLimitTest() {
        checkedSuperUser.getRequest(USERS).create(testData.get(USERS));
        checkedSuperUser.getRequest(PROJECTS).create(testData.get(PROJECTS));

        ((BuildType) testData.get(BUILD_TYPES)).setId(RandomData.getString(BUILD_TYPE_ID_CHARACTERS_LIMIT + 1));

        var uncheckedBuildTypeRequest = new UncheckedBase(Specifications.getSpec()
                .authSpec(testData.get(USERS)), BUILD_TYPES);
        uncheckedBuildTypeRequest.create(testData.get(BUILD_TYPES))
                .then().assertThat().statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);

        ((BuildType) testData.get(BUILD_TYPES)).setId(RandomData.getString(BUILD_TYPE_ID_CHARACTERS_LIMIT));

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

        ((User) testData.get(USERS)).setRoles((Roles) TestDataGenerator.generate(Roles.class,
                UserRole.PROJECT_ADMIN, "p:" + ((NewProjectDescription) testData.get(PROJECTS)).getId()));

        checkedSuperUser.getRequest(USERS).create(testData.get(USERS));

        var checkedBuildTypeRequest = new CheckedBase(Specifications.getSpec()
                .authSpec(testData.get(USERS)), BUILD_TYPES);
        var buildType = (BuildType) checkedBuildTypeRequest.create(testData.get(BUILD_TYPES));

        softy.assertThat(buildType.getId()).as("buildTypeId").isEqualTo(((BuildType) testData.get(BUILD_TYPES)).getId());
    }

    @Test(description = "Project admin should not be able to create build type for not their project", groups = {"Regression"})
    public void projectAdminCreatesBuildTypeForAnotherUserProjectTest() {
        var firstTestData = testData;
        var secondTestData = TestDataGenerator.generate();

        checkedSuperUser.getRequest(PROJECTS).create(firstTestData.get(PROJECTS));
        checkedSuperUser.getRequest(PROJECTS).create(secondTestData.get(PROJECTS));

        ((User) firstTestData.get(USERS)).setRoles((Roles) TestDataGenerator.generate(Roles.class,
                UserRole.PROJECT_ADMIN, "p:" + ((NewProjectDescription) firstTestData.get(PROJECTS)).getId()));
        ((User) secondTestData.get(USERS)).setRoles((Roles) TestDataGenerator.generate(Roles.class,
                UserRole.PROJECT_ADMIN, "p:" + ((NewProjectDescription) secondTestData.get(PROJECTS)).getId()));

        checkedSuperUser.getRequest(USERS).create(firstTestData.get(USERS));
        checkedSuperUser.getRequest(USERS).create(secondTestData.get(USERS));

        var uncheckedBuildTypeRequest = new UncheckedBase(Specifications.getSpec()
                .authSpec(firstTestData.get(USERS)), BUILD_TYPES);
        uncheckedBuildTypeRequest.create(secondTestData.get(BUILD_TYPES))
                .then().assertThat().statusCode(HttpStatus.SC_FORBIDDEN)
                .body(Matchers.containsString("Access denied"));
    }

}
