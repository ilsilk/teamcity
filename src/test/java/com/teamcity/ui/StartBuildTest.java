package com.teamcity.ui;

import com.teamcity.api.models.Build;
import com.teamcity.api.models.BuildType;
import com.teamcity.api.models.NewProjectDescription;
import com.teamcity.api.models.Project;
import com.teamcity.api.requests.checked.CheckedBase;
import com.teamcity.api.spec.Specifications;
import com.teamcity.ui.pages.ProjectsPage;
import com.teamcity.ui.pages.admin.CreateBuildTypeStepPage;
import io.qameta.allure.Feature;
import org.testng.annotations.Test;

import static com.teamcity.api.enums.Endpoint.*;

@Feature("Start build")
public class StartBuildTest extends BaseUiTest {

    @Test(description = "User should be able to create build type step and start build", groups = {"Regression"})
    public void userCreatesBuildTypeStepAndStartsBuildTest() {
        checkedSuperUser.getRequest(PROJECTS).create(testData.get(PROJECTS));
        checkedSuperUser.getRequest(BUILD_TYPES).create(testData.get(BUILD_TYPES));
        loginAs(testData.get(USERS));

        CreateBuildTypeStepPage.open(((BuildType) testData.get(BUILD_TYPES)).getId())
                .createCommandLineBuildStep("echo 'Hello World!'");

        // Тесты реализованы по паттерну fluent page object, поэтому эта запись выглядит как билдер, в одну строчку
        var createdBuildId = ProjectsPage.open()
                .verifyProjectAndBuildType(((NewProjectDescription) testData.get(PROJECTS)).getName(), ((BuildType) testData.get(BUILD_TYPES)).getName())
                .runBuildAndWaitUntilItIsFinished()
                .getBuildId();
        var checkedBuildRequest = new CheckedBase(Specifications.getSpec()
                .authSpec(testData.get(USERS)), BUILDS);
        // Каждое действие на UI всегда проверяется через API
        var build = (Build) checkedBuildRequest.read(createdBuildId);
        softy.assertThat(build.getState()).as("buildState").isEqualTo("finished");
        softy.assertThat(build.getStatus()).as("buildStatus").isEqualTo("SUCCESS");
    }

}
