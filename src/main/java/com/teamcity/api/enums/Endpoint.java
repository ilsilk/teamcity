package com.teamcity.api.enums;

import com.teamcity.api.models.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Endpoint {

    BUILD_QUEUE("/app/rest/buildQueue", Build.class),
    BUILDS("/app/rest/builds", Build.class),
    BUILD_TYPES("/app/rest/buildTypes", BuildType.class),
    USERS("/app/rest/users", User.class),
    PROJECTS("/app/rest/projects", Project.class);

    private String url;
    private Class<? extends BaseModel> modelClass;

}
