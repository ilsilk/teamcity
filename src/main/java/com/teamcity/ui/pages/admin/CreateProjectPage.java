package com.teamcity.ui.pages.admin;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.teamcity.ui.pages.BasePage;

import static com.codeborne.selenide.Condition.appear;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.page;

public class CreateProjectPage extends BasePage {

    private static final String CREATE_PROJECT_URL = "/admin/createObjectMenu.html?projectId=%s&showMode=createProjectMenu";
    private final SelenideElement urlInput = $("#url");
    private final SelenideElement projectNameInput = $("#projectName");
    private final SelenideElement buildTypeNameInput = $("#buildTypeName");
    private final SelenideElement connectionSuccessfulMessage = $(".connectionSuccessful");
    private final SelenideElement projectNameError = $("#error_projectName");

    public CreateProjectPage() {
        submitButton.shouldBe(visible, BASE_WAITING);
    }

    public static CreateProjectPage open(String projectId) {
        Selenide.open(CREATE_PROJECT_URL.formatted(projectId));
        return page(CreateProjectPage.class);
    }

    public CreateProjectPage createProjectFrom(String url) {
        urlInput.val(url);
        submitButton.click();
        connectionSuccessfulMessage.should(appear, BASE_WAITING);
        return this;
    }

    public CreateProjectPage setupProject(String projectName, String buildTypeName) {
        projectNameInput.val(projectName);
        buildTypeNameInput.val(buildTypeName);
        submitButton.click();
        return this;
    }

    public CreateProjectPage verifyProjectNameError() {
        projectNameError.shouldBe(visible);
        return this;
    }

}
