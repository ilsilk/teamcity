package com.teamcity.ui.pages.admin;

import com.codeborne.selenide.Selenide;

import static com.codeborne.selenide.Selenide.page;

public class CreateBuildTypePage extends CreateBasePage {

    private static final String BUILD_TYPE_SHOW_MODE = "createBuildTypeMenu";

    public static CreateBuildTypePage open(String projectId) {
        Selenide.open(CREATE_URL.formatted(projectId, BUILD_TYPE_SHOW_MODE));
        return page(CreateBuildTypePage.class);
    }

    public CreateBuildTypePage createFrom(String url) {
        baseCreateFrom(url);
        return this;
    }

    public CreateBuildTypePage setupBuildType(String buildTypeName) {
        buildTypeNameInput.val(buildTypeName);
        submitButton.click();
        return this;
    }

}
