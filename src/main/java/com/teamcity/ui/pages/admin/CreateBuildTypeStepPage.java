package com.teamcity.ui.pages.admin;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.teamcity.api.generators.RandomData;
import com.teamcity.ui.pages.BasePage;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static com.teamcity.ui.Selectors.byDataTest;

public class CreateBuildTypeStepPage extends BasePage {

    private static final String NEW_BUILD_STEP_URL = "/admin/editRunType.html?id=buildType:%s&runnerId=__NEW_RUNNER__";
    private static final String COMMAND_LINE_RUNNER_TYPE = "Command Line";
    private final SelenideElement runnerItemFilterInput = $(byDataTest("runner-item-filter"));
    private final SelenideElement buildStepNameInput = $("#buildStepName");
    private final SelenideElement customScriptLine = $(".CodeMirror-code");
    private final SelenideElement customScriptInput = $(".CodeMirror textarea");
    private final ElementsCollection runnerItems = $$(byDataTest("runner-item"));

    public CreateBuildTypeStepPage() {
        runnerItemFilterInput.shouldBe(visible, BASE_WAITING);
    }

    public static CreateBuildTypeStepPage open(String buildTypeId) {
        return Selenide.open(NEW_BUILD_STEP_URL.formatted(buildTypeId), CreateBuildTypeStepPage.class);
    }

    public EditBuildTypePage createCommandLineBuildStep(String customScript) {
        runnerItems.findBy(text(COMMAND_LINE_RUNNER_TYPE)).click();
        buildStepNameInput.shouldBe(visible, BASE_WAITING).val(RandomData.getString());
        customScriptLine.click();
        customScriptInput.sendKeys(customScript);
        submitButton.click();
        return page(EditBuildTypePage.class);
    }

}
