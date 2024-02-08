package com.teamcity.api;

import com.teamcity.api.models.AuthorizedInfo;
import com.teamcity.api.requests.checked.CheckedAgents;
import com.teamcity.api.spec.Specifications;
import io.qameta.allure.Feature;
import org.testng.annotations.Test;

import static com.teamcity.api.generators.TestDataGenerator.generate;

@Feature("Setup")
public class SetupAgentTest extends BaseApiTest {

    @Test(groups = {"Setup"})
    public void setupTeamCityAgentTest() {
        var checkedAgentsRequest = new CheckedAgents(Specifications.getSpec().superUserSpec());
        var agentId = checkedAgentsRequest
                .read("authorized:false")
                .getAgent()
                .get(0)
                .getId();

        checkedAgentsRequest.update(agentId, generate(AuthorizedInfo.class));
    }

}
