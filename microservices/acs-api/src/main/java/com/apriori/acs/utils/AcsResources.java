package com.apriori.acs.utils;

import com.apriori.acs.entity.enums.AcsApiEnum;
import com.apriori.acs.entity.response.createmissingscenario.CreateMissingScenarioInputs;
import com.apriori.acs.entity.response.createmissingscenario.CreateMissingScenarioResponse;
import com.apriori.acs.entity.response.getenabledcurrencyrateversions.CurrencyRateVersionResponse;
import com.apriori.acs.entity.response.getscenarioinfobyscenarioiterationkey.GetScenarioInfoByScenarioIterationKeyResponse;
import com.apriori.acs.entity.response.getscenariosinfo.GetScenariosInfoResponse;
import com.apriori.acs.entity.response.getscenariosinfo.ScenarioIterationKeysInputs;
import com.apriori.acs.entity.response.getsetdisplayunits.GetDisplayUnitsResponse;
import com.apriori.acs.entity.response.getsetdisplayunits.SetDisplayUnitsInputs;
import com.apriori.acs.entity.response.getsetdisplayunits.SetDisplayUnitsResponse;
import com.apriori.acs.entity.response.getunitvariantsettings.GetUnitVariantSettingsResponse;
import com.apriori.acs.entity.response.getunitvariantsettings.UnitVariantSetting;
import com.apriori.apibase.utils.APIAuthentication;
import com.apriori.entity.response.upload.ScenarioIterationKey;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.user.UserUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;

public class AcsResources {

    private static final Logger logger = LoggerFactory.getLogger(AcsResources.class);
    private static final long WAIT_TIME = 180;

    private static final HashMap<String, String> token = new APIAuthentication()
            .initAuthorizationHeaderNoContent(UserUtil.getUser().getEmail());

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

        return (CreateMissingScenarioResponse) HTTPRequest.build(requestEntity).post().getResponseEntity();
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

        return (GetScenarioInfoByScenarioIterationKeyResponse) HTTPRequest
                .build(requestEntity).get().getResponseEntity();
    }

    /**
     * Get Scenarios Information (multiple scenarios, one api call)
     *
     * @param scenarioIterationKeyOne - first Scenario Iteration Key
     * @param scenarioIterationKeyTwo - second Scenario Iteration Key
     * @return instance of GetScenariosInfoResponse
     */
    public ResponseWrapper<GetScenariosInfoResponse> getScenariosInformation(ScenarioIterationKey scenarioIterationKeyOne,
                                                                             ScenarioIterationKey scenarioIterationKeyTwo) {
        token.put(contentType, applicationJson);

        ArrayList<ScenarioIterationKey> listOfKeys = new ArrayList<>();
        listOfKeys.add(scenarioIterationKeyOne);
        listOfKeys.add(scenarioIterationKeyTwo);

        final RequestEntity requestEntity = RequestEntityUtil.init(AcsApiEnum.GET_SCENARIOS_INFORMATION, GetScenariosInfoResponse.class)
                .headers(token)
                .body(ScenarioIterationKeysInputs.builder()
                        .scenarioIterationKeys(listOfKeys)
                        .build())
                .inlineVariables(Constants.USERNAME);

        return HTTPRequest.build(requestEntity).post();
    }

    public ResponseWrapper<GetScenariosInfoResponse> getScenariosInformation2(
            ArrayList<ScenarioIterationKey> scenarioIterationKeys) {
        token.put(contentType, applicationJson);

        final RequestEntity requestEntity = RequestEntityUtil
                .init(AcsApiEnum.GET_SCENARIOS_INFORMATION, GetScenariosInfoResponse.class)
                .headers(token)
                .body(ScenarioIterationKeysInputs.builder()
                        .scenarioIterationKeys(scenarioIterationKeys).build()).inlineVariables(Constants.USERNAME);

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Get Scenarios Info, negative - sending null body
     *
     * @return ResponseWrapper<GetScenariosInfoResponse> instance
     */
    public ResponseWrapper<GetScenariosInfoResponse> getScenariosInfoNullBody() {
        token.put(contentType, applicationJson);

        final RequestEntity requestEntity = RequestEntityUtil.init(AcsApiEnum.GET_SCENARIOS_INFORMATION, null)
                .headers(token)
                .body(null)
                .inlineVariables(Constants.USERNAME);

        return HTTPRequest.build(requestEntity).post();
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

        return (GetDisplayUnitsResponse) HTTPRequest.build(requestEntity).get().getResponseEntity();
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

        return (SetDisplayUnitsResponse) HTTPRequest.build(requestEntity).post().getResponseEntity();
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

        return (GetUnitVariantSettingsResponse) HTTPRequest.build(requestEntity).get().getResponseEntity();
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

        return (UnitVariantSetting) HTTPRequest.build(requestEntity).get().getResponseEntity();
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

        return (CurrencyRateVersionResponse) HTTPRequest.build(requestEntity).get().getResponseEntity();
    }
}
