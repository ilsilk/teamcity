package com.teamcity.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
// Необходимая аннотация для десериализации с помощью Jackson, позволяет отказаться от использования Gson
@Jacksonized
// Без этой аннотации сериализация в объект производилась бы по всем полям, которые пришли в респонсе,
// даже если такие поля не указаны в классе-модели (в таком случае, был бы exception)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthModule {

    private String name;
    private Properties properties;

}
