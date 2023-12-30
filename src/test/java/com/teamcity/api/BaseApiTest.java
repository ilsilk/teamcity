package com.teamcity.api;

import com.teamcity.BaseTest;
import com.teamcity.api.generators.TestDataGenerator;
import com.teamcity.api.models.ServerAuthSettings;
import com.teamcity.api.requests.checked.CheckedServerAuthSettings;
import com.teamcity.api.spec.Specifications;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

public class BaseApiTest extends BaseTest {

    private final CheckedServerAuthSettings checkedServerAuthSettings = new CheckedServerAuthSettings(
            Specifications.getSpec().superUserSpec());
    private boolean perProjectPermissions;

    @BeforeSuite(alwaysRun = true)
    public void setUpSettings() {
        perProjectPermissions = checkedServerAuthSettings.read(null)
                .getPerProjectPermissions();

        checkedServerAuthSettings.update(null, ServerAuthSettings.builder()
                .perProjectPermissions(true)
                .modules(TestDataGenerator.generateAuthModules())
                .build());
    }

    @AfterSuite(alwaysRun = true)
    public void cleanUpSettings() {
        checkedServerAuthSettings.update(null, ServerAuthSettings.builder()
                .perProjectPermissions(perProjectPermissions)
                .modules(TestDataGenerator.generateAuthModules())
                .build());
    }

}
