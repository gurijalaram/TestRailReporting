package util;

import com.apriori.apibase.utils.APIAuthentication;
import com.apriori.apibase.utils.CommonRequestUtil;
import com.apriori.apibase.utils.JwtTokenUtil;
import com.apriori.apibase.utils.TestUtil;
import com.apriori.entity.response.PostComponentResponse;
import com.apriori.sds.entity.enums.SDSAPIEnum;
import com.apriori.sds.utils.Constants;
import com.apriori.utils.CidAppTestUtil;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.utils.FormParams;
import com.apriori.utils.http.utils.MultiPartFiles;
import com.apriori.utils.http.utils.ResponseWrapper;
import org.apache.http.HttpStatus;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import static org.junit.Assert.assertEquals;

public class SDSTestUtil extends TestUtil {

    protected static String token;
    private static PostComponentResponse partPostComponentResponse;

    @BeforeClass
    public static void initTestingComponentInfo() {
        initToken();
        postTestingComponent();
    }

    @AfterClass
    public static void clearTestingData() {
        removeTestingComponent();
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
        ResponseWrapper<PostComponentResponse> response =
            new CommonRequestUtil().postCommonRequest(initRequestEntity());

        assertEquals("The response code should be as expected ",  HttpStatus.SC_CREATED, response.getStatusCode());

        partPostComponentResponse = response.getResponseEntity();

        new CidAppTestUtil().getScenarioRepresentation("processing",
            partPostComponentResponse.getComponentIdentity(),
            partPostComponentResponse.getScenarioIdentity()
        );

        return response;
    }

    protected static ResponseWrapper removeTestingComponent() {
        ResponseWrapper response =
            new CommonRequestUtil().deleteCommonRequestWithInlineVariables(SDSAPIEnum.DELETE_SCENARIO_BY_COMPONENT_SCENARIO_IDS, null,
                new APIAuthentication().initAuthorizationHeaderContent(token), getComponentId(), getScenarioId()
            );

        assertEquals("The response code should be as expected ",  HttpStatus.SC_OK, response.getStatusCode());

        return response;
    }

    private static String initToken() {
        if( token == null ) {
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

    private static RequestEntity initRequestEntity() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        String partName = "Casting.prt";
        String processGroup = "Casting - Die";

        return RequestEntity.init(SDSAPIEnum.POST_COMPONENTS, PostComponentResponse.class)
            .setHeaders(new APIAuthentication().initAuthorizationHeaderContent(token))
            .setMultiPartFiles(new MultiPartFiles().use("data", FileResourceUtil.getCloudFile(ProcessGroupEnum.fromString(processGroup), partName)))
            .setFormParams(new FormParams().use("filename", "Casting.prt")
                .use("override", "false")
                .use("scenarioName", scenarioName));
    }
}
