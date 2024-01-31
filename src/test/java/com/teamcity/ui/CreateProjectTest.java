package com.teamcity.ui;

import com.teamcity.api.models.BuildType;
import com.teamcity.api.models.NewProjectDescription;
import com.teamcity.api.models.Project;
import com.teamcity.api.requests.checked.CheckedBase;
import com.teamcity.api.spec.Specifications;
import com.teamcity.ui.pages.ProjectsPage;
import com.teamcity.ui.pages.admin.CreateProjectPage;
import com.teamcity.ui.pages.admin.EditBuildTypePage;
import io.qameta.allure.Feature;
import org.testng.annotations.Test;

import static com.teamcity.api.enums.Endpoint.*;

@Feature("Project")
public class CreateProjectTest extends BaseUiTest {

    @Test(description = "User should be able to create project", groups = {"Regression"})
    public void userCreatesProject() {
        loginAs(testData.get(USERS));

        CreateProjectPage.open(((NewProjectDescription) testData.get(PROJECTS)).getParentProject().getLocator())
                .createFrom(GIT_URL)
                .setupProject(((NewProjectDescription) testData.get(PROJECTS)).getName(), ((BuildType) testData.get(BUILD_TYPES)).getName());
        var createdBuildTypeId = EditBuildTypePage.open().getBuildTypeId();

        var checkedBuildTypeRequest = new CheckedBase(Specifications.getSpec()
                .authSpec(testData.get(USERS)), BUILD_TYPES);
        var buildType = (BuildType) checkedBuildTypeRequest.read(createdBuildTypeId);
        softy.assertThat(buildType.getProject().getName()).as("projectName").isEqualTo(((NewProjectDescription) testData.get(PROJECTS)).getName());
        softy.assertThat(buildType.getName()).as("buildTypeName").isEqualTo(((BuildType) testData.get(BUILD_TYPES)).getName());
        // Добавляем созданную сущность в сторедж, чтобы автоматически удалить ее в конце теста логикой, реализованной в API части

        var createdProjectId = ProjectsPage.open()
                .verifyProjectAndBuildType(((NewProjectDescription) testData.get(PROJECTS)).getName(), ((BuildType) testData.get(BUILD_TYPES)).getName())
                .getProjectId();
        var checkedProjectRequest = new CheckedBase(Specifications.getSpec()
                .authSpec(testData.get(USERS)), PROJECTS);
        var project = (Project) checkedProjectRequest.read(createdProjectId);
        softy.assertThat(project.getName()).as("projectName").isEqualTo(((NewProjectDescription) testData.get(PROJECTS)).getName());
    }

    @Test(description = "User should not be able to create project without name", groups = {"Regression"})
    public void userCreatesProjectWithoutName() {
        loginAs(testData.get(USERS));

        CreateProjectPage.open(((NewProjectDescription) testData.get(PROJECTS)).getParentProject().getLocator())
                .createFrom(GIT_URL)
                .setupProject("", ((BuildType) testData.get(BUILD_TYPES)).getName())
                .verifyProjectNameError();
    }

}
