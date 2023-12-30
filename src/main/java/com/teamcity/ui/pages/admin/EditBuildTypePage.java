package com.teamcity.ui.pages.admin;

import com.codeborne.selenide.SelenideElement;
import com.teamcity.ui.pages.BasePage;

import java.util.regex.Pattern;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.WebDriverRunner.url;

public class EditBuildTypePage extends BasePage {

    private final SelenideElement generalTab = $("#general_Tab");
    /* При открытии страницы с редактированием билд конфигурации открываемая страница может выглядеть по-разному.
    Например, страница открывается после создания проекта через UI, причем ее состояние может быть разным, в зависимости
    от указанного git url. Также если зайти в редактирование билд конфигурации у ранее созданного проекта, то ее состояние
    тоже будет другим. Поэтому необходимо было завязаться на элемент-индикатор того, что страница полностью загрузилась,
    который присутствует в любом состоянии страницы. */
    private final SelenideElement headerHelpIcon = $("h2 + div > span");

    public EditBuildTypePage() {
        generalTab.shouldBe(visible, BASE_WAITING);
        headerHelpIcon.shouldBe(visible, BASE_WAITING);
    }

    // Получаем через UI айди созданной билд конфигурации
    public String getBuildTypeId() {
        var pattern = Pattern.compile("buildType:(.*?)(?:&|$)");
        var matcher = pattern.matcher(url());
        return matcher.find() ? matcher.group(1) : null;
    }

}
