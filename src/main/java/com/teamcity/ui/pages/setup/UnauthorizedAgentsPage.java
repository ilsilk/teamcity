package com.teamcity.ui.pages.setup;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.teamcity.ui.pages.BasePage;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class UnauthorizedAgentsPage extends BasePage {

    private final SelenideElement authorizeAgentButton = $(".AuthorizeAgent__authorizeAgent--Xr > button");
    private final SelenideElement authorizeAgentModalWindowButton = $(".CommonForm__button--Nb");

    public UnauthorizedAgentsPage() {
        authorizeAgentButton.shouldBe(visible, LONG_WAITING);
    }

    @Step("Open unauthorized agents page")
    public static UnauthorizedAgentsPage open() {
        return Selenide.open("/agents/unauthorized", UnauthorizedAgentsPage.class);
    }

    @Step("Authorize agent")
    public UnauthorizedAgentsPage authorizeAgent() {
        authorizeAgentButton.click();
        authorizeAgentModalWindowButton.shouldBe(visible, BASE_WAITING).click();
        return this;
    }

}
