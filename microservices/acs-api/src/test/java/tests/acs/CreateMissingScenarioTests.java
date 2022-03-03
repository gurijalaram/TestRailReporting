package tests.acs;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.acs.entity.response.acs.createmissingscenario.CreateMissingScenarioResponse;
import com.apriori.acs.entity.response.acs.getscenarioinfobyscenarioiterationkey.GetScenarioInfoByScenarioIterationKeyResponse;
import com.apriori.acs.utils.Constants;
import com.apriori.acs.utils.acs.AcsResources;
import com.apriori.utils.TestRail;

import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.categories.AcsTest;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class CreateMissingScenarioTests {

    @Test
    @Category(AcsTest.class)
    @TestRail(testCaseId = "8767")
    @Description("Test Create Missing Scenario")
    public void testCreateMissingScenario() {
        AcsResources acsResources = new AcsResources();
        CreateMissingScenarioResponse createMissingScenarioResponse = acsResources.createMissingScenario();

        assertThat(createMissingScenarioResponse.isResourceCreated(), is(equalTo(true)));
        assertThat(createMissingScenarioResponse.isMissing(), is(equalTo(true)));

        GetScenarioInfoByScenarioIterationKeyResponse getScenarioInfoByScenarioIterationKeyResponse = acsResources.getScenarioInfoByScenarioIterationKey(createMissingScenarioResponse.getScenarioIterationKey());

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