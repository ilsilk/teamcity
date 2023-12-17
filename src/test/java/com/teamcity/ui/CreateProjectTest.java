package com.teamcity.ui;

import com.teamcity.api.generators.TestDataStorage;
import com.teamcity.ui.pages.ProjectsPage;
import com.teamcity.ui.pages.admin.CreateProjectPage;
import org.testng.annotations.Test;

import static com.teamcity.api.enums.Endpoint.BUILD_TYPES;
import static com.teamcity.api.enums.Endpoint.PROJECTS;

public class CreateProjectTest extends BaseUiTest {

    @Test(description = "User should be able to create project")
    public void userCreatesProject() {
        var url = "https://github.com/selenide/selenide.git";
        loginAs(testData.getUser());

        var createdBuildType = CreateProjectPage.open(testData.getProject().getParentProject().getLocator())
                .createProjectBy(url)
                .setupProject(testData.getProject().getName(), testData.getBuildType().getName())
                .getBuildTypeId();
        TestDataStorage.getStorage().addCreatedEntity(BUILD_TYPES, createdBuildType);

        var createdProject = ProjectsPage.open()
                .verifyProjectAndBuildType(testData.getProject().getName(), testData.getBuildType().getName())
                .getProjectId();
        TestDataStorage.getStorage().addCreatedEntity(PROJECTS, createdProject);
    }

}
