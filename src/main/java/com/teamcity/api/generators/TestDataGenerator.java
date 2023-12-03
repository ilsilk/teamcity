package com.teamcity.api.generators;

import com.teamcity.api.enums.RoleEnum;
import com.teamcity.api.models.*;

import java.util.List;

public final class TestDataGenerator {

    private TestDataGenerator() {
    }

    public static TestData generate() {
        var project = NewProjectDescription.builder()
                .id(RandomData.getString())
                .name(RandomData.getString())
                .copyAllAssociatedSettings(true)
                .parentProject(Project.builder()
                        .locator("_Root")
                        .build())
                .build();

        var user = User.builder()
                .username(RandomData.getString())
                .email(RandomData.getString() + "@gmail.com")
                .password(RandomData.getString())
                .roles(generateRoles(RoleEnum.SYSTEM_ADMIN, "g"))
                .build();

        var buildType = BuildType.builder()
                .id(RandomData.getString())
                .name(RandomData.getString())
                .project(project)
                .build();

        return TestData.builder()
                .project(project)
                .user(user)
                .buildType(buildType)
                .build();
    }

    public static Roles generateRoles(RoleEnum role, String scope) {
        return Roles.builder()
                .role(List.of(Role.builder()
                        .roleId(role.toString())
                        .scope(scope)
                        .build()))
                .build();
    }

}
