package com.teamcity.ui.pages.admin;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.teamcity.ui.pages.BasePage;

import java.time.Duration;

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

    public CreateProjectPage() {
        submitButton.shouldBe(visible, Duration.ofSeconds(10));
    }

    public static CreateProjectPage open(String projectId) {
        Selenide.open(CREATE_PROJECT_URL.formatted(projectId));
        return page(CreateProjectPage.class);
    }

    public CreateProjectPage createProjectBy(String url) {
        urlInput.val(url);
        submitButton.click();
        connectionSuccessfulMessage.should(appear, Duration.ofSeconds(10));
        return this;
    }

    public EditBuildPage setupProject(String projectName, String buildTypeName) {
        projectNameInput.val(projectName);
        buildTypeNameInput.val(buildTypeName);
        submitButton.click();
        return page(EditBuildPage.class);
    }

}
