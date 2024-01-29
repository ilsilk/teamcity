package com.teamcity.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.teamcity.api.annotations.Random;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
public class User extends BaseModel {

    private String id;
    @Random
    private String username;
    @Random
    private String email;
    @Random
    private String password;
    private Roles roles;

}
