package com.teamcity.api.requests;

import com.teamcity.api.enums.Endpoint;
import com.teamcity.api.requests.unchecked.UncheckedRequest;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;

import java.util.EnumMap;

@Getter
public class UncheckedRequests {

    private final EnumMap<Endpoint, UncheckedRequest> uncheckedRequests = new EnumMap<>(Endpoint.class);

    public UncheckedRequests(RequestSpecification spec) {
        for (var endpoint : Endpoint.values()) {
            uncheckedRequests.put(endpoint, new UncheckedRequest(spec, endpoint));
        }
    }

    public UncheckedRequest getRequest(Endpoint endpoint) {
        return uncheckedRequests.get(endpoint);
    }

}
