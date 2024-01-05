package com.teamcity.ui.pages.admin;

import com.codeborne.selenide.SelenideElement;
import com.teamcity.ui.pages.BasePage;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.appear;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

// Создали родительский класс для страниц создания проекта и билд конфигурации, так как они имеют схожий шаблон
public abstract class CreateBasePage extends BasePage {

    protected static final String CREATE_URL = "/admin/createObjectMenu.html?projectId=%s&showMode=%s";
    protected final SelenideElement projectNameInput = $("#projectName");
    protected final SelenideElement buildTypeNameInput = $("#buildTypeName");
    private final SelenideElement urlInput = $("#url");
    private final SelenideElement connectionSuccessfulMessage = $(".connectionSuccessful");
    private final SelenideElement projectNameError = $("#error_projectName");
    private final SelenideElement buildTypeNameError = $("#error_buildTypeName");

    public CreateBasePage() {
        submitButton.shouldBe(visible, BASE_WAITING);
    }

    protected abstract CreateBasePage createFrom(String url);

    protected void baseCreateFrom(String url) {
        urlInput.val(url);
        submitButton.click();
        connectionSuccessfulMessage.should(appear, BASE_WAITING);
    }

    @Step("Verify project name error")
    public CreateBasePage verifyProjectNameError() {
        projectNameError.shouldBe(visible, BASE_WAITING);
        return this;
    }

    @Step("Verify build type name error")
    public CreateBasePage verifyBuildTypeNameError() {
        buildTypeNameError.shouldBe(visible, BASE_WAITING);
        return this;
    }

}
