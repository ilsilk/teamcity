package com.teamcity.api.generators;

import com.teamcity.api.enums.Endpoint;
import com.teamcity.api.models.BaseModel;
import com.teamcity.api.requests.unchecked.UncheckedRequest;
import com.teamcity.api.spec.Specifications;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

public final class TestDataStorage {

    private static TestDataStorage testDataStorage;
    private final EnumMap<Endpoint, List<BaseModel>> createdEntitiesMap;

    private TestDataStorage() {
        createdEntitiesMap = new EnumMap<>(Endpoint.class);
    }

    public static TestDataStorage getStorage() {
        if (testDataStorage == null) {
            testDataStorage = new TestDataStorage();
        }
        return testDataStorage;
    }

    public BaseModel addCreatedEntity(Endpoint endpoint, BaseModel model) {
        createdEntitiesMap.computeIfAbsent(endpoint, key -> new ArrayList<>()).add(model);
        return model;
    }

    public void deleteTestData() {
        createdEntitiesMap.forEach((endpoint, models) -> models.forEach(model ->
                new UncheckedRequest(Specifications.getSpec().superUserSpec(), endpoint).delete(model.getId())));
        createdEntitiesMap.clear();
    }

}
