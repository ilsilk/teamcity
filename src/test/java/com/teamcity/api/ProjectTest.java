package com.teamcity.api;

import com.teamcity.api.requests.checked.CheckedProject;
import com.teamcity.api.requests.unchecked.UncheckedProject;
import com.teamcity.api.spec.Specifications;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

public class ProjectTest extends BaseApiTest {

    @Test(description = "User should be able to create project")
    public void userCreatesProjectTest() {
        checkedSuperUser.getUserRequest().create(testData.getUser());

        var project = new CheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(testData.getProject());

        softy.assertThat(project.getId()).isEqualTo(testData.getProject().getId());
    }

    @Test(description = "User should be able to delete project")
    public void userDeletesProjectTest() {
        checkedSuperUser.getUserRequest().create(testData.getUser());

        var project = new CheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(testData.getProject());

        new CheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .delete(project.getId());

        new UncheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .read(project.getId())
                .then().assertThat().statusCode(HttpStatus.SC_NOT_FOUND)
                .body(Matchers.containsString("Could not find the entity requested"));
    }

}
