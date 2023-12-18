package com.teamcity.ui.pages.admin;

import com.codeborne.selenide.Selenide;

import static com.codeborne.selenide.Selenide.page;

public class CreateProjectPage extends CreateBasePage {

    private static final String PROJECT_SHOW_MODE = "createProjectMenu";

    public static CreateProjectPage open(String projectId) {
        Selenide.open(CREATE_URL.formatted(projectId, PROJECT_SHOW_MODE));
        return page(CreateProjectPage.class);
    }

    public CreateProjectPage createFrom(String url) {
        baseCreateFrom(url);
        return this;
    }

    public CreateProjectPage setupProject(String projectName, String buildTypeName) {
        projectNameInput.val(projectName);
        buildTypeNameInput.val(buildTypeName);
        submitButton.click();
        return this;
    }

}
