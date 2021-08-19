package com.apriori.acs.utils;

import com.apriori.acs.entity.enums.AcsApiEnum;
import com.apriori.acs.entity.response.createmissingscenario.CreateMissingScenarioInputs;
import com.apriori.acs.entity.response.createmissingscenario.CreateMissingScenarioResponse;
import com.apriori.acs.entity.response.createmissingscenario.ScenarioIterationKey;
import com.apriori.acs.entity.response.getenabledcurrencyrateversions.CurrencyRateVersionResponse;
import com.apriori.acs.entity.response.getscenarioinfobyscenarioiterationkey.GetScenarioInfoByScenarioIterationKeyResponse;
import com.apriori.acs.entity.response.getsetdisplayunits.GetDisplayUnitsResponse;
import com.apriori.acs.entity.response.getsetdisplayunits.SetDisplayUnitsInputs;
import com.apriori.acs.entity.response.getsetdisplayunits.SetDisplayUnitsResponse;
import com.apriori.acs.entity.response.getunitvariantsettings.GetUnitVariantSettingsResponse;
import com.apriori.acs.entity.response.getunitvariantsettings.UnitVariantSetting;
import com.apriori.apibase.utils.APIAuthentication;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.http.builder.dao.GenericRequestUtil;
import com.apriori.utils.http.builder.service.RequestAreaApi;
import com.apriori.utils.http2.builder.common.entity.RequestEntity;
import com.apriori.utils.http2.builder.service.HTTP2Request;
import com.apriori.utils.http2.utils.RequestEntityUtil;
import com.apriori.utils.properties.PropertiesContext;

import com.apriori.utils.users.UserUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class AcsResources {

    private static final Logger logger = LoggerFactory.getLogger(AcsResources.class);
    private static final long WAIT_TIME = 180;

    private static final HashMap<String, String> token = new APIAuthentication()
            .initAuthorizationHeaderNoContent(UserUtil.getUser().getUsername());

    private final String contentType = "Content-Type";
    private final String applicationJson = "application/json";

    /**
     * Creates Missing Scenario
     *
     * @return CreateMissingScenarioResponse response instance
     */
    public CreateMissingScenarioResponse createMissingScenario() {
        token.put(contentType, applicationJson);

        final RequestEntity requestEntity = RequestEntityUtil.init(AcsApiEnum.CREATE_MISSING_SCENARIO, CreateMissingScenarioResponse.class)
                .headers(token)
                .body(CreateMissingScenarioInputs.builder()
                        .baseName(Constants.PART_FILE_NAME)
                        .configurationName(Constants.PART_CONFIG_NAME)
                        .modelName(Constants.PART_MODEL_NAME)
                        .scenarioName(new GenerateStringUtil().generateScenarioName())
                        .scenarioType(Constants.PART_COMPONENT_TYPE)
                        .missing(true)
                        .createdBy(Constants.USERNAME).build());

        return (CreateMissingScenarioResponse) HTTP2Request.build(requestEntity).post().getResponseEntity();
    }


    /**
     * Gets Scenario Information by Scenario Iteration Key
     *
     * @return GetScenarioInfoByScenarioIterationKeyResponse instance
     */
    public GetScenarioInfoByScenarioIterationKeyResponse getScenarioInfoByScenarioIterationKey(
            ScenarioIterationKey scenarioIterationKey) {
        token.put(contentType, applicationJson);

        final RequestEntity requestEntity = RequestEntityUtil
                .init(AcsApiEnum.GET_SCENARIO_INFO_BY_SCENARIO_ITERATION_KEY,
                        GetScenarioInfoByScenarioIterationKeyResponse.class)
                .headers(token)
                .inlineVariables(
                        scenarioIterationKey.getScenarioKey().getTypeName(),
                        scenarioIterationKey.getScenarioKey().getMasterName(),
                        scenarioIterationKey.getScenarioKey().getStateName(),
                        scenarioIterationKey.getIteration().toString()
                );

        return (GetScenarioInfoByScenarioIterationKeyResponse) HTTP2Request
                .build(requestEntity).get().getResponseEntity();
    }

    /**
     * Gets Display Units
     *
     * @return GetDisplayUnitsResponse
     */
    public GetDisplayUnitsResponse getDisplayUnits() {
        token.put(contentType, applicationJson);

        final RequestEntity requestEntity = RequestEntityUtil.init(AcsApiEnum.GET_DISPLAY_UNITS, GetDisplayUnitsResponse.class)
                .headers(token)
                .inlineVariables(Constants.USERNAME);

        return (GetDisplayUnitsResponse) HTTP2Request.build(requestEntity).get().getResponseEntity();
    }

    /**
     * Sets Display Units
     *
     * @return Set Display Units response instance
     */
    public SetDisplayUnitsResponse setDisplayUnits(SetDisplayUnitsInputs setDisplayUnitsInputs) {
        token.put(contentType, applicationJson);

        final RequestEntity requestEntity = RequestEntityUtil.init(AcsApiEnum.SET_DISPLAY_UNITS, SetDisplayUnitsResponse.class)
                .headers(token)
                .body(setDisplayUnitsInputs)
                .inlineVariables(Constants.USERNAME);

        return (SetDisplayUnitsResponse) HTTP2Request.build(requestEntity).post().getResponseEntity();
    }

    /**
     * Gets Unit Variant Settings
     *
     * @return GetUnitVariantSettingsResponse instance
     */
    public GetUnitVariantSettingsResponse getUnitVariantSettings() {
        token.put(contentType, applicationJson);

        final RequestEntity requestEntity = RequestEntityUtil
                .init(AcsApiEnum.GET_UNIT_VARIANT_SETTINGS, GetUnitVariantSettingsResponse.class)
                .headers(token);

        return (GetUnitVariantSettingsResponse) HTTP2Request.build(requestEntity).get().getResponseEntity();
    }

    /**
     * Gets Custom Unit Variant Settings
     *
     * @return GetUnitVariantSettingsResponse instance
     */
    public UnitVariantSetting getCustomUnitVariantSettings() {
        token.put(contentType, applicationJson);

        final RequestEntity requestEntity = RequestEntityUtil
                .init(AcsApiEnum.GET_CUSTOM_UNIT_VARIANT_SETTINGS, UnitVariantSetting.class)
                .headers(token)
                .inlineVariables(Constants.USERNAME);

        return (UnitVariantSetting) HTTP2Request.build(requestEntity).get().getResponseEntity();
    }

    /**
     * Gets Enabled Currency Rate Versions
     *
     * @return GetEnabledCurrencyRateVersions instance
     */
    public CurrencyRateVersionResponse getEnabledCurrencyRateVersions() {
        token.put(contentType, applicationJson);

        final RequestEntity requestEntity = RequestEntityUtil
                .init(AcsApiEnum.GET_ENABLED_CURRENCY_RATE_VERSIONS, CurrencyRateVersionResponse.class)
                .headers(token);

        return (CurrencyRateVersionResponse) HTTP2Request.build(requestEntity).get().getResponseEntity();
    }
}
