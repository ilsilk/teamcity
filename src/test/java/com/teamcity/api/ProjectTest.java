package com.teamcity.api;

import com.teamcity.api.requests.checked.CheckedProject;
import com.teamcity.api.spec.Specifications;
import org.testng.annotations.Test;

public class ProjectTest extends BaseApiTest {

    @Test(description = "User should be able to create project")
    public void createProjectPositiveTest() {
        checkedSuperUser.getUserRequest().create(testData.getUser());

        var project = new CheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(testData.getProject());

        softy.assertThat(project.getId()).isEqualTo(testData.getProject().getId());
    }

}
