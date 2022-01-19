package com.apriori.acs.utils;

import com.apriori.acs.entity.enums.AcsApiEnum;
import com.apriori.acs.entity.response.GenericResourceCreatedResponse;
import com.apriori.acs.entity.response.createmissingscenario.CreateMissingScenarioInputs;
import com.apriori.acs.entity.response.createmissingscenario.CreateMissingScenarioResponse;
import com.apriori.acs.entity.response.getenabledcurrencyrateversions.CurrencyRateVersionResponse;
import com.apriori.acs.entity.response.getscenarioinfobyscenarioiterationkey
        .GetScenarioInfoByScenarioIterationKeyResponse;
import com.apriori.acs.entity.response.getscenariosinfo.GetScenariosInfoResponse;
import com.apriori.acs.entity.response.getscenariosinfo.ScenarioIterationKeysInputs;
import com.apriori.acs.entity.response.getsetdisplayunits.GetDisplayUnitsResponse;
import com.apriori.acs.entity.response.getsetdisplayunits.SetDisplayUnitsInputs;
import com.apriori.acs.entity.response.getsetproductiondefaults.GetProductionDefaultsResponse;
import com.apriori.acs.entity.response.getsetproductiondefaults.SetProductionDefaultsInputs;
import com.apriori.acs.entity.response.getsettolerancepolicydefaults.GetTolerancePolicyDefaultsResponse;
import com.apriori.acs.entity.response.getsettolerancepolicydefaults.SetTolerancePolicyDefaultsInputs;
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

    private final String validUsername = UserUtil.getUser().getUsername();
    private final String invalidUsername = UserUtil.getUser().getUsername().substring(0, 14).concat("41");

    private final String contentType = "Content-Type";
    private final String applicationJson = "application/json";

    /**
     * Creates Missing Scenario
     *
     * @return CreateMissingScenarioResponse response instance
     */
    public CreateMissingScenarioResponse createMissingScenario() {
        token.put(contentType, applicationJson);

        final RequestEntity requestEntity = RequestEntityUtil.init(AcsApiEnum.CREATE_MISSING_SCENARIO,
                        CreateMissingScenarioResponse.class)
                .headers(token)
                .body(CreateMissingScenarioInputs.builder()
                        .baseName(Constants.PART_FILE_NAME)
                        .configurationName(Constants.PART_CONFIG_NAME)
                        .modelName(Constants.PART_MODEL_NAME)
                        .scenarioName(new GenerateStringUtil().generateScenarioName())
                        .scenarioType(Constants.PART_COMPONENT_TYPE)
                        .missing(true)
                        .createdBy(validUsername)
                        .build()
                );

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
    public ResponseWrapper<GetScenariosInfoResponse> getScenariosInformation(
            ScenarioIterationKey scenarioIterationKeyOne,
            ScenarioIterationKey scenarioIterationKeyTwo) {
        token.put(contentType, applicationJson);

        ArrayList<ScenarioIterationKey> listOfKeys = new ArrayList<>();
        listOfKeys.add(scenarioIterationKeyOne);
        listOfKeys.add(scenarioIterationKeyTwo);

        final RequestEntity requestEntity = RequestEntityUtil.init(AcsApiEnum.GET_SCENARIOS_INFORMATION,
                        GetScenariosInfoResponse.class)
                .headers(token)
                .body(ScenarioIterationKeysInputs.builder()
                        .scenarioIterationKeys(listOfKeys)
                        .build())
                .inlineVariables(validUsername);

        return HTTPRequest.build(requestEntity).post();
    }

    public ResponseWrapper<GetScenariosInfoResponse> getScenariosInformation2(
            ArrayList<ScenarioIterationKey> scenarioIterationKeys) {
        token.put(contentType, applicationJson);

        final RequestEntity requestEntity = RequestEntityUtil
                .init(AcsApiEnum.GET_SCENARIOS_INFORMATION, GetScenariosInfoResponse.class)
                .headers(token)
                .body(ScenarioIterationKeysInputs.builder()
                        .scenarioIterationKeys(scenarioIterationKeys)
                        .build())
                .inlineVariables(validUsername);

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
                .inlineVariables(validUsername);

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Gets Display Units
     *
     * @return GetDisplayUnitsResponse
     */
    public GetDisplayUnitsResponse getDisplayUnits() {
        token.put(contentType, applicationJson);

        final RequestEntity requestEntity = RequestEntityUtil.init(AcsApiEnum.GET_DISPLAY_UNITS,
                        GetDisplayUnitsResponse.class)
                .headers(token)
                .inlineVariables(validUsername);

        return (GetDisplayUnitsResponse) HTTPRequest.build(requestEntity).get().getResponseEntity();
    }

    /**
     * Sets Display Units
     *
     * @return Set Display Units response instance
     */
    public GenericResourceCreatedResponse setDisplayUnits(SetDisplayUnitsInputs setDisplayUnitsInputs) {
        token.put(contentType, applicationJson);

        final RequestEntity requestEntity = RequestEntityUtil.init(AcsApiEnum.SET_DISPLAY_UNITS,
                        GenericResourceCreatedResponse.class)
                .headers(token)
                .body(setDisplayUnitsInputs)
                .inlineVariables(validUsername);

        return (GenericResourceCreatedResponse) HTTPRequest.build(requestEntity).post().getResponseEntity();
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
                .inlineVariables(validUsername);

        return (UnitVariantSetting) HTTPRequest.build(requestEntity).get().getResponseEntity();
    }

    /**
     * Gets Enabled Currency Rate Versions
     * Return is void because the mapping to CurrencyRateVersionResponse is the validation, so no assertions required
     */
    public void getEnabledCurrencyRateVersions() {
        token.put(contentType, applicationJson);

        RequestEntityUtil
                .init(AcsApiEnum.GET_ENABLED_CURRENCY_RATE_VERSIONS, CurrencyRateVersionResponse.class)
                .headers(token);
    }

    /**
     * Gets Tolerance Policy Defaults
     *
     * @return GetTolerancePolicyDefaults instance
     */
    public GetTolerancePolicyDefaultsResponse getTolerancePolicyDefaults() {
        token.put(contentType, applicationJson);

        final RequestEntity requestEntity = RequestEntityUtil
                .init(AcsApiEnum.GET_SET_TOLERANCE_POLICY_DEFAULTS, GetTolerancePolicyDefaultsResponse.class)
                .headers(token)
                .inlineVariables(validUsername);

        return (GetTolerancePolicyDefaultsResponse) HTTPRequest.build(requestEntity).get().getResponseEntity();
    }

    /**
     * Gets Tolerance Policy Defaults with invalid user to produce 400 error
     *
     * @return String of error
     */
    public String getTolerancePolicyDefaults400Error() {
        token.put(contentType, applicationJson);

        final RequestEntity requestEntity = RequestEntityUtil
                .init(AcsApiEnum.GET_SET_TOLERANCE_POLICY_DEFAULTS, null)
                .headers(token)
                .inlineVariables(invalidUsername);

        return HTTPRequest.build(requestEntity).get().getBody();
    }

    /**
     * Sets Tolerance Policy Defaults Values
     *
     * @param totalRunoutOverride - double
     * @param toleranceMode - String
     * @param useCadToleranceThreshhold - boolean
     *
     * @return GenericResourceCreatedResponse
     */
    public GenericResourceCreatedResponse setTolerancePolicyDefaults(double totalRunoutOverride,
                                                                     String toleranceMode,
                                                                     boolean useCadToleranceThreshhold) {
        token.put(contentType, applicationJson);

        final RequestEntity requestEntity = RequestEntityUtil
                .init(AcsApiEnum.GET_SET_TOLERANCE_POLICY_DEFAULTS, GenericResourceCreatedResponse.class)
                .headers(token)
                .body(SetTolerancePolicyDefaultsInputs.builder()
                        .totalRunoutOverride(totalRunoutOverride)
                        .toleranceMode(toleranceMode)
                        .useCadToleranceThreshhold(useCadToleranceThreshhold)
                        .build()
                )
                .inlineVariables(validUsername);

        return (GenericResourceCreatedResponse) HTTPRequest.build(requestEntity).post().getResponseEntity();
    }

    /**
     * Set Tolerance Policy Defaults with Invalid Username to produce error
     *
     * @param totalRunoutOverride - double
     * @param toleranceMode - String
     * @param useCadToleranceThreshhold - boolean
     * @return String of error
     */
    public String setTolerancePolicyDefaultsInvalidUsername(double totalRunoutOverride,
                                                     String toleranceMode,
                                                     boolean useCadToleranceThreshhold) {
        token.put(contentType, applicationJson);

        final RequestEntity requestEntity = RequestEntityUtil
                .init(AcsApiEnum.GET_SET_TOLERANCE_POLICY_DEFAULTS, null)
                .headers(token)
                .body(SetTolerancePolicyDefaultsInputs.builder()
                        .totalRunoutOverride(totalRunoutOverride)
                        .toleranceMode(toleranceMode)
                        .useCadToleranceThreshhold(useCadToleranceThreshhold)
                        .build()
                )
                .inlineVariables(invalidUsername);

        return HTTPRequest.build(requestEntity).post().getBody();
    }

    /**
     * Gets Production Defaults
     *
     * @return GetProductionDefaultsResponse instance - response from API
     */
    public GetProductionDefaultsResponse getProductionDefaults() {
        token.put(contentType, applicationJson);

        final RequestEntity requestEntity = RequestEntityUtil
                .init(AcsApiEnum.GET_SET_PRODUCTION_DEFAULTS, GetProductionDefaultsResponse.class)
                .headers(token)
                .inlineVariables(validUsername);

        return (GetProductionDefaultsResponse) HTTPRequest.build(requestEntity).get().getResponseEntity();
    }

    /**
     * Gets Production Defaults
     *
     * @return GetProductionDefaultsResponse instance - response from API
     */
    public String getProductionDefaultsInvalidUsername() {
        token.put(contentType, applicationJson);

        final RequestEntity requestEntity = RequestEntityUtil
                .init(AcsApiEnum.GET_SET_PRODUCTION_DEFAULTS, null)
                .headers(token)
                .inlineVariables(invalidUsername);

        return HTTPRequest.build(requestEntity).get().getBody();
    }

    /**
     * Sets production defaults
     *
     * @return GenericResourceCreatedResponse
     */
    public GenericResourceCreatedResponse setProductionDefaults() {
        token.put(contentType, applicationJson);

        final RequestEntity requestEntity = RequestEntityUtil
                .init(AcsApiEnum.GET_SET_PRODUCTION_DEFAULTS, GenericResourceCreatedResponse.class)
                .headers(token)
                .body(SetProductionDefaultsInputs.builder()
                        .material("Accura 10")
                        .annualVolume("5500")
                        .productionLife(5.0)
                        .batchSize(458)
                        .useVpeForAllProcesses(false)
                        .batchSizeMode(false)
                        .build()
                ).inlineVariables(validUsername);

        return (GenericResourceCreatedResponse) HTTPRequest.build(requestEntity).post().getResponseEntity();
    }

    /**
     * Calls set production defaults endpoint with invalid username
     *
     * @return String of body, which contains the error
     */
    public String setProductionDefaultsInvalidUsername() {
        token.put(contentType, applicationJson);

        final RequestEntity requestEntity = RequestEntityUtil
                .init(AcsApiEnum.GET_SET_PRODUCTION_DEFAULTS, null)
                .headers(token)
                .body(SetProductionDefaultsInputs.builder()
                        .material("Accura 10")
                        .annualVolume("5500")
                        .productionLife(5.0)
                        .batchSize(458)
                        .useVpeForAllProcesses(false)
                        .batchSizeMode(false)
                        .build()
                ).inlineVariables(invalidUsername);

        return HTTPRequest.build(requestEntity).post().getBody();
    }
}
