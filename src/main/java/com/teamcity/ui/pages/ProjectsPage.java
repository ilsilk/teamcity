package com.teamcity.ui.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import java.time.Duration;
import java.util.regex.Pattern;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.teamcity.ui.Selectors.byDataTest;
import static com.teamcity.ui.Selectors.byDataTestItemtype;

public class ProjectsPage extends BasePage {

    private static final String PROJECTS_URL = "/favorite/projects";
    private static final String SUCCESS_BUILD_STATUS = "Success";
    private final SelenideElement header = $(".App__router--BP > div");
    private final SelenideElement editProjectLink = $(".EditEntity__link--en");
    private final SelenideElement runButton = $(byDataTest("run-build"));
    private final SelenideElement buildType = $(".BuildTypeLine__link--os");
    private final SelenideElement buildTypeHeader = $(".BuildTypePageHeader__heading--De");
    private final SelenideElement buildDetailsButton = $(".BuildDetails__button--BC");
    private final SelenideElement buildStatusLink = $(".Build__status--bG > a");
    private final ElementsCollection projects = $$(byDataTestItemtype("project"));

    public ProjectsPage() {
        header.shouldBe(visible, BASE_WAITING);
    }

    public static ProjectsPage open() {
        return Selenide.open(PROJECTS_URL, ProjectsPage.class);
    }

    public ProjectsPage verifyProjectAndBuildType(String projectName, String buildName) {
        projects.findBy(exactText(projectName)).should(visible).click();
        runButton.shouldBe(visible, BASE_WAITING);
        buildType.shouldHave(exactText(buildName));
        return this;
    }

    public ProjectsPage runBuildAndWaitUntilItIsFinished() {
        buildType.click();
        buildTypeHeader.should(appear, BASE_WAITING);
        runButton.click();
        buildDetailsButton.should(appear, BASE_WAITING);
        buildStatusLink.shouldBe(exactText(SUCCESS_BUILD_STATUS), Duration.ofMinutes(3));
        return this;
    }

    public String getProjectId() {
        var pattern = Pattern.compile("projectId=(.*?)(?:&|$)");
        var matcher = pattern.matcher(editProjectLink.attr("href"));
        return matcher.find() ? matcher.group(1) : null;
    }

    public String getBuildId() {
        var href = buildStatusLink.attr("href");
        return href != null ? href.substring(href.lastIndexOf("/") + 1) : null;
    }

}
