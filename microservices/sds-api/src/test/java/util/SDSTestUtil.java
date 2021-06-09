package util;

import static org.junit.Assert.assertEquals;

import com.apriori.apibase.utils.APIAuthentication;
import com.apriori.apibase.utils.CommonRequestUtil;
import com.apriori.apibase.utils.TestUtil;
import com.apriori.ats.utils.JwtTokenUtil;
import com.apriori.css.entity.response.CssComponentResponse;
import com.apriori.css.entity.response.Item;
import com.apriori.sds.entity.enums.SDSAPIEnum;
import com.apriori.sds.entity.request.PostComponentRequest;
import com.apriori.sds.entity.response.PostComponentResponse;
import com.apriori.sds.utils.Constants;
import com.apriori.utils.Base64EncoderUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.UncostedComponents;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.http2.builder.common.entity.RequestEntity;
import com.apriori.utils.http2.builder.service.HTTP2Request;
import com.apriori.utils.http2.utils.RequestEntityUtil;

import org.apache.http.HttpStatus;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;

import java.util.HashMap;

public class SDSTestUtil extends TestUtil {

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

    /**
     * Get component id
     * @return string
     */
    protected static String getComponentId() {
        return partPostComponentResponse.getComponentIdentity();
    }

    /**
     * Get scenario id
     * @return string
     */
    protected static String getScenarioId() {
        return partPostComponentResponse.getScenarioIdentity();
    }

    /**
     * Get iteration id
     * @return string
     */
    protected static String getIterationId() {
        return partPostComponentResponse.getIterationIdentity();
    }

    /**
     * Post testing component
     * @return object
     */
    protected static Item postTestingComponent() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String componentName = "Casting.prt";
        String processGroup = "Casting - Die";

        Item response = postComponents(componentName, scenarioName, processGroup);

        return response;
    }

    /**
     * Remove testing component
     * @param componentId - component id
     * @param scenarioId - scenario id
     * @return response object
     */
    // TODO: 09/06/2021 cn - @vlad maybe instead of using raw type the variable can be declared as ResponseWrapper<String> ?
    protected static ResponseWrapper removeTestingComponent(final String componentId, final String scenarioId) {
        ResponseWrapper response =
            new CommonRequestUtil().deleteCommonRequestWithInlineVariables(SDSAPIEnum.DELETE_SCENARIO_BY_COMPONENT_SCENARIO_IDS, null,
                new APIAuthentication().initAuthorizationHeaderContent(token), componentId, scenarioId
            );

        assertEquals(String.format("The component with scenario %s, was not removed", scenarioId),
            HttpStatus.SC_NO_CONTENT, response.getStatusCode());

        return response;
    }

    /**
     * Init token
     * @return string
     */
    private static String initToken() {
        if (token == null) {
            token = new JwtTokenUtil().retrieveJwtToken();
        }

        return token;
    }

    /**
     * Adds a new component
     *
     * @param componentName - the part name
     * @param scenarioName  - the scenario name
     * @return responsewrapper
     */
    protected static Item postComponents(String componentName, String scenarioName, String resourceFile) {
        RequestEntity requestEntity =
            RequestEntityUtil.init(SDSAPIEnum.POST_COMPONENTS, PostComponentResponse.class)
                .headers(new HashMap<String, String>() {{
                    put("ap-user-context", Constants.getUserContext());
                }})
                .body("component", PostComponentRequest.builder().filename(componentName)
                    .scenarioName(scenarioName)
                    .override(false)
                    .fileContents(Base64EncoderUtil.encodeFileToBase64Binary(componentName, resourceFile))
                    .build());

        ResponseWrapper<PostComponentResponse> responseWrapper = HTTP2Request.build(requestEntity).post();

        Assert.assertEquals(String.format("The component with a part name %s, and scenario name %s, was not uploaded.", componentName, scenarioName),
            HttpStatus.SC_CREATED, responseWrapper.getStatusCode());

        ResponseWrapper<CssComponentResponse> itemResponse = new UncostedComponents().getUnCostedCssComponent(componentName, scenarioName);

        Assert.assertEquals("The component response should be okay.", HttpStatus.SC_OK, itemResponse.getStatusCode());
        return itemResponse.getResponseEntity().getItems().get(0);
    }
}
