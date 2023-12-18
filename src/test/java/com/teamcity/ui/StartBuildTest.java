package com.teamcity.ui;

import com.teamcity.api.models.Build;
import com.teamcity.api.requests.checked.CheckedBase;
import com.teamcity.api.spec.Specifications;
import com.teamcity.ui.pages.ProjectsPage;
import com.teamcity.ui.pages.admin.CreateBuildTypeStepPage;
import org.testng.annotations.Test;

import static com.teamcity.api.enums.Endpoint.*;

public class StartBuildTest extends BaseUiTest {

    @Test(description = "User should be able to create build type step and start build")
    public void userCreatesBuildTypeStepAndStartsBuildTest() {
        checkedSuperUser.getRequest(PROJECTS).create(testData.getProject());
        checkedSuperUser.getRequest(BUILD_TYPES).create(testData.getBuildType());
        loginAs(testData.getUser());

        CreateBuildTypeStepPage.open(testData.getBuildType().getId())
                .createCommandLineBuildStep("echo 'Hello World!'");

        var createdBuildId = ProjectsPage.open()
                .verifyProjectAndBuildType(testData.getProject().getName(), testData.getBuildType().getName())
                .runBuildAndWaitUntilItIsFinished()
                .getBuildId();
        var checkedBuildRequest = new CheckedBase(Specifications.getSpec()
                .authSpec(testData.getUser()), BUILDS);
        var build = (Build) checkedBuildRequest.read(createdBuildId);
        softy.assertThat(build.getState()).isEqualTo("finished");
        softy.assertThat(build.getStatus()).isEqualTo("SUCCESS");
    }

}
