package com.teamcity.ui;

import com.teamcity.api.models.BuildType;
import com.teamcity.api.requests.checked.CheckedBase;
import com.teamcity.api.spec.Specifications;
import com.teamcity.ui.pages.admin.CreateBuildTypePage;
import com.teamcity.ui.pages.admin.EditBuildTypePage;
import io.qameta.allure.Feature;
import org.testng.annotations.Test;

import static com.teamcity.api.enums.Endpoint.BUILD_TYPES;
import static com.teamcity.api.enums.Endpoint.PROJECTS;

@Feature("Build type")
public class CreateBuildTypeTest extends BaseUiTest {

    @Test(description = "User should be able to create build type", groups = {"Regression"})
    public void userCreatesBuildTypeTest() {
        checkedSuperUser.getRequest(PROJECTS).create(testData.project());
        loginAs(testData.user());

        CreateBuildTypePage.open(testData.project().id())
                .createFrom(GIT_URL)
                .setupBuildType(testData.buildType().name());
        var createdBuildTypeId = EditBuildTypePage.open().getBuildTypeId();

        var checkedBuildTypeRequest = new CheckedBase(Specifications.getSpec()
                .authSpec(testData.user()), BUILD_TYPES);
        var buildType = (BuildType) checkedBuildTypeRequest.read(createdBuildTypeId);
        softy.assertThat(buildType.name()).as("buildTypeName").isEqualTo(testData.buildType().name());
    }

    @Test(description = "User should not be able to create build type without name", groups = {"Regression"})
    public void userCreatesBuildTypeWithoutName() {
        checkedSuperUser.getRequest(PROJECTS).create(testData.project());
        loginAs(testData.user());

        CreateBuildTypePage.open(testData.project().id())
                .createFrom(GIT_URL)
                .setupBuildType("")
                .verifyBuildTypeNameError("Build configuration name must not be empty");
    }

}
