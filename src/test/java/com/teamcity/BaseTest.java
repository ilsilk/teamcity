package com.teamcity;

import com.teamcity.api.generators.TestData;
import com.teamcity.api.generators.TestDataGenerator;
import com.teamcity.api.generators.TestDataStorage;
import com.teamcity.api.requests.CheckedRequests;
import com.teamcity.api.requests.UncheckedRequests;
import com.teamcity.api.spec.Specifications;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class BaseTest {

    protected final CheckedRequests checkedSuperUser = new CheckedRequests(Specifications.getSpec().superUserSpec());
    protected final UncheckedRequests uncheckedSuperUser = new UncheckedRequests(Specifications.getSpec().superUserSpec());
    protected TestData testData;
    protected SoftAssertions softy;

    @BeforeMethod(alwaysRun = true)
    public void baseBeforeMethod() {
        softy = new SoftAssertions();
        testData = TestDataGenerator.generate();
    }

    @AfterMethod(alwaysRun = true)
    public void baseAfterMethod() {
        TestDataStorage.getStorage().deleteCreatedEntities();
        softy.assertAll();
    }

}
