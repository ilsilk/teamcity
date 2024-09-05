package com.teamcity.api;

import com.teamcity.api.models.Build;
import com.teamcity.api.models.Property;
import com.teamcity.api.models.Steps;
import com.teamcity.api.requests.checked.CheckedBase;
import com.teamcity.api.spec.Specifications;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import org.awaitility.Awaitility;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static com.teamcity.api.enums.Endpoint.BUILDS;
import static com.teamcity.api.enums.Endpoint.BUILD_QUEUE;
import static com.teamcity.api.enums.Endpoint.BUILD_TYPES;
import static com.teamcity.api.enums.Endpoint.PROJECTS;
import static com.teamcity.api.enums.Endpoint.USERS;
import static com.teamcity.api.generators.TestDataGenerator.generate;

@Feature("Start build")
public class StartBuildTest extends BaseApiTest {

    @Test(description = "User should be able to start build", groups = {"Regression"})
    public void userStartsBuildTest() {
        checkedSuperUser.getRequest(USERS).create(testData.user());
        checkedSuperUser.getRequest(PROJECTS).create(testData.project());

        var buildTypeTestData = testData.buildType();
        buildTypeTestData.steps(generate(Steps.class, List.of(
                generate(Property.class, "script.content", "echo 'Hello World!'"),
                generate(Property.class, "use.custom.script", "true"))));

        checkedSuperUser.getRequest(BUILD_TYPES).create(testData.buildType());

        var checkedBuildQueueRequest = new CheckedBase(Specifications.getSpec()
                .authSpec(testData.user()), BUILD_QUEUE);
        var build = (Build) checkedBuildQueueRequest.create(Build.builder()
                .buildType(testData.buildType())
                .build());

        softy.assertThat(build.state()).as("buildState").isEqualTo("queued");

        build = waitUntilBuildIsFinished(build);
        softy.assertThat(build.status()).as("buildStatus").isEqualTo("SUCCESS");
    }

    @Step("Wait until build is finished")
    private Build waitUntilBuildIsFinished(Build build) {
        // Необходимо использовать AtomicReference, так как переменная в лямбда выражении должна быть final или effectively final
        var atomicBuild = new AtomicReference<>(build);
        var checkedBuildRequest = new CheckedBase(Specifications.getSpec()
                .authSpec(testData.user()), BUILDS);
        Awaitility.await()
                .atMost(Duration.ofSeconds(30))
                .pollInterval(Duration.ofSeconds(3))
                .until(() -> {
                    atomicBuild.set((Build) checkedBuildRequest.read(atomicBuild.get().id()));
                    return "finished".equals(atomicBuild.get().state());
                });
        return atomicBuild.get();
    }

}
