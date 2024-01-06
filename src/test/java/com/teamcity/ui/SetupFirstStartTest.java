package com.teamcity.ui;

import com.teamcity.ui.pages.setup.FirstStartPage;
import com.teamcity.ui.pages.setup.UnauthorizedAgentsPage;
import org.testng.annotations.Test;

public class SetupFirstStartTest extends BaseUiTest {

    @Test
    public void setupTeamCityServerTest() {
        FirstStartPage.open().setupFirstStart();
    }

    @Test
    public void setupTeamCityAgentTest() {
        loginAs(testData.getUser());
        UnauthorizedAgentsPage.open().authorizeAgent();
    }

}
