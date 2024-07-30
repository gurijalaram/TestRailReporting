package com.apriori.acs.api.tests;

import com.apriori.acs.api.models.response.acs.missingscenario.MissingScenarioResponse;
import com.apriori.acs.api.models.response.acs.scenarioinfobyscenarioiterationkey.ScenarioInfoByScenarioIterationKeyResponse;
import com.apriori.acs.api.utils.Constants;
import com.apriori.acs.api.utils.acs.AcsResources;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.TestHelper;
import com.apriori.shared.util.http.utils.TestUtil;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@ExtendWith(TestRulesAPI.class)
public class CreateMissingScenarioTests extends TestUtil {
    private SoftAssertions softAssertions;
    private AcsResources acsResources;

    @BeforeEach
    public void setup() {
        RequestEntityUtil requestEntityUtil = TestHelper.initUser();
        acsResources = new AcsResources(requestEntityUtil);
        softAssertions = new SoftAssertions();
    }

    @Test
    @TestRail(id = 8767)
    @Description("Test Create Missing Scenario")
    public void testCreateMissingScenario() {
        MissingScenarioResponse createMissingScenarioResponse = acsResources.createMissingScenario();

        softAssertions.assertThat(createMissingScenarioResponse.isResourceCreated()).isEqualTo(true);
        softAssertions.assertThat(createMissingScenarioResponse.isMissing()).isEqualTo(true);

        ScenarioInfoByScenarioIterationKeyResponse getScenarioInfoByScenarioIterationKeyResponse = acsResources
            .getScenarioInfoByScenarioIterationKey(createMissingScenarioResponse.getScenarioIterationKey());

        softAssertions.assertThat(getScenarioInfoByScenarioIterationKeyResponse.isInitialized()).isEqualTo(false);
        softAssertions.assertThat(getScenarioInfoByScenarioIterationKeyResponse.isVirtual()).isEqualTo(true);

        softAssertions.assertThat(getScenarioInfoByScenarioIterationKeyResponse.getComponentType()).isEqualTo(Constants.PART_COMPONENT_TYPE);
        softAssertions.assertThat(getScenarioInfoByScenarioIterationKeyResponse.getConfigurationName()).isEqualTo(Constants.PART_CONFIG_NAME);
        softAssertions.assertThat(getScenarioInfoByScenarioIterationKeyResponse.getLocked()).isEqualTo("false");
        softAssertions.assertThat(getScenarioInfoByScenarioIterationKeyResponse.getFileName()).isEqualTo(Constants.PART_FILE_NAME);
        softAssertions.assertThat(getScenarioInfoByScenarioIterationKeyResponse.getCreatedBy()).contains("qa-automation");

        String currentDate = LocalDateTime.now(ZoneOffset.UTC).withNano(0).toString().substring(0, 10);

        softAssertions.assertThat(getScenarioInfoByScenarioIterationKeyResponse.getCreatedAt()).startsWith(currentDate);
        softAssertions.assertThat(getScenarioInfoByScenarioIterationKeyResponse.getUpdatedBy()).contains("qa-automation");
        softAssertions.assertThat(getScenarioInfoByScenarioIterationKeyResponse.getUpdatedAt()).startsWith(currentDate);
        softAssertions.assertThat(createMissingScenarioResponse.getScenarioIterationKey().getScenarioKey().getStateName())
            .isEqualTo(getScenarioInfoByScenarioIterationKeyResponse.getScenarioName());
        softAssertions.assertThat(createMissingScenarioResponse.getScenarioIterationKey().getScenarioKey().getMasterName())
            .startsWith(getScenarioInfoByScenarioIterationKeyResponse.getComponentName());
        softAssertions.assertAll();
    }
}
