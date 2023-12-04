package com.teamcity.api;

import com.teamcity.api.generators.RandomData;
import com.teamcity.api.requests.checked.CheckedProjects;
import com.teamcity.api.requests.unchecked.UncheckedProjects;
import com.teamcity.api.spec.Specifications;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

public class ProjectTest extends BaseApiTest {

    private static final int PROJECT_ID_CHARACTERS_LIMIT = 225;

    @Test(description = "User should be able to create project")
    public void userCreatesProjectTest() {
        checkedSuperUser.getUserRequest().create(testData.getUser());

        var project = new CheckedProjects(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(testData.getProject());

        softy.assertThat(project.getId()).isEqualTo(testData.getProject().getId());
    }

    @Test(description = "User should not be able to create two projects with the same id")
    public void userCreatesTwoProjectsWithSameIdTest() {
        var firstTestData = testData;
        var secondTestData = testDataStorage.addTestData();

        checkedSuperUser.getUserRequest().create(firstTestData.getUser());

        new CheckedProjects(Specifications.getSpec()
                .authSpec(firstTestData.getUser()))
                .create(firstTestData.getProject());

        secondTestData.getProject().setId(firstTestData.getProject().getId());

        new UncheckedProjects(Specifications.getSpec()
                .authSpec(firstTestData.getUser()))
                .create(secondTestData.getProject())
                .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test(description = "User should not be able to create project with id exceeding the limit")
    public void userCreatesProjectWithIdExceedingLimitTest() {
        checkedSuperUser.getUserRequest().create(testData.getUser());

        testData.getProject().setId(RandomData.getString(PROJECT_ID_CHARACTERS_LIMIT + 1));

        new UncheckedProjects(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(testData.getProject())
                .then().assertThat().statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);

        testData.getProject().setId(RandomData.getString(PROJECT_ID_CHARACTERS_LIMIT));

        new CheckedProjects(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(testData.getProject());
    }

    @Test(description = "Unauthorized user should not be able to create project")
    public void unauthorizedUserCreatesProjectTest() {
        new UncheckedProjects(Specifications.getSpec().unauthSpec())
                .create(testData.getProject())
                .then().assertThat().statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body(Matchers.containsString("Authentication required"));

        uncheckedSuperUser.getProjectRequest()
                .read(testData.getProject().getId())
                .then().assertThat().statusCode(HttpStatus.SC_NOT_FOUND)
                .body(Matchers.containsString("Could not find the entity requested"));
    }

    @Test(description = "User should be able to delete project")
    public void userDeletesProjectTest() {
        checkedSuperUser.getUserRequest().create(testData.getUser());

        new CheckedProjects(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(testData.getProject());

        new CheckedProjects(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .delete(testData.getProject().getId());

        new UncheckedProjects(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .read(testData.getProject().getId())
                .then().assertThat().statusCode(HttpStatus.SC_NOT_FOUND)
                .body(Matchers.containsString("Could not find the entity requested"));
    }

}
