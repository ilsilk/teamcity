package com.teamcity.api;

import com.teamcity.api.generators.RandomData;
import com.teamcity.api.generators.TestDataGenerator;
import com.teamcity.api.models.Project;
import com.teamcity.api.requests.checked.CheckedRequest;
import com.teamcity.api.requests.unchecked.UncheckedRequest;
import com.teamcity.api.spec.Specifications;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import static com.teamcity.api.enums.Endpoint.PROJECTS;
import static com.teamcity.api.enums.Endpoint.USERS;

public class ProjectTest extends BaseApiTest {

    private static final int PROJECT_ID_CHARACTERS_LIMIT = 225;

    @Test(description = "User should be able to create project")
    public void userCreatesProjectTest() {
        checkedSuperUser.getRequest(USERS).create(testData.getUser());

        var checkedProjectRequest = new CheckedRequest(Specifications.getSpec()
                .authSpec(testData.getUser()), PROJECTS);
        var project = (Project) checkedProjectRequest.create(testData.getProject());

        softy.assertThat(project.getId()).isEqualTo(testData.getProject().getId());
    }

    @Test(description = "User should not be able to create two projects with the same id")
    public void userCreatesTwoProjectsWithSameIdTest() {
        var firstTestData = testData;
        var secondTestData = TestDataGenerator.generate();

        checkedSuperUser.getRequest(USERS).create(firstTestData.getUser());

        var checkedProjectRequest = new CheckedRequest(Specifications.getSpec()
                .authSpec(testData.getUser()), PROJECTS);
        checkedProjectRequest.create(firstTestData.getProject());

        secondTestData.getProject().setId(firstTestData.getProject().getId());

        var uncheckedProjectRequest = new UncheckedRequest(Specifications.getSpec()
                .authSpec(testData.getUser()), PROJECTS);
        uncheckedProjectRequest.create(secondTestData.getProject())
                .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test(description = "User should not be able to create project with id exceeding the limit")
    public void userCreatesProjectWithIdExceedingLimitTest() {
        checkedSuperUser.getRequest(USERS).create(testData.getUser());

        testData.getProject().setId(RandomData.getString(PROJECT_ID_CHARACTERS_LIMIT + 1));

        var uncheckedProjectRequest = new UncheckedRequest(Specifications.getSpec()
                .authSpec(testData.getUser()), PROJECTS);
        uncheckedProjectRequest.create(testData.getProject())
                .then().assertThat().statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);

        testData.getProject().setId(RandomData.getString(PROJECT_ID_CHARACTERS_LIMIT));

        var checkedProjectRequest = new CheckedRequest(Specifications.getSpec()
                .authSpec(testData.getUser()), PROJECTS);
        checkedProjectRequest.create(testData.getProject());
    }

    @Test(description = "Unauthorized user should not be able to create project")
    public void unauthorizedUserCreatesProjectTest() {
        var uncheckedProjectRequest = new UncheckedRequest(Specifications.getSpec()
                .unauthSpec(), PROJECTS);
        uncheckedProjectRequest.create(testData.getProject())
                .then().assertThat().statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body(Matchers.containsString("Authentication required"));

        uncheckedSuperUser.getRequest(PROJECTS)
                .read(testData.getProject().getId())
                .then().assertThat().statusCode(HttpStatus.SC_NOT_FOUND)
                .body(Matchers.containsString("Could not find the entity requested"));
    }

    @Test(description = "User should be able to delete project")
    public void userDeletesProjectTest() {
        checkedSuperUser.getRequest(USERS).create(testData.getUser());

        var checkedProjectRequest = new CheckedRequest(Specifications.getSpec()
                .authSpec(testData.getUser()), PROJECTS);
        checkedProjectRequest.create(testData.getProject());
        checkedProjectRequest.delete(testData.getProject().getId());

        var uncheckedProjectRequest = new UncheckedRequest(Specifications.getSpec()
                .authSpec(testData.getUser()), PROJECTS);
        uncheckedProjectRequest.read(testData.getProject().getId())
                .then().assertThat().statusCode(HttpStatus.SC_NOT_FOUND)
                .body(Matchers.containsString("Could not find the entity requested"));
    }

}
