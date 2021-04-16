package util;

import static org.junit.Assert.assertEquals;

import com.apriori.apibase.utils.APIAuthentication;
import com.apriori.apibase.utils.CommonRequestUtil;
import com.apriori.apibase.utils.JwtTokenUtil;
import com.apriori.apibase.utils.TestUtil;
import com.apriori.entity.response.PostComponentResponse;
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
    private static PostComponentResponse partPostComponentResponse;

    @BeforeClass
    public static void initTestingComponentInfo() {
        initToken();
        partPostComponentResponse = postTestingComponent().getResponseEntity();
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

    protected static ResponseWrapper<PostComponentResponse> postTestingComponent() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String partName = "Casting.prt";
        String processGroup = "Casting - Die";

        ResponseWrapper<PostComponentResponse> response =
            cidAppTestUtil.postComponents(scenarioName,
                processGroup,
                partName);

        assertEquals(String.format("The component with a part name %s, was not uploaded.", partName),
            HttpStatus.SC_CREATED, response.getStatusCode());

        cidAppTestUtil.getScenarioRepresentation("processing",
            response.getResponseEntity().getComponentIdentity(),
            response.getResponseEntity().getScenarioIdentity()
        );

        return response;
    }

    protected static ResponseWrapper removeTestingComponent(final String componentId, final String scenarioId) {
        ResponseWrapper response =
            new CommonRequestUtil().deleteCommonRequestWithInlineVariables(SDSAPIEnum.DELETE_SCENARIO_BY_COMPONENT_SCENARIO_IDS, null,
                new APIAuthentication().initAuthorizationHeaderContent(token), componentId, scenarioId
            );

        assertEquals(String.format("The component with scenario %s, was not removed", scenarioId),
            HttpStatus.SC_OK, response.getStatusCode());

        return response;
    }

    private static String initToken() {
        if (token == null) {
            token = new JwtTokenUtil().retrieveJwtToken(Constants.getSecretKey(),
                Constants.getCidServiceHost(),
                HttpStatus.SC_CREATED,
                Constants.getCidTokenUsername(),
                Constants.getCidTokenEmail(),
                Constants.getCidTokenIssuer(),
                Constants.getCidTokenSubject());
        }

        return token;
    }
}
