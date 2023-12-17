package com.teamcity.ui.pages;

import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;
import static com.teamcity.ui.Selectors.byDataTest;

public abstract class BasePage {

    protected static final Duration baseWaiting = Duration.ofSeconds(10);
    protected final SelenideElement submitButton = $(".saveButtonsBlock > .submitButton");
    protected final SelenideElement header = $(byDataTest("overview-header"));
    protected final SelenideElement heading = header.find("h1");

}
