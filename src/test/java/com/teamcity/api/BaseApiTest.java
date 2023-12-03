package com.teamcity.api;

import com.teamcity.api.generators.TestData;
import com.teamcity.api.generators.TestDataStorage;
import com.teamcity.api.requests.CheckedRequests;
import com.teamcity.api.requests.UncheckedRequests;
import com.teamcity.api.spec.Specifications;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class BaseApiTest extends BaseTest {

    protected TestDataStorage testDataStorage;
    protected TestData testData;
    protected CheckedRequests checkedSuperUser = new CheckedRequests(Specifications.getSpec().superUserSpec());
    protected UncheckedRequests uncheckedSuperUser = new UncheckedRequests(Specifications.getSpec().superUserSpec());

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        testDataStorage = TestDataStorage.getStorage();
        testData = testDataStorage.addTestData();
    }

    @AfterMethod(alwaysRun = true)
    public void cleanUp() {
        testDataStorage.deleteTestData();
    }

}
