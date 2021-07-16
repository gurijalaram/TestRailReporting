package com.apriori.acs.utils;

import com.apriori.acs.entity.response.ScenarioIterationKey;
import com.apriori.acs.entity.response.createmissingscenario.CreateMissingScenarioInputs;
import com.apriori.acs.entity.response.createmissingscenario.CreateMissingScenarioResponse;
import com.apriori.acs.entity.response.publish.getscenarioinfobyscenarioiterationkey.GetScenarioInfoByScenarioIterationKeyResponse;
import com.apriori.apibase.utils.APIAuthentication;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.dao.GenericRequestUtil;
import com.apriori.utils.http.builder.service.RequestAreaApi;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class AcsResources {

    private static final Logger logger = LoggerFactory.getLogger(AcsResources.class);
    private static final long WAIT_TIME = 180;

    private static final HashMap<String, String> token = new APIAuthentication()
            .initAuthorizationHeaderNoContent("aPrioriCIGenerateUser@apriori.com");

    private final String orderSuccess = "SUCCESS";
    private final String orderFailed = "FAILED";
    private final String acceptHeader = "Accept";
    private final String contentType = "Content-Type";
    private final String applicationJson = "application/json";
    private final String textPlain = "text/plain";
    Map<String, String> headers = new HashMap<>();
    private String baseUrl = System.getProperty("baseUrl");
    private String sessionUrl = "apriori/cost/session/";

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Creates Missing Scenario
     *
     * @return CreateMissingScenarioResponse response instance
     */
    public CreateMissingScenarioResponse createMissingScenario() {
        String createMissingScenarioUrl = baseUrl.concat("ws/workspace/0/scenarios");

        headers.put(contentType, applicationJson);

        RequestEntity createMissingScenarioRequestEntity =
                RequestEntity.init(createMissingScenarioUrl, CreateMissingScenarioResponse.class)
                .setHeaders(headers)
                .setHeaders(token)
                .setBody(CreateMissingScenarioInputs.builder()
                        .baseName("Cap Screw Hex Head (Metric)")
                        .configurationName("M12 x 16")
                        .modelName("Cap Screw Hex Head (Metric) (M12 x 16)")
                        .scenarioName(new GenerateStringUtil().generateScenarioName())
                        .scenarioType("PART")
                        .missing(true)
                        .createdBy("bhegan").build()
                );

        return (CreateMissingScenarioResponse) GenericRequestUtil.post(createMissingScenarioRequestEntity,
                new RequestAreaApi()).getResponseEntity();
    }

    /**
     * Gets Scenario Information by Scenario Iteration Key
     *
     * @return GetScenarioInfoByScenarioIterationKeyResponse instance
     */
    public GetScenarioInfoByScenarioIterationKeyResponse getScenarioInfoByScenarioIterationKey(
            ScenarioIterationKey scenarioIterationKey) {
        String createMissingScenarioUrl = baseUrl.concat(
                String.format("ws/workspace/0/scenarios/%s/%s/%s/iterations/%s/scenario-info",
                        scenarioIterationKey.getScenarioKey().getTypeName(),
                        scenarioIterationKey.getScenarioKey().getMasterName(),
                        scenarioIterationKey.getScenarioKey().getStateName(),
                        scenarioIterationKey.getIteration()));

        headers.put(contentType, applicationJson);

        RequestEntity getScenarioInfoByScenarioIterationKeyRequestEntity =
                RequestEntity.init(createMissingScenarioUrl, GetScenarioInfoByScenarioIterationKeyResponse.class)
                        .setHeaders(headers)
                        .setHeaders(token);

        return (GetScenarioInfoByScenarioIterationKeyResponse) GenericRequestUtil
                .get(getScenarioInfoByScenarioIterationKeyRequestEntity, new RequestAreaApi()).getResponseEntity();
    }
}
