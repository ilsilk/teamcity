package com.teamcity.api;

import com.teamcity.api.generators.TestDataGenerator;
import com.teamcity.api.models.Build;
import com.teamcity.api.requests.checked.CheckedBase;
import com.teamcity.api.spec.Specifications;
import org.testng.annotations.Test;

import static com.teamcity.api.enums.Endpoint.*;

public class StartBuildTest extends BaseApiTest {

    private static final int FIVE_SECONDS = 5000;

    @Test(description = "User should be able to start build")
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

        softy.assertThat(build.getState()).isEqualTo("queued");

        // TODO: Change to Awaitility
        try {
            Thread.sleep(FIVE_SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        var checkedBuildRequest = new CheckedBase(Specifications.getSpec()
                .authSpec(testData.getUser()), BUILDS);
        build = (Build) checkedBuildRequest.read(build.getId());

        softy.assertThat(build.getState()).isEqualTo("finished");
        softy.assertThat(build.getStatus()).isEqualTo("SUCCESS");
    }

}
