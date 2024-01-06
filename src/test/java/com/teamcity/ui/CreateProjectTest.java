package com.teamcity.ui;

import com.teamcity.api.generators.TestDataStorage;
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
        loginAs(testData.getUser());

        CreateProjectPage.open(testData.getProject().getParentProject().getLocator())
                .createFrom(GIT_URL)
                .setupProject(testData.getProject().getName(), testData.getBuildType().getName());
        var createdBuildTypeId = EditBuildTypePage.open().getBuildTypeId();

        var checkedBuildTypeRequest = new CheckedBase(Specifications.getSpec()
                .authSpec(testData.getUser()), BUILD_TYPES);
        var buildType = (BuildType) checkedBuildTypeRequest.read(createdBuildTypeId);
        softy.assertThat(buildType.getName()).as("buildTypeName").isEqualTo(testData.getBuildType().getName());
        // Добавляем созданную сущность в сторедж, чтобы автоматически удалить ее в конце теста логикой, реализованной в API части
        TestDataStorage.getStorage().addCreatedEntity(BUILD_TYPES, createdBuildTypeId);

        var createdProjectId = ProjectsPage.open()
                .verifyProjectAndBuildType(testData.getProject().getName(), testData.getBuildType().getName())
                .getProjectId();
        var checkedProjectRequest = new CheckedBase(Specifications.getSpec()
                .authSpec(testData.getUser()), PROJECTS);
        var project = (Project) checkedProjectRequest.read(createdProjectId);
        softy.assertThat(project.getName()).as("projectName").isEqualTo(testData.getProject().getName());
        TestDataStorage.getStorage().addCreatedEntity(PROJECTS, createdProjectId);
    }

    @Test(description = "User should not be able to create project without name", groups = {"Regression"})
    public void userCreatesProjectWithoutName() {
        loginAs(testData.getUser());

        CreateProjectPage.open(testData.getProject().getParentProject().getLocator())
                .createFrom(GIT_URL)
                .setupProject("", testData.getBuildType().getName())
                .verifyProjectNameError();
    }

}
