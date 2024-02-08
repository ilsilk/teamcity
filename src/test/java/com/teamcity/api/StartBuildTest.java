package com.teamcity.api;

import com.teamcity.api.models.Build;
import com.teamcity.api.models.BuildType;
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

import static com.teamcity.api.enums.Endpoint.*;
import static com.teamcity.api.generators.TestDataGenerator.generate;

@Feature("Start build")
public class StartBuildTest extends BaseApiTest {

    @Test(description = "User should be able to start build", groups = {"Regression"})
    public void userStartsBuildTest() {
        checkedSuperUser.getRequest(USERS).create(testData.get(USERS));
        checkedSuperUser.getRequest(PROJECTS).create(testData.get(PROJECTS));

        var buildTypeTestData = (BuildType) testData.get(BUILD_TYPES);
        buildTypeTestData.setSteps((Steps) generate(Steps.class, List.of(
                generate(Property.class, "script.content", "echo 'Hello World!'"),
                generate(Property.class, "use.custom.script", "true"))));

        checkedSuperUser.getRequest(BUILD_TYPES).create(testData.get(BUILD_TYPES));

        var checkedBuildQueueRequest = new CheckedBase(Specifications.getSpec()
                .authSpec(testData.get(USERS)), BUILD_QUEUE);
        var build = (Build) checkedBuildQueueRequest.create(Build.builder()
                .buildType((BuildType) testData.get(BUILD_TYPES))
                .build());

        softy.assertThat(build.getState()).as("buildState").isEqualTo("queued");

        build = waitUntilBuildIsFinished(build);
        softy.assertThat(build.getStatus()).as("buildStatus").isEqualTo("SUCCESS");
    }

    @Step("Wait until build is finished")
    private Build waitUntilBuildIsFinished(Build build) {
        // Необходимо использовать AtomicReference, так как переменная в лямбда выражении должна быть final или effectively final
        var atomicBuild = new AtomicReference<>(build);
        var checkedBuildRequest = new CheckedBase(Specifications.getSpec()
                .authSpec(testData.get(USERS)), BUILDS);
        Awaitility.await()
                .atMost(Duration.ofSeconds(30))
                .pollInterval(Duration.ofSeconds(3))
                .until(() -> {
                    atomicBuild.set((Build) checkedBuildRequest.read(atomicBuild.get().getId()));
                    return "finished".equals(atomicBuild.get().getState());
                });
        return atomicBuild.get();
    }

}
