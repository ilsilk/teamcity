package com.teamcity.ui;

import com.teamcity.ui.pages.ProjectsPage;
import com.teamcity.ui.pages.admin.CreateProjectPage;
import org.testng.annotations.Test;

public class CreateProjectTest extends BaseUiTest {

    @Test(description = "User should be able to create project")
    public void userCreatesProject() {
        var url = "https://github.com/selenide/selenide.git";

        loginAs(testData.getUser());
        CreateProjectPage.open(testData.getProject().getParentProject().getLocator())
                .createProjectBy(url)
                .setupProject(testData.getProject().getName(), testData.getBuildType().getName())
                .configureCommandLineBuildSteps("echo Hello World!");
        ProjectsPage.open();
    }

}
