package com.teamcity.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.teamcity.api.enums.UserRole;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
public class Role extends BaseModel {

    @Builder.Default
    private String roleId = UserRole.SYSTEM_ADMIN.toString();
    @Builder.Default
    private String scope = "g";

}
