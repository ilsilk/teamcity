package com.teamcity.ui;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import com.teamcity.BaseTest;
import com.teamcity.api.config.Config;
import com.teamcity.api.models.User;
import com.teamcity.ui.pages.LoginPage;
import io.qameta.allure.selenide.AllureSelenide;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.util.Map;

import static com.teamcity.api.enums.Endpoint.USERS;
import static io.qameta.allure.Allure.label;

public class BaseUiTest extends BaseTest {

    protected static final String GIT_URL = "https://github.com/selenide/selenide.git";

    @BeforeSuite(alwaysRun = true)
    public void setupUiTests() {
        // Нет никакой необходимости писать класс-конфигуратор браузера, так как Selenide последних версий делает это из коробки
        Configuration.browser = Config.getProperty("browser");
        Configuration.baseUrl = "http://" + Config.getProperty("host");
        Configuration.remote = Config.getProperty("remote");
        // Проводим тестирование на фиксированном разрешении экрана
        Configuration.browserSize = "1920x1080";
        Configuration.browserCapabilities.setCapability("selenoid:options", Map.of(
                "enableVNC", true,
                "enableLog", true
        ));
        Configuration.downloadsFolder = "target/downloads";
        Configuration.reportsFolder = "target/reports/tests";

        // Подключаем степы и скриншоты в Allure репорте
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide()
                .screenshots(true)
                .savePageSource(true)
                .includeSelenideSteps(true));

    }

    @BeforeMethod(alwaysRun = true)
    public void addLabel() {
        label("browser", Configuration.browser);
    }

    @AfterMethod(alwaysRun = true)
    public void closeWebDriver() {
        // Перезапускаем браузер после каждого теста
        Selenide.closeWebDriver();
    }

    protected void loginAs(User user) {
        checkedSuperUser.getRequest(USERS).create(user);
        LoginPage.open().login(user);
    }

}
