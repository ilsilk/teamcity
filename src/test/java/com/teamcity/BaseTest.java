package com.teamcity;

import com.teamcity.api.enums.Endpoint;
import com.teamcity.api.generators.TestDataGenerator;
import com.teamcity.api.generators.TestDataStorage;
import com.teamcity.api.models.BaseModel;
import com.teamcity.api.requests.CheckedRequests;
import com.teamcity.api.requests.UncheckedRequests;
import com.teamcity.api.spec.Specifications;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.util.EnumMap;

public class BaseTest {

    protected final CheckedRequests checkedSuperUser = new CheckedRequests(Specifications.getSpec().superUserSpec());
    protected final UncheckedRequests uncheckedSuperUser = new UncheckedRequests(Specifications.getSpec().superUserSpec());
    protected EnumMap<Endpoint, BaseModel> testData;
    protected SoftAssertions softy;

    @BeforeMethod(alwaysRun = true)
    public void generateBaseTestData() {
        softy = new SoftAssertions();
        // Генерируем одну testData перед каждым тестом (так как она всегда нужна), без добавления ее в какое-то хранилище
        testData = TestDataGenerator.generate();
    }

    @AfterMethod(alwaysRun = true)
    public void deleteCreatedEntities() {
        TestDataStorage.getStorage().deleteCreatedEntities();
        softy.assertAll();
    }

}
