package com.teamcity.ui.pages;

import com.codeborne.selenide.Selenide;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.page;

public class ProjectsPage extends BasePage {

    private static final String PROJECTS_URL = "/favorite/projects";

    public ProjectsPage() {
        heading.shouldBe(visible, Duration.ofSeconds(10));
    }

    public static ProjectsPage open() {
        Selenide.open(PROJECTS_URL);
        return page(ProjectsPage.class);
    }

}
