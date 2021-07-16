package tests;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.acs.entity.response.createmissingscenario.CreateMissingScenarioResponse;
import com.apriori.acs.entity.response.publish.getscenarioinfobyscenarioiterationkey.GetScenarioInfoByScenarioIterationKeyResponse;
import com.apriori.acs.utils.AcsResources;
import com.apriori.acs.utils.Constants;

import org.junit.BeforeClass;
import org.junit.Test;

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
        assertThat(getScenarioInfoByScenarioIterationKeyResponse.getCreatedBy(), is(equalTo("bhegan")));
        assertThat(getScenarioInfoByScenarioIterationKeyResponse.getUpdateddBy(), is(equalTo("bhegan")));

        assertThat(createMissingScenarioResponse.getScenarioIterationKey().getScenarioKey().getStateName(),
                is(equalTo(getScenarioInfoByScenarioIterationKeyResponse.getScenarioName())));

        assertThat(createMissingScenarioResponse.getScenarioIterationKey().getScenarioKey().getMasterName(),
                is(startsWith(getScenarioInfoByScenarioIterationKeyResponse.getComponentName())));

    }
}
