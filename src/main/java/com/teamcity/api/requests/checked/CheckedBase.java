package com.teamcity.api.requests.checked;

import com.teamcity.api.enums.Endpoint;
import com.teamcity.api.generators.TestDataStorage;
import com.teamcity.api.models.BaseModel;
import com.teamcity.api.requests.CrudInterface;
import com.teamcity.api.requests.Request;
import com.teamcity.api.requests.unchecked.UncheckedBase;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;

public final class CheckedBase extends Request implements CrudInterface {

    // Все реквесты, имеющие одинаковую реализацию CRUD методов, можно создать через общий конструктор
    public CheckedBase(RequestSpecification spec, Endpoint endpoint) {
        super(spec, endpoint);
    }

    @Override
    public BaseModel create(BaseModel model) {
        var createdModel = new UncheckedBase(spec, endpoint)
                .create(model)
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .extract().as(endpoint.getModelClass());
        // После создания сущности ее айди добавляется в список созданных сущностей (для их удаления в конце)
        TestDataStorage.getStorage().addCreatedEntity(endpoint, createdModel);
        return createdModel;
    }

    @Override
    public BaseModel read(String id) {
        return new UncheckedBase(spec, endpoint)
                .read(id)
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .extract().as(endpoint.getModelClass());
    }

    @Override
    public BaseModel update(String id, BaseModel model) {
        return new UncheckedBase(spec, endpoint)
                .update(id, model)
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .extract().as(endpoint.getModelClass());
    }

    @Override
    public String delete(String id) {
        return new UncheckedBase(spec, endpoint)
                .delete(id)
                .then().assertThat().statusCode(HttpStatus.SC_NO_CONTENT)
                .extract().asString();
    }

}
