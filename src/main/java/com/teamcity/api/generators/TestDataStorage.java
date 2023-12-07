package com.teamcity.api.generators;

import com.teamcity.api.enums.Endpoint;
import com.teamcity.api.requests.unchecked.UncheckedRequest;
import com.teamcity.api.spec.Specifications;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

public final class TestDataStorage {

    private static TestDataStorage testDataStorage;
    private final EnumMap<Endpoint, List<String>> createdEntitiesMap;

    private TestDataStorage() {
        createdEntitiesMap = new EnumMap<>(Endpoint.class);
    }

    public static TestDataStorage getStorage() {
        if (testDataStorage == null) {
            testDataStorage = new TestDataStorage();
        }
        return testDataStorage;
    }

    public void addCreatedEntity(Endpoint endpoint, String id) {
        createdEntitiesMap.computeIfAbsent(endpoint, key -> new ArrayList<>()).add(id);
    }

    public void deleteTestData() {
        createdEntitiesMap.forEach((endpoint, ids) -> ids.forEach(id ->
                new UncheckedRequest(Specifications.getSpec().superUserSpec(), endpoint).delete(id)));
        createdEntitiesMap.clear();
    }

}
