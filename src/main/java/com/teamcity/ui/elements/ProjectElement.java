package com.teamcity.ui.elements;

import com.codeborne.selenide.SelenideElement;
import lombok.Getter;

@Getter
public class ProjectElement extends BasePageElement {

    private final SelenideElement header;
    private final SelenideElement collapseButton;
    private final SelenideElement link;

    public ProjectElement(SelenideElement element) {
        super(element);
        header = find(".ring-global-ellipsis");
        collapseButton = find("button");
        link = find("a");
    }

}
