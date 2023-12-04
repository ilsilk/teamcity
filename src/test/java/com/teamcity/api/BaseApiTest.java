package com.teamcity.api;

import com.teamcity.api.generators.TestData;
import com.teamcity.api.generators.TestDataGenerator;
import com.teamcity.api.generators.TestDataStorage;
import com.teamcity.api.models.ServerAuthSettings;
import com.teamcity.api.requests.CheckedRequests;
import com.teamcity.api.requests.UncheckedRequests;
import com.teamcity.api.requests.checked.CheckedServerAuthSettings;
import com.teamcity.api.spec.Specifications;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

public class BaseApiTest extends BaseTest {

    protected TestDataStorage testDataStorage;
    protected TestData testData;
    protected CheckedRequests checkedSuperUser = new CheckedRequests(Specifications.getSpec().superUserSpec());
    protected UncheckedRequests uncheckedSuperUser = new UncheckedRequests(Specifications.getSpec().superUserSpec());
    private boolean perProjectPermissions;

    @BeforeSuite(alwaysRun = true)
    public void setUpSettings() {
        perProjectPermissions = new CheckedServerAuthSettings(Specifications.getSpec()
                .superUserSpec())
                .read(null)
                .getPerProjectPermissions();

        new CheckedServerAuthSettings(Specifications.getSpec()
                .superUserSpec())
                .update(null, ServerAuthSettings.builder()
                        .perProjectPermissions(true)
                        .modules(TestDataGenerator.generateAuthModules())
                        .build());
    }

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        testDataStorage = TestDataStorage.getStorage();
        testData = testDataStorage.addTestData();
    }

    @AfterMethod(alwaysRun = true)
    public void cleanUp() {
        testDataStorage.deleteTestData();
    }

    @AfterSuite(alwaysRun = true)
    public void cleanUpSettings() {
        new CheckedServerAuthSettings(Specifications.getSpec()
                .superUserSpec())
                .update(null, ServerAuthSettings.builder()
                        .perProjectPermissions(perProjectPermissions)
                        .modules(TestDataGenerator.generateAuthModules())
                        .build());
    }

}
