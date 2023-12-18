package com.teamcity.ui.pages.admin;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.teamcity.ui.pages.BasePage;

import static com.codeborne.selenide.Condition.appear;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.page;

public class CreateBuildTypePage extends BasePage {

    private static final String CREATE_BUILD_TYPE_URL = "/admin/createObjectMenu.html?projectId=%s&showMode=createBuildTypeMenu";
    private final SelenideElement urlInput = $("#url");
    private final SelenideElement buildTypeNameInput = $("#buildTypeName");
    private final SelenideElement connectionSuccessfulMessage = $(".connectionSuccessful");
    private final SelenideElement buildTypeNameError = $("#error_buildTypeName");

    public CreateBuildTypePage() {
        submitButton.shouldBe(visible, BASE_WAITING);
    }

    public static CreateBuildTypePage open(String projectId) {
        Selenide.open(CREATE_BUILD_TYPE_URL.formatted(projectId));
        return page(CreateBuildTypePage.class);
    }

    public CreateBuildTypePage createBuildTypeFrom(String url) {
        urlInput.val(url);
        submitButton.click();
        connectionSuccessfulMessage.should(appear, BASE_WAITING);
        return this;
    }

    public CreateBuildTypePage setupBuildType(String buildTypeName) {
        buildTypeNameInput.val(buildTypeName);
        submitButton.click();
        return this;
    }

    public CreateBuildTypePage verifyBuildTypeNameError() {
        buildTypeNameError.shouldBe(visible);
        return this;
    }

}
