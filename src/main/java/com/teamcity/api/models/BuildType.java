package com.teamcity.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.teamcity.api.annotations.Optional;
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
public class BuildType extends BaseModel {

    @Random
    private String id;
    @Random
    private String name;
    private NewProjectDescription project;
    @Optional
    private Steps steps;

}
