package com.teamcity.ui;

import com.teamcity.ui.pages.LoginPage;
import com.teamcity.ui.pages.setup.FirstStartPage;
import com.teamcity.ui.pages.setup.UnauthorizedAgentsPage;
import io.qameta.allure.Feature;
import org.testng.annotations.Test;

@Feature("Setup")
public class SetupFirstStartTest extends BaseUiTest {

    @Test(groups = {"Setup"})
    public void setupTeamCityServerTest() {
        FirstStartPage.open().setupFirstStart();
    }

    @Test(groups = {"Setup"})
    public void setupTeamCityAgentTest() {
        LoginPage.openSuperUser().login();
        UnauthorizedAgentsPage.open().authorizeAgent();
    }

}
