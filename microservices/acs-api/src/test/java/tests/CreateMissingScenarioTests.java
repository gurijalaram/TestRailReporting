package tests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.acs.entity.response.createmissingscenario.CreateMissingScenarioResponse;
import com.apriori.acs.entity.response.getscenarioinfobyscenarioiterationkey.GetScenarioInfoByScenarioIterationKeyResponse;
import com.apriori.acs.utils.AcsResources;
import com.apriori.acs.utils.Constants;

import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class CreateMissingScenarioTests {

    @BeforeClass
    public static void getAuthorizationToken() {
        Constants.getDefaultUrl();
    }

    @Test
    /*@Category()
    @TestRail(testCaseId = "")
    @Description("")*/
    public void testCreateMissingScenario() {
        AcsResources acsResources = new AcsResources();
        CreateMissingScenarioResponse createMissingScenarioResponse = acsResources.createMissingScenario();

        assertThat(createMissingScenarioResponse, is(notNullValue()));
        assertThat(createMissingScenarioResponse.isResourceCreated(), is(equalTo(true)));
        assertThat(createMissingScenarioResponse.isMissing(), is(equalTo(true)));

        GetScenarioInfoByScenarioIterationKeyResponse getScenarioInfoByScenarioIterationKeyResponse = acsResources
                .getScenarioInfoByScenarioIterationKey(createMissingScenarioResponse.getScenarioIterationKey());

        assertThat(getScenarioInfoByScenarioIterationKeyResponse, is(notNullValue()));

        assertThat(getScenarioInfoByScenarioIterationKeyResponse.isInitialized(), is(equalTo(false)));
        assertThat(getScenarioInfoByScenarioIterationKeyResponse.isVirtual(), is(equalTo(true)));

        assertThat(getScenarioInfoByScenarioIterationKeyResponse.getComponentType(),
                is(equalTo(Constants.PART_COMPONENT_TYPE)));
        assertThat(getScenarioInfoByScenarioIterationKeyResponse.getConfigurationName(),
                is(equalTo(Constants.PART_CONFIG_NAME)));

        assertThat(getScenarioInfoByScenarioIterationKeyResponse.getLocked(), is(equalTo("false")));

        assertThat(getScenarioInfoByScenarioIterationKeyResponse.getFileName(),
                is(equalTo(Constants.PART_FILE_NAME)));

        assertThat(getScenarioInfoByScenarioIterationKeyResponse.getCreatedBy(), is(equalTo(Constants.USERNAME)));

        String currentDate = LocalDateTime.now(ZoneOffset.UTC).withNano(0).toString().substring(0, 10);

        assertThat(getScenarioInfoByScenarioIterationKeyResponse.getCreatedAt(), is(startsWith(currentDate)));

        assertThat(getScenarioInfoByScenarioIterationKeyResponse.getUpdateddBy(), is(equalTo(Constants.USERNAME)));

        assertThat(getScenarioInfoByScenarioIterationKeyResponse.getUpdatedAt(), is(startsWith(currentDate)));


        assertThat(createMissingScenarioResponse.getScenarioIterationKey().getScenarioKey().getStateName(),
                is(equalTo(getScenarioInfoByScenarioIterationKeyResponse.getScenarioName())));

        assertThat(createMissingScenarioResponse.getScenarioIterationKey().getScenarioKey().getMasterName(),
                is(startsWith(getScenarioInfoByScenarioIterationKeyResponse.getComponentName())));
    }
}
