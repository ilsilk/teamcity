package com.teamcity.ui;

import com.teamcity.api.models.Build;
import com.teamcity.api.requests.checked.CheckedBase;
import com.teamcity.api.spec.Specifications;
import com.teamcity.ui.pages.ProjectsPage;
import com.teamcity.ui.pages.admin.CreateBuildTypeStepPage;
import io.qameta.allure.Feature;
import org.testng.annotations.Test;

import static com.teamcity.api.enums.Endpoint.BUILDS;
import static com.teamcity.api.enums.Endpoint.BUILD_TYPES;
import static com.teamcity.api.enums.Endpoint.PROJECTS;

@Feature("Start build")
public class StartBuildTest extends BaseUiTest {

    @Test(description = "User should be able to create build type step and start build", groups = {"Regression"})
    public void userCreatesBuildTypeStepAndStartsBuildTest(String browser) {
        checkedSuperUser.getRequest(PROJECTS).create(testData.getNewProjectDescription());
        checkedSuperUser.getRequest(BUILD_TYPES).create(testData.getBuildType());
        loginAs(testData.getUser());

        CreateBuildTypeStepPage.open(testData.getBuildType().getId())
                .createCommandLineBuildStep("echo 'Hello World!'");

        // Тесты реализованы по паттерну fluent page object, поэтому эта запись выглядит как билдер, в одну строчку
        var createdBuildId = ProjectsPage.open()
                .verifyProjectAndBuildType(testData.getProject().getName(), testData.getBuildType().getName())
                .runBuildAndWaitUntilItIsFinished()
                .getBuildId();
        var checkedBuildRequest = new CheckedBase<Build>(Specifications.getSpec()
                .authSpec(testData.getUser()), BUILDS);
        // Каждое действие на UI всегда проверяется через API
        var build = checkedBuildRequest.read(createdBuildId);
        softy.assertThat(build.getState()).as("buildState").isEqualTo("finished");
        softy.assertThat(build.getStatus()).as("buildStatus").isEqualTo("SUCCESS");
    }

}
