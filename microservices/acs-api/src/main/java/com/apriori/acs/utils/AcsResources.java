package com.apriori.acs.utils;

import com.apriori.acs.entity.response.createmissingscenario.ScenarioIterationKey;
import com.apriori.acs.entity.response.createmissingscenario.CreateMissingScenarioInputs;
import com.apriori.acs.entity.response.createmissingscenario.CreateMissingScenarioResponse;
import com.apriori.acs.entity.response.getenabledcurrencyrateversions.GetEnabledCurrencyRateVersionsResponse;
import com.apriori.acs.entity.response.getsetdisplayunits.GetDisplayUnitsResponse;
import com.apriori.acs.entity.response.getsetdisplayunits.SetDisplayUnitsInputs;
import com.apriori.acs.entity.response.getscenarioinfobyscenarioiterationkey
        .GetScenarioInfoByScenarioIterationKeyResponse;
import com.apriori.acs.entity.response.getsetdisplayunits.SetDisplayUnitsResponse;
import com.apriori.acs.entity.response.getunitvariantsettings.GetUnitVariantSettingsResponse;
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
                        .baseName(Constants.PART_FILE_NAME)
                        .configurationName(Constants.PART_CONFIG_NAME)
                        .modelName(Constants.PART_MODEL_NAME)
                        .scenarioName(new GenerateStringUtil().generateScenarioName())
                        .scenarioType(Constants.PART_COMPONENT_TYPE)
                        .missing(true)
                        .createdBy(Constants.USERNAME).build()
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

    /**
     * Gets Display Units
     *
     * @return GetDisplayUnitsResponse
     */
    public GetDisplayUnitsResponse getDisplayUnits() {
        String getDisplayUnitsUrl = baseUrl.concat(
                String.format("ws/workspace/users/%s/display-units", Constants.USERNAME));

        headers.put(contentType, applicationJson);

        RequestEntity getDisplayUnitsRequestEntity = RequestEntity.init(
                getDisplayUnitsUrl, GetDisplayUnitsResponse.class)
                .setHeaders(headers)
                .setHeaders(token);

        return (GetDisplayUnitsResponse) GenericRequestUtil.get(getDisplayUnitsRequestEntity, new RequestAreaApi())
                .getResponseEntity();
    }

    /**
     * Sets Display Units
     *
     * @return Set Display Units response instance
     */
    public SetDisplayUnitsResponse setDisplayUnits(SetDisplayUnitsInputs setDisplayUnitsInputs) {
        String setDisplayUnitsUrl = baseUrl.concat(
                String.format("ws/workspace/users/%s/display-units", Constants.USERNAME));

        headers.put(contentType, applicationJson);

        RequestEntity setDisplayUnitsRequestEntity = RequestEntity.init(
                setDisplayUnitsUrl, SetDisplayUnitsResponse.class)
                .setHeaders(headers)
                .setHeaders(token)
                .setBody(setDisplayUnitsInputs);

        return (SetDisplayUnitsResponse) GenericRequestUtil.post(setDisplayUnitsRequestEntity, new RequestAreaApi())
                .getResponseEntity();
    }

    /**
     * Gets Unit Variant Settings
     *
     * @return GetUnitVariantSettingsResponse instance
     */
    public GetUnitVariantSettingsResponse getUnitVariantSettings() {
        String getUnitVariantSettingUrl = baseUrl.concat("ws/workspace/global-info/unitVariantSettings");

        headers.put(contentType, applicationJson);

        RequestEntity getUnitVariantSettingsRequestEntity = RequestEntity.init(
                getUnitVariantSettingUrl, GetUnitVariantSettingsResponse.class)
                .setHeaders(headers)
                .setHeaders(token);

        return (GetUnitVariantSettingsResponse) GenericRequestUtil.get(getUnitVariantSettingsRequestEntity,
                new RequestAreaApi()).getResponseEntity();
    }

    /**
     * Gets Enabled Currency Rate Versions
     *
     * @return GetEnabledCurrencyRateVersions instance
     */
    public GetEnabledCurrencyRateVersionsResponse getEnabledCurrencyRateVersions() {
        String getEnabledCurrencyRateVersionsUrl = baseUrl.concat("ws/workspace/global-info/enabledCurrency");

        headers.put(contentType, applicationJson);

        RequestEntity getEnabledCurrencyRateVersionsRequestEntity = RequestEntity.init(
                getEnabledCurrencyRateVersionsUrl, GetEnabledCurrencyRateVersionsResponse.class)
                .setHeaders(headers)
                .setHeaders(token);

        return (GetEnabledCurrencyRateVersionsResponse) GenericRequestUtil
                .get(getEnabledCurrencyRateVersionsRequestEntity,
                new RequestAreaApi()).getResponseEntity();
    }
}
