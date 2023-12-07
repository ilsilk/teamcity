package com.teamcity.api.enums;

import com.teamcity.api.models.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Endpoint {

    PROJECTS("/app/rest/projects", Project.class),
    USERS("/app/rest/users", User.class),
    BUILD_TYPES("/app/rest/buildTypes", BuildType.class),
    BUILDS("/app/rest/builds", Build.class),
    BUILD_QUEUE("/app/rest/buildQueue", Build.class);

    private String url;
    private Class<? extends BaseModel> modelClass;

}
