package com.teamcity.api;

import com.teamcity.api.generators.TestDataGenerator;
import com.teamcity.api.models.Build;
import com.teamcity.api.requests.checked.CheckedBase;
import com.teamcity.api.spec.Specifications;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import org.awaitility.Awaitility;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicReference;

import static com.teamcity.api.enums.Endpoint.*;

@Feature("Start build")
public class StartBuildTest extends BaseApiTest {

    @Test(description = "User should be able to start build", groups = {"Regression"})
    public void userStartsBuildTest() {
        checkedSuperUser.getRequest(USERS).create(testData.getUser());
        checkedSuperUser.getRequest(PROJECTS).create(testData.getProject());

        testData.getBuildType().setSteps(TestDataGenerator.generateSimpleRunnerSteps("echo 'Hello World!'"));

        checkedSuperUser.getRequest(BUILD_TYPES).create(testData.getBuildType());

        var checkedBuildQueueRequest = new CheckedBase(Specifications.getSpec()
                .authSpec(testData.getUser()), BUILD_QUEUE);
        var build = (Build) checkedBuildQueueRequest.create(Build.builder()
                .buildType(testData.getBuildType())
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
                .authSpec(testData.getUser()), BUILDS);
        Awaitility.await()
                .atMost(Duration.ofSeconds(15))
                .pollInterval(Duration.ofSeconds(3))
                .until(() -> {
                    atomicBuild.set((Build) checkedBuildRequest.read(atomicBuild.get().getId()));
                    return "finished".equals(atomicBuild.get().getState());
                });
        return atomicBuild.get();
    }

}
