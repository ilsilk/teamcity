package com.teamcity.api.requests.checked;

import com.teamcity.api.enums.Endpoint;
import com.teamcity.api.models.BaseModel;
import com.teamcity.api.requests.Request;
import com.teamcity.api.requests.SearchInterface;
import com.teamcity.api.requests.unchecked.UncheckedSearch;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;

import java.util.List;

@SuppressWarnings("unchecked")
public final class CheckedSearch<T extends BaseModel> extends Request implements SearchInterface {

    public CheckedSearch(RequestSpecification spec, Endpoint endpoint) {
        super(spec, endpoint);
    }

    @Override
    public List<T> search() {
        return (List<T>) new UncheckedSearch(spec, endpoint)
                .search()
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .extract().jsonPath()
                .getList(StringUtils.uncapitalize(endpoint.getModelClass().getSimpleName()), endpoint.getModelClass());
    }

}
