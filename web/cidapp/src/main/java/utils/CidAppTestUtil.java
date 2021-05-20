package utils;

import com.apriori.apibase.utils.JwtTokenUtil;
import com.apriori.entity.response.PostComponentResponse;
import com.apriori.entity.response.css.CssComponentResponse;
import com.apriori.entity.response.css.Item;
import com.apriori.utils.http.utils.FormParams;
import com.apriori.utils.http.utils.MultiPartFiles;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.http2.builder.common.entity.RequestEntity;
import com.apriori.utils.http2.builder.service.HTTP2Request;
import com.apriori.utils.http2.utils.RequestEntityUtil;
import com.apriori.utils.users.UserCredentials;

import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * @author cfrith
 */

// TODO: 18/05/2021 cf - a duplicate of microservices:cidapp for now but a ticket needs to be created to refactor to its own class.

public class CidAppTestUtil {
    private static final Logger logger = LoggerFactory.getLogger(CidAppTestUtil.class);

    private static String token = new JwtTokenUtil().retrieveJwtToken(Constants.getSecretKey(),
        Constants.getCidServiceHost(),
        HttpStatus.SC_CREATED,
        Constants.getCidTokenUsername(),
        Constants.getCidTokenEmail(),
        Constants.getCidTokenIssuer(),
        Constants.getCidTokenSubject());

    static {
        RequestEntityUtil.useTokenForRequests(token);
    }

    /**
     * Adds a new component
     *
     * @param scenarioName    - the scenario name
     * @param processGroup    - the process group
     * @param partName        - the part name
     * @param userCredentials - the user credentials
     * @return response object
     */
    public Item postComponents(String scenarioName, String processGroup, File partName, UserCredentials userCredentials) {

        if (userCredentials.getToken() != null) {
            token = userCredentials.getToken();
        } else {
            token = new JwtTokenUtil().retrieveJwtToken(Constants.getSecretKey(),
                Constants.getCidServiceHost(),
                HttpStatus.SC_CREATED,
                userCredentials.getUsername().split("@")[0],
                userCredentials.getUsername(),
                Constants.getCidTokenIssuer(),
                Constants.getCidTokenSubject());
        }

        RequestEntityUtil.useTokenForRequests(token);
        return postComponents(scenarioName, processGroup, partName);
    }

    /**
     * Adds a new component
     *
     * @param scenarioName  - the scenario name
     * @param componentName - the part name
     * @return responsewrapper
     */
    public Item postComponents(String componentName, String scenarioName, File processGroup) {
        RequestEntity requestEntity =
            RequestEntityUtil.init(CidAppAPIEnum.POST_COMPONENTS, PostComponentResponse.class)
                .multiPartFiles(new MultiPartFiles().use("data", processGroup))
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

    /**
     * Gets the uncosted component from Css
     *
     * @param componentName - the component name
     * @param scenarioName  - the scenario name
     * @return response object
     */
    public ResponseWrapper<CssComponentResponse> getUnCostedCssComponents(String componentName, String scenarioName) {
        RequestEntity requestEntity = RequestEntityUtil.init(CssAPIEnum.GET_COMPONENT_BY_COMPONENT_SCENARIO_NAMES, CssComponentResponse.class)
            .inlineVariables(Arrays.asList(componentName.split("\\.")[0].toUpperCase(), scenarioName));

        int currentCount = 0;
        int attemptsCount = 60;
        int secondsToWait = 2;

        final String verifiedState = "NOT_COSTED";

        try {

            do {
                TimeUnit.SECONDS.sleep(secondsToWait);

                ResponseWrapper<CssComponentResponse> scenarioRepresentation = HTTP2Request.build(requestEntity).get();

                Assert.assertEquals(String.format("Failed to receive data about component name: %s, with scenario name: %s", componentName, scenarioName),
                    HttpStatus.SC_OK, scenarioRepresentation.getStatusCode());

                if (!scenarioRepresentation.getResponseEntity().getItems().isEmpty()
                    && scenarioRepresentation.getResponseEntity().getItems().get(0).getScenarioState().equals(verifiedState.toUpperCase())) {
                    return scenarioRepresentation;
                }

            } while (currentCount++ <= attemptsCount);

        } catch (InterruptedException e) {
            logger.error(e.getMessage());
            Thread.currentThread().interrupt();
        }

        throw new IllegalArgumentException(
            String.format("Failed to get uploaded component name: %s, with scenario name: %s, after %d attempts with period in %d seconds.",
                componentName, scenarioName, attemptsCount, secondsToWait)
        );
    }
}
