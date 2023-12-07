package com.teamcity.api.requests;

import com.teamcity.api.enums.Endpoint;
import com.teamcity.api.requests.checked.CheckedRequest;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;

import java.util.EnumMap;

@Getter
public class CheckedRequests {

    private final EnumMap<Endpoint, CheckedRequest> checkedRequests = new EnumMap<>(Endpoint.class);

    public CheckedRequests(RequestSpecification spec) {
        for (var endpoint : Endpoint.values()) {
            checkedRequests.put(endpoint, new CheckedRequest(spec, endpoint));
        }
    }

    public CheckedRequest getRequest(Endpoint endpoint) {
        return checkedRequests.get(endpoint);
    }

}
