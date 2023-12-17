package com.teamcity.ui.pages;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.teamcity.api.models.User;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.page;

public class LoginPage extends BasePage {

    private static final String LOGIN_URL = "/login.html";
    private final SelenideElement usernameInput = $("#username");
    private final SelenideElement passwordInput = $("#password");
    private final SelenideElement loginButton = $(".loginButton");

    public static LoginPage open() {
        Selenide.open(LOGIN_URL);
        return page(LoginPage.class);
    }

    public void login(User user) {
        usernameInput.val(user.getUsername());
        passwordInput.val(user.getPassword());
        loginButton.click();
    }

}
