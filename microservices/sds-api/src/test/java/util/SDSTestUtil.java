package util;

import static org.junit.Assert.assertEquals;

import com.apriori.apibase.utils.APIAuthentication;
import com.apriori.apibase.utils.CommonRequestUtil;
import com.apriori.apibase.utils.TestUtil;
import com.apriori.ats.utils.JwtTokenUtil;
import com.apriori.cidapp.entity.response.css.Item;
import com.apriori.sds.entity.enums.SDSAPIEnum;
import com.apriori.sds.utils.Constants;
import com.apriori.utils.CidAppTestUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.http.utils.ResponseWrapper;

import org.apache.http.HttpStatus;
import org.junit.AfterClass;
import org.junit.BeforeClass;

public class SDSTestUtil extends TestUtil {

    private static final CidAppTestUtil cidAppTestUtil = new CidAppTestUtil();

    protected static String token;
    private static Item partPostComponentResponse;

    @BeforeClass
    public static void initTestingComponentInfo() {
        initToken();

        partPostComponentResponse = postTestingComponent();
    }

    @AfterClass
    public static void clearTestingData() {
        if (partPostComponentResponse != null) {
            removeTestingComponent(getComponentId(), getScenarioId());
        }
    }

    protected static String getComponentId() {
        return partPostComponentResponse.getComponentIdentity();
    }

    protected static String getScenarioId() {
        return partPostComponentResponse.getScenarioIdentity();
    }

    protected static String getIterationId() {
        return partPostComponentResponse.getIterationIdentity();
    }

    protected static Item postTestingComponent() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String partName = "Casting.prt";
        String processGroup = "Casting - Die";

        Item response =
            cidAppTestUtil.postComponents(partName, scenarioName,
                processGroup
            );

        return response;
    }

    protected static ResponseWrapper removeTestingComponent(final String componentId, final String scenarioId) {
        ResponseWrapper response =
            new CommonRequestUtil().deleteCommonRequestWithInlineVariables(SDSAPIEnum.DELETE_SCENARIO_BY_COMPONENT_SCENARIO_IDS, null,
                new APIAuthentication().initAuthorizationHeaderContent(token), componentId, scenarioId
            );

        assertEquals(String.format("The component with scenario %s, was not removed", scenarioId),
            HttpStatus.SC_NO_CONTENT, response.getStatusCode());

        return response;
    }

    private static String initToken() {
        if (token == null) {
            token = new JwtTokenUtil().retrieveJwtToken();
        }

        return token;
    }
}
