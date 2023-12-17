package com.teamcity.ui.pages.admin;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.teamcity.api.generators.RandomData;
import com.teamcity.ui.pages.BasePage;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static com.teamcity.ui.Selectors.byDataTest;

public class EditBuildPage extends BasePage {

    private static final String COMMAND_LINE_RUNNER_TYPE = "Command Line";
    private static final String CUSTOM_SCRIPT_INPUT_SELECTOR = ".CodeMirror-code pre > span";
    private final SelenideElement configureBuildStepsLink = $("#discoveredRunners a");
    private final SelenideElement runnerItemFilterInput = $(byDataTest("runner-item-filter"));
    private final SelenideElement buildStepNameInput = $("#buildStepName");
    private final ElementsCollection runnerItems = $$(byDataTest("runner-item"));

    public void configureCommandLineBuildSteps(String customScript) {
        configureBuildStepsLink.shouldBe(visible, Duration.ofSeconds(10)).click();
        runnerItemFilterInput.shouldBe(visible, Duration.ofSeconds(10));
        runnerItems.findBy(text(COMMAND_LINE_RUNNER_TYPE)).click();
        buildStepNameInput.shouldBe(visible, Duration.ofSeconds(10)).val(RandomData.getString());
        $(CUSTOM_SCRIPT_INPUT_SELECTOR).click();
        executeJavaScript("document.querySelector('%s').innerText = '%s'".formatted(CUSTOM_SCRIPT_INPUT_SELECTOR, customScript));
        submitButton.click();
    }

}
