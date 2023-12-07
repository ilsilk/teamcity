package com.teamcity.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
public class User extends BaseModel {

    private String id;
    private String username;
    private String email;
    private String password;
    private Roles roles;

}
