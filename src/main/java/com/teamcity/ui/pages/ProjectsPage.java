package com.teamcity.ui.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import java.util.regex.Pattern;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static com.teamcity.ui.Selectors.byDataTest;
import static com.teamcity.ui.Selectors.byDataTestItemtype;

public class ProjectsPage extends BasePage {

    private static final String PROJECTS_URL = "/favorite/projects";
    private final SelenideElement editProjectLink = $(".EditEntity__link--en");
    private final SelenideElement runButton = $(byDataTest("run-build"));
    private final SelenideElement buildType = $(".BuildTypeLine__link--os");
    private final ElementsCollection projects = $$(byDataTestItemtype("project"));

    public ProjectsPage() {
        heading.shouldBe(visible, baseWaiting);
    }

    public static ProjectsPage open() {
        Selenide.open(PROJECTS_URL);
        return page(ProjectsPage.class);
    }

    public ProjectsPage verifyProjectAndBuildType(String projectName, String buildName) {
        projects.findBy(exactText(projectName)).should(visible).click();
        runButton.shouldBe(visible, baseWaiting);
        buildType.shouldHave(exactText(buildName));
        return this;
    }

    public String getProjectId() {
        var pattern = Pattern.compile("projectId=(.*?)(?:&|$)");
        var matcher = pattern.matcher(editProjectLink.attr("href"));
        return matcher.find() ? matcher.group(1) : null;
    }

}
