package com.teamcity.ui;

import lombok.experimental.UtilityClass;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selectors.byAttribute;
import static com.codeborne.selenide.Selectors.byCssSelector;

@UtilityClass
public final class Selectors {

    /* Оператор ~= ищет частичное вхождение. Например, byDataTest("runner-item") найдет элемент с атрибутом
    data-test="runner-item simpleRunner".
    От остальных кастомных By методов было решено отказаться, так как те же byId и byClassName можно реализовать
    короче с помощью css селектора */
    public static By byDataTest(String dataTest) {
        return byCssSelector("[data-test~='%s']".formatted(dataTest));
    }

    public static By byDataTestItemtype(String dataTestItemtype) {
        return byAttribute("data-test-itemtype", dataTestItemtype);
    }

}
