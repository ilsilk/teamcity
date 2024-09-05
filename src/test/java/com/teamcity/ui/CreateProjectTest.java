package com.teamcity.ui;

import com.teamcity.api.models.BuildType;
import com.teamcity.api.models.Project;
import com.teamcity.api.requests.checked.CheckedBase;
import com.teamcity.api.spec.Specifications;
import com.teamcity.ui.pages.ProjectsPage;
import com.teamcity.ui.pages.admin.CreateProjectPage;
import com.teamcity.ui.pages.admin.EditBuildTypePage;
import io.qameta.allure.Feature;
import org.testng.annotations.Test;

import static com.teamcity.api.enums.Endpoint.BUILD_TYPES;
import static com.teamcity.api.enums.Endpoint.PROJECTS;

@Feature("Project")
public class CreateProjectTest extends BaseUiTest {

    @Test(description = "User should be able to create project", groups = {"Regression"})
    public void userCreatesProject() {
        loginAs(testData.user());

        CreateProjectPage.open(testData.project().parentProject().locator())
                .createFrom(GIT_URL)
                .setupProject(testData.project().name(), (testData.buildType()).name());
        var createdBuildTypeId = EditBuildTypePage.open().getBuildTypeId();

        var checkedBuildTypeRequest = new CheckedBase(Specifications.getSpec()
                .authSpec(testData.user()), BUILD_TYPES);
        var buildType = (BuildType) checkedBuildTypeRequest.read(createdBuildTypeId);
        softy.assertThat(buildType.project().name()).as("projectName").isEqualTo(testData.project().name());
        softy.assertThat(buildType.name()).as("buildTypeName").isEqualTo(testData.buildType().name());
        // Добавляем созданную сущность в сторедж, чтобы автоматически удалить ее в конце теста логикой, реализованной в API части

        var createdProjectId = ProjectsPage.open()
                .verifyProjectAndBuildType(testData.project().name(), (testData.buildType()).name())
                .getProjectId();
        var checkedProjectRequest = new CheckedBase(Specifications.getSpec()
                .authSpec(testData.user()), PROJECTS);
        var project = (Project) checkedProjectRequest.read(createdProjectId);
        softy.assertThat(project.name()).as("projectName").isEqualTo(testData.project().name());
    }

    @Test(description = "User should not be able to create project without name", groups = {"Regression"})
    public void userCreatesProjectWithoutName() {
        loginAs(testData.user());

        CreateProjectPage.open(testData.project().parentProject().locator())
                .createFrom(GIT_URL)
                .setupProject("", (testData.buildType()).name())
                .verifyProjectNameError("Project name must not be empty");
    }

}
