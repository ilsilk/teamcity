package com.teamcity.api;

import com.teamcity.api.generators.TestDataStorage;
import com.teamcity.api.requests.CheckedRequests;
import com.teamcity.api.requests.UncheckedRequests;
import com.teamcity.api.spec.Specifications;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeTest;

public class BaseApiTest extends BaseTest {

    protected TestDataStorage testDataStorage;
    protected CheckedRequests checkedSuperUser = new CheckedRequests(Specifications.getSpec().superUserSpec());
    protected UncheckedRequests uncheckedSuperUser = new UncheckedRequests(Specifications.getSpec().superUserSpec());

    @BeforeTest
    public void setUp() {
        testDataStorage = TestDataStorage.getStorage();
    }

    @AfterMethod
    public void cleanUp() {
        testDataStorage.deleteTestData();
    }

}
