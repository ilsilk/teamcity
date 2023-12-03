package com.teamcity.api.generators;

import java.util.ArrayList;
import java.util.List;

public final class TestDataStorage {

    private static TestDataStorage testDataStorage;
    private final List<TestData> testDataList;

    private TestDataStorage() {
        testDataList = new ArrayList<>();
    }

    public static TestDataStorage getStorage() {
        if (testDataStorage == null) {
            testDataStorage = new TestDataStorage();
        }
        return testDataStorage;
    }

    public TestData addTestData() {
        return addTestData(TestDataGenerator.generate());
    }

    public TestData addTestData(TestData testData) {
        getStorage().testDataList.add(testData);
        return testData;
    }

    public void deleteTestData() {
        getStorage().testDataList.forEach(TestData::delete);
        getStorage().testDataList.clear();
    }

}
