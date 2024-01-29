package com.teamcity.api.enums;

import com.teamcity.api.models.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Endpoint {

    // Описываем соответствие между эндпоинтом и моделью, которую он возвращает
    BUILD_QUEUE("/app/rest/buildQueue", Build.class, null),
    BUILDS("/app/rest/builds", Build.class, null),
    BUILD_TYPES("/app/rest/buildTypes", BuildType.class, BuildType.class),
    USERS("/app/rest/users", User.class, User.class),
    PROJECTS("/app/rest/projects", Project.class, NewProjectDescription.class);

    private final String url;
    // Все классы, наследующие BaseModel
    private final Class<? extends BaseModel> modelClass;
    private final Class<? extends BaseModel> generatorClass;

}
