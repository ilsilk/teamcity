package com.teamcity.ui;

import org.openqa.selenium.By;

import static com.codeborne.selenide.Selectors.byAttribute;
import static com.codeborne.selenide.Selectors.byCssSelector;

public final class Selectors {

    private Selectors() {
    }

    public static By byDataTest(String dataTest) {
        return byCssSelector("[data-test~='%s'".formatted(dataTest));
    }

    public static By byDataTestItemtype(String dataTestItemtype) {
        return byAttribute("data-test-itemtype", dataTestItemtype);
    }

}
