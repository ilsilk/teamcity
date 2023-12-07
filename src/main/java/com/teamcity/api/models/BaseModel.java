package com.teamcity.api.models;

import lombok.Data;

@Data
public abstract class BaseModel {

    private String id;

    public abstract String getId();

}
