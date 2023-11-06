package com.apriori.acs.api.tests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;
import static org.hamcrest.core.StringStartsWith.startsWith;

import com.apriori.acs.api.models.response.acs.missingscenario.MissingScenarioResponse;
import com.apriori.acs.api.models.response.acs.scenarioinfobyscenarioiterationkey.ScenarioInfoByScenarioIterationKeyResponse;
import com.apriori.acs.api.utils.Constants;
import com.apriori.acs.api.utils.acs.AcsResources;
import com.apriori.shared.util.http.utils.TestUtil;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@ExtendWith(TestRulesAPI.class)
public class CreateMissingScenarioTests extends TestUtil {

    @Test
    @TestRail(id = 8767)
    @Description("Test Create Missing Scenario")
    public void testCreateMissingScenario() {
        AcsResources acsResources = new AcsResources();
        MissingScenarioResponse createMissingScenarioResponse = acsResources.createMissingScenario();

        assertThat(createMissingScenarioResponse.isResourceCreated(), is(equalTo(true)));
        assertThat(createMissingScenarioResponse.isMissing(), is(equalTo(true)));

        ScenarioInfoByScenarioIterationKeyResponse getScenarioInfoByScenarioIterationKeyResponse = acsResources.getScenarioInfoByScenarioIterationKey(createMissingScenarioResponse.getScenarioIterationKey());

        assertThat(getScenarioInfoByScenarioIterationKeyResponse.isInitialized(), is(equalTo(false)));
        assertThat(getScenarioInfoByScenarioIterationKeyResponse.isVirtual(), is(equalTo(true)));

        assertThat(getScenarioInfoByScenarioIterationKeyResponse.getComponentType(), is(equalTo(Constants.PART_COMPONENT_TYPE)));
        assertThat(getScenarioInfoByScenarioIterationKeyResponse.getConfigurationName(), is(equalTo(Constants.PART_CONFIG_NAME)));
        assertThat(getScenarioInfoByScenarioIterationKeyResponse.getLocked(), is(equalTo("false")));
        assertThat(getScenarioInfoByScenarioIterationKeyResponse.getFileName(), is(equalTo(Constants.PART_FILE_NAME)));
        assertThat(getScenarioInfoByScenarioIterationKeyResponse.getCreatedBy(), is(containsString("qa-automation")));

        String currentDate = LocalDateTime.now(ZoneOffset.UTC).withNano(0).toString().substring(0, 10);

        assertThat(getScenarioInfoByScenarioIterationKeyResponse.getCreatedAt(), is(startsWith(currentDate)));
        assertThat(getScenarioInfoByScenarioIterationKeyResponse.getUpdatedBy(), is(containsString("qa-automation")));
        assertThat(getScenarioInfoByScenarioIterationKeyResponse.getUpdatedAt(), is(startsWith(currentDate)));
        assertThat(createMissingScenarioResponse.getScenarioIterationKey().getScenarioKey().getStateName(), is(equalTo(getScenarioInfoByScenarioIterationKeyResponse.getScenarioName())));
        assertThat(createMissingScenarioResponse.getScenarioIterationKey().getScenarioKey().getMasterName(), is(startsWith(getScenarioInfoByScenarioIterationKeyResponse.getComponentName())));
    }
}
