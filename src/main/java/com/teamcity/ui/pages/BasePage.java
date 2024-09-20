package com.teamcity.ui.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.teamcity.ui.elements.BasePageElement;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

import static com.codeborne.selenide.Selenide.$;

public abstract class BasePage {

    protected static final Duration BASE_WAITING = Duration.ofSeconds(30);
    protected static final Duration LONG_WAITING = Duration.ofMinutes(3);
    /* Метод Selenide.element вызывает внутри себя этот метод $. Официальная документация рекомендует использовать его
    для гораздо более компактной записи.
    Здесь используется css селектор: элемент с классом submitButton, который является первым дочерним элементом
    у элемента с классом saveButtonsBlock */
    protected final SelenideElement submitButton = $(".saveButtonsBlock > .submitButton");

    protected <T extends BasePageElement> List<T> generatePageElements(ElementsCollection collection,
                                                                       Function<SelenideElement, T> creator) {
        return collection.stream()
                .map(creator)
                .toList();
    }

}
