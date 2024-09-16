package com.teamcity;

import com.teamcity.api.generators.TestDataStorage;
import com.teamcity.api.models.TestData;
import com.teamcity.api.requests.CheckedRequests;
import com.teamcity.api.requests.UncheckedRequests;
import com.teamcity.api.spec.Specifications;
import org.assertj.core.api.SoftAssertions;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import static com.teamcity.api.generators.TestDataGenerator.generate;

public class BaseTest {

    protected final CheckedRequests checkedSuperUser = new CheckedRequests(Specifications.getSpec().superUserSpec());
    protected final UncheckedRequests uncheckedSuperUser = new UncheckedRequests(Specifications.getSpec().superUserSpec());
    protected TestData testData;
    protected SoftAssertions softy;

    @BeforeMethod(alwaysRun = true)
    public void generateBaseTestData() {
        // Генерируем одну testData перед каждым тестом (так как она всегда нужна), без добавления ее в какое-то хранилище
        testData = generate();
        softy = new SoftAssertions();
    }

    @AfterMethod(alwaysRun = true)
    public void deleteCreatedEntities(ITestResult testResult) {
        try {
            softy.assertAll();
        } catch (AssertionError e) {
            testResult.setThrowable(e);
            testResult.setStatus(ITestResult.FAILURE);
        }
        TestDataStorage.getStorage().deleteCreatedEntities();
    }

}
