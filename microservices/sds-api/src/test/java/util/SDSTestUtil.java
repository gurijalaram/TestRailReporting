package util;

import static org.junit.Assert.assertEquals;

import com.apriori.apibase.utils.APIAuthentication;
import com.apriori.apibase.utils.CommonRequestUtil;
import com.apriori.apibase.utils.TestUtil;
import com.apriori.ats.utils.JwtTokenUtil;
import com.apriori.cidapp.entity.response.PostComponentResponse;
import com.apriori.cidapp.entity.response.css.CssComponentResponse;
import com.apriori.cidapp.entity.response.css.Item;
import com.apriori.sds.entity.enums.SDSAPIEnum;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.http.utils.FormParams;
import com.apriori.utils.http.utils.MultiPartFiles;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.http2.builder.common.entity.RequestEntity;
import com.apriori.utils.http2.builder.service.HTTP2Request;
import com.apriori.utils.http2.utils.RequestEntityUtil;

import org.apache.http.HttpStatus;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;

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
            postComponents(partName, scenarioName,
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
                .multiPartFiles(new MultiPartFiles().use("data", FileResourceUtil.getCloudFile(ProcessGroupEnum.fromString(resourceFile), componentName)))
                .formParams(new FormParams().use("filename", componentName)
                    .use("override", "false")
                    .use("scenarioName", scenarioName));

        ResponseWrapper<PostComponentResponse> responseWrapper = HTTP2Request.build(requestEntity).post();

        Assert.assertEquals(String.format("The component with a part name %s, and scenario name %s, was not uploaded.", componentName, scenarioName),
            HttpStatus.SC_CREATED, responseWrapper.getStatusCode());

        ResponseWrapper<CssComponentResponse> itemResponse = getUnCostedCssComponents(componentName, scenarioName);

        Assert.assertEquals("The component response should be okay.", HttpStatus.SC_OK, itemResponse.getStatusCode());
        return itemResponse.getResponseEntity().getItems().get(0);
    }
}
