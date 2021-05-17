package util;

import static org.junit.Assert.assertEquals;

import com.apriori.apibase.utils.APIAuthentication;
import com.apriori.apibase.utils.CommonRequestUtil;
import com.apriori.apibase.utils.JwtTokenUtil;
import com.apriori.apibase.utils.TestUtil;
import com.apriori.cidapp.entity.response.PostComponentResponse;
import com.apriori.sds.entity.enums.SDSAPIEnum;
import com.apriori.sds.utils.Constants;
import com.apriori.cidapp.utils.CidAppTestUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.http.utils.ResponseWrapper;

import org.apache.http.HttpStatus;
import org.junit.AfterClass;
import org.junit.BeforeClass;

public class SDSTestUtil extends TestUtil {

    private static final CidAppTestUtil cidAppTestUtil = new CidAppTestUtil();

    protected static String token;
    private static PostComponentResponse partPostComponentResponse = PostComponentResponse.builder()
        .componentIdentity("6L5J136E3GDG")
        .scenarioIdentity("80F3138B63F2")
        .iterationIdentity("80F64BJ8FCL3")
        .build();

    @BeforeClass
    public static void initTestingComponentInfo() {
        initToken();

        //TODO z: temporary removed.
        // partPostComponentResponse = postTestingComponent().getResponseEntity();
    }

    @AfterClass
    public static void clearTestingData() {
        //TODO z: temporary removed.
        //        if (partPostComponentResponse != null) {
        //            removeTestingComponent(getComponentId(), getScenarioId());
        //        }
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
            HttpStatus.SC_NO_CONTENT, response.getStatusCode());

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
