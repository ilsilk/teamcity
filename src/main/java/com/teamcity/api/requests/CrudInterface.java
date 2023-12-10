package com.teamcity.api.requests;

public interface CrudInterface {

    Object create(Object obj);

    Object read(String id);

    Object update(String id, Object obj);

    Object delete(String id);

}
