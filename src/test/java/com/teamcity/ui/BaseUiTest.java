package com.teamcity.ui;

import com.codeborne.selenide.Configuration;
import com.teamcity.BaseTest;
import com.teamcity.api.config.Config;
import com.teamcity.api.models.User;
import com.teamcity.ui.pages.LoginPage;
import org.testng.annotations.BeforeSuite;

import java.util.Map;

import static com.teamcity.api.enums.Endpoint.USERS;

public class BaseUiTest extends BaseTest {

    @BeforeSuite
    public void setupUiTests() {
        Configuration.browser = Config.getProperty("browser");
        Configuration.baseUrl = "http://" + Config.getProperty("host");
        Configuration.remote = Config.getProperty("remote");
        Configuration.browserCapabilities.setCapability("selenoid:options", Map.of(
                "enableVNC", true,
                "enableLog", true
        ));
        Configuration.downloadsFolder = "target/downloads";
        Configuration.reportsFolder = "target/reports/tests";
    }

    protected void loginAs(User user) {
        checkedSuperUser.getRequest(USERS).create(user);
        LoginPage.open().login(user);
    }

}
