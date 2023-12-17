package com.teamcity.ui.pages.admin;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.teamcity.api.generators.RandomData;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static com.teamcity.ui.Selectors.byDataTest;

public class CreateBuildTypeStepPage extends EditBuildTypePage {

    private static final String NEW_BUILD_STEP_URL = "/admin/editRunType.html?id=buildType:%s&runnerId=__NEW_RUNNER__";
    private static final String COMMAND_LINE_RUNNER_TYPE = "Command Line";
    private static final String CUSTOM_SCRIPT_SELECTOR = "[id='script.content']";
    private final SelenideElement runnerItemFilterInput = $(byDataTest("runner-item-filter"));
    private final SelenideElement buildStepNameInput = $("#buildStepName");
    private final ElementsCollection runnerItems = $$(byDataTest("runner-item"));

    public CreateBuildTypeStepPage() {
        runnerItemFilterInput.shouldBe(visible, BASE_WAITING);
    }

    public static CreateBuildTypeStepPage open(String buildTypeId) {
        Selenide.open(NEW_BUILD_STEP_URL.formatted(buildTypeId));
        return page(CreateBuildTypeStepPage.class);
    }

    public EditBuildTypePage createCommandLineBuildStep(String customScript) {
        runnerItems.findBy(text(COMMAND_LINE_RUNNER_TYPE)).click();
        buildStepNameInput.shouldBe(visible, BASE_WAITING).val(RandomData.getString());
        executeJavaScript("document.querySelector('%s').value = '%s'".formatted(CUSTOM_SCRIPT_SELECTOR, customScript));
        submitButton.click();
        return this;
    }

}
