package com.teamcity.api.generators;

import com.teamcity.api.enums.UserRole;
import com.teamcity.api.models.*;

import java.util.List;

public final class TestDataGenerator {

    private static final String SIMPLE_RUNNER_STEP_TYPE = "simpleRunner";

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
                .roles(generateRoles(UserRole.SYSTEM_ADMIN, "g"))
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

    public static Roles generateRoles(UserRole role, String scope) {
        return Roles.builder()
                .role(List.of(Role.builder()
                        .roleId(role.toString())
                        .scope(scope)
                        .build()))
                .build();
    }

    public static Steps generateSimpleRunnerSteps(String propertyValue) {
        return Steps.builder()
                .step(List.of(Step.builder()
                        .name(RandomData.getString())
                        .type(SIMPLE_RUNNER_STEP_TYPE)
                        .properties(Properties.builder()
                                .property(List.of(Property.builder()
                                        .name("script.content")
                                        .value(propertyValue)
                                        .build(), Property.builder()
                                        .name("use.custom.script")
                                        .value("true")
                                        .build()))
                                .build())
                        .build()))
                .build();
    }

    public static AuthModules generateAuthModules() {
        return AuthModules.builder()
                .module(List.of(AuthModule.builder()
                        .name("HTTP-Basic")
                        .build()))
                .build();
    }

}
