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
    public void userCreatesBuildTypeStepAndStartsBuildTest() {
        checkedSuperUser.getRequest(PROJECTS).create(testData.project());
        checkedSuperUser.getRequest(BUILD_TYPES).create(testData.buildType());
        loginAs(testData.user());

        CreateBuildTypeStepPage.open(testData.buildType().id())
                .createCommandLineBuildStep("echo 'Hello World!'");

        // Тесты реализованы по паттерну fluent page object, поэтому эта запись выглядит как билдер, в одну строчку
        var createdBuildId = ProjectsPage.open()
                .verifyProjectAndBuildType(testData.project().name(), testData.buildType().name())
                .runBuildAndWaitUntilItIsFinished()
                .getBuildId();
        var checkedBuildRequest = new CheckedBase(Specifications.getSpec()
                .authSpec(testData.user()), BUILDS);
        // Каждое действие на UI всегда проверяется через API
        var build = (Build) checkedBuildRequest.read(createdBuildId);
        softy.assertThat(build.state()).as("buildState").isEqualTo("finished");
        softy.assertThat(build.status()).as("buildStatus").isEqualTo("SUCCESS");
    }

}
