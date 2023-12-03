package com.teamcity.api;

import com.teamcity.api.generators.TestDataGenerator;
import com.teamcity.api.models.Build;
import com.teamcity.api.requests.checked.CheckedBuildQueue;
import com.teamcity.api.requests.checked.CheckedBuilds;
import com.teamcity.api.spec.Specifications;
import org.testng.annotations.Test;

public class StartBuildTest extends BaseApiTest {

    private static final int FIVE_SECONDS = 5000;

    @Test(description = "User should be able to start build")
    public void userStartsBuildTest() {
        checkedSuperUser.getUserRequest().create(testData.getUser());
        checkedSuperUser.getProjectRequest().create(testData.getProject());

        testData.getBuildType().setSteps(TestDataGenerator.generateSimpleRunnerSteps("echo 'Hello World!'"));

        checkedSuperUser.getBuildTypeRequest().create(testData.getBuildType());

        var build = new CheckedBuildQueue(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(Build.builder()
                        .buildType(testData.getBuildType())
                        .build());

        softy.assertThat(build.getState()).isEqualTo("queued");

        // TODO: Change to Awaitility
        try {
            Thread.sleep(FIVE_SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        build = new CheckedBuilds(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .read(build.getId());

        softy.assertThat(build.getState()).isEqualTo("finished");
        softy.assertThat(build.getStatus()).isEqualTo("SUCCESS");
    }

}
