package com.teamcity.ui.pages.setup;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.teamcity.ui.pages.BasePage;

import java.time.Duration;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class FirstStartPage extends BasePage {

    private final SelenideElement restoreButton = $("#restoreButton");
    private final SelenideElement proceedButton = $("#proceedButton");
    private final SelenideElement dbTypeSelect = $("#dbType");
    private final SelenideElement acceptLicenseCheckbox = $("#accept");
    private final SelenideElement submitButton = $(".continueBlock > .submitButton");

    public FirstStartPage() {
        restoreButton.shouldBe(visible, LONG_WAITING);
    }

    public static FirstStartPage open() {
        return Selenide.open("/", FirstStartPage.class);
    }

    public FirstStartPage setupFirstStart() {
        proceedButton.click();
        dbTypeSelect.shouldBe(visible, LONG_WAITING);
        proceedButton.click();
        acceptLicenseCheckbox.should(exist, LONG_WAITING).scrollTo().click();
        submitButton.click();
        return this;
    }

}
