package com.teamcity.ui;

import com.teamcity.api.generators.TestDataStorage;
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

    @Test(description = "User should be able to create build type")
    public void userCreatesBuildTypeTest() {
        checkedSuperUser.getRequest(PROJECTS).create(testData.getProject());
        loginAs(testData.getUser());

        CreateBuildTypePage.open(testData.getProject().getId())
                .createFrom(GIT_URL)
                .setupBuildType(testData.getBuildType().getName());
        var createdBuildTypeId = EditBuildTypePage.open().getBuildTypeId();

        var checkedBuildTypeRequest = new CheckedBase(Specifications.getSpec()
                .authSpec(testData.getUser()), BUILD_TYPES);
        var buildType = (BuildType) checkedBuildTypeRequest.read(createdBuildTypeId);
        softy.assertThat(buildType.getName()).as("buildTypeName").isEqualTo(testData.getBuildType().getName());
        TestDataStorage.getStorage().addCreatedEntity(BUILD_TYPES, createdBuildTypeId);
    }

    @Test(description = "User should not be able to create build type without name")
    public void userCreatesBuildTypeWithoutName() {
        checkedSuperUser.getRequest(PROJECTS).create(testData.getProject());
        loginAs(testData.getUser());

        CreateBuildTypePage.open(testData.getProject().getId())
                .createFrom(GIT_URL)
                .setupBuildType("")
                .verifyBuildTypeNameError();
    }

}
