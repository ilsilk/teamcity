package com.teamcity.ui.pages.admin;

import com.codeborne.selenide.SelenideElement;
import com.teamcity.ui.pages.BasePage;

import java.util.regex.Pattern;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.WebDriverRunner.url;

public class EditBuildTypePage extends BasePage {

    private final SelenideElement generalTab = $("#general_Tab");

    public EditBuildTypePage() {
        generalTab.shouldBe(visible, BASE_WAITING);
    }

    public String getBuildTypeId() {
        var pattern = Pattern.compile("buildType:(.*?)(?:&|$)");
        var matcher = pattern.matcher(url());
        return matcher.find() ? matcher.group(1) : null;
    }

}
