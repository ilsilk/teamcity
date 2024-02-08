package com.teamcity.api.requests;

import com.teamcity.api.models.BaseModel;

public interface CrudInterface {

    Object create(BaseModel modelClass);

    Object read(String id);

    Object update(String id, Object obj);

    Object delete(String id);

}
