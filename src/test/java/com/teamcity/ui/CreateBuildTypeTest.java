package com.teamcity.ui;

import com.teamcity.api.generators.TestDataStorage;
import com.teamcity.api.models.BuildType;
import com.teamcity.api.models.NewProjectDescription;
import com.teamcity.api.requests.checked.CheckedBase;
import com.teamcity.api.spec.Specifications;
import com.teamcity.ui.pages.admin.CreateBuildTypePage;
import com.teamcity.ui.pages.admin.EditBuildTypePage;
import io.qameta.allure.Feature;
import org.testng.annotations.Test;

import static com.teamcity.api.enums.Endpoint.*;

@Feature("Build type")
public class CreateBuildTypeTest extends BaseUiTest {

    @Test(description = "User should be able to create build type", groups = {"Regression"})
    public void userCreatesBuildTypeTest() {
        checkedSuperUser.getRequest(PROJECTS).create(testData.get(PROJECTS));
        loginAs(testData.get(USERS));

        CreateBuildTypePage.open(((NewProjectDescription) testData.get(PROJECTS)).getId())
                .createFrom(GIT_URL)
                .setupBuildType(((BuildType) testData.get(BUILD_TYPES)).getName());
        var createdBuildTypeId = EditBuildTypePage.open().getBuildTypeId();

        var checkedBuildTypeRequest = new CheckedBase(Specifications.getSpec()
                .authSpec(testData.get(USERS)), BUILD_TYPES);
        var buildType = (BuildType) checkedBuildTypeRequest.read(createdBuildTypeId);
        softy.assertThat(buildType.getName()).as("buildTypeName").isEqualTo(((BuildType) testData.get(BUILD_TYPES)).getName());
        TestDataStorage.getStorage().addCreatedEntity(BUILD_TYPES, createdBuildTypeId);
    }

    @Test(description = "User should not be able to create build type without name", groups = {"Regression"})
    public void userCreatesBuildTypeWithoutName() {
        checkedSuperUser.getRequest(PROJECTS).create(testData.get(PROJECTS));
        loginAs(testData.get(USERS));

        CreateBuildTypePage.open(((NewProjectDescription) testData.get(PROJECTS)).getId())
                .createFrom(GIT_URL)
                .setupBuildType("")
                .verifyBuildTypeNameError();
    }

}
