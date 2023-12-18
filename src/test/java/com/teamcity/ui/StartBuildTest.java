package com.teamcity.ui;

import com.teamcity.ui.pages.ProjectsPage;
import com.teamcity.ui.pages.admin.CreateBuildTypeStepPage;
import org.testng.annotations.Test;

import static com.teamcity.api.enums.Endpoint.BUILD_TYPES;
import static com.teamcity.api.enums.Endpoint.PROJECTS;

public class StartBuildTest extends BaseUiTest {

    @Test(description = "User should be able to create build type step and start build")
    public void userCreatesBuildTypeStepAndStartsBuildTest() {
        checkedSuperUser.getRequest(PROJECTS).create(testData.getProject());
        checkedSuperUser.getRequest(BUILD_TYPES).create(testData.getBuildType());
        loginAs(testData.getUser());

        CreateBuildTypeStepPage.open(testData.getBuildType().getId())
                .createCommandLineBuildStep("echo 'Hello World!'");

        ProjectsPage.open()
                .verifyProjectAndBuildType(testData.getProject().getName(), testData.getBuildType().getName())
                .runBuildAndWaitUntilItIsFinished();
    }

}
