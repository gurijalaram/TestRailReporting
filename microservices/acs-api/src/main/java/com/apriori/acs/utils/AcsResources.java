package com.apriori.acs.utils;

import com.apriori.acs.entity.enums.AcsApiEnum;
import com.apriori.acs.entity.response.GenericResourceCreatedResponse;
import com.apriori.acs.entity.response.createmissingscenario.CreateMissingScenarioInputs;
import com.apriori.acs.entity.response.createmissingscenario.CreateMissingScenarioResponse;
import com.apriori.acs.entity.response.getenabledcurrencyrateversions.CurrencyRateVersionResponse;
import com.apriori.acs.entity.response.getscenarioinfobyscenarioiterationkey.GetScenarioInfoByScenarioIterationKeyResponse;
import com.apriori.acs.entity.response.getscenariosinfo.GetScenariosInfoResponse;
import com.apriori.acs.entity.response.getscenariosinfo.ScenarioIterationKeysInputs;
import com.apriori.acs.entity.response.getsetdisplayunits.GetDisplayUnitsResponse;
import com.apriori.acs.entity.response.getsetdisplayunits.SetDisplayUnitsInputs;
import com.apriori.acs.entity.response.getsetproductiondefaults.GetProductionDefaultsResponse;
import com.apriori.acs.entity.response.getsetproductiondefaults.SetProductionDefaultsInputs;
import com.apriori.acs.entity.response.getsettolerancepolicydefaults.GetTolerancePolicyDefaultsResponse;
import com.apriori.acs.entity.response.getsettolerancepolicydefaults.SetTolerancePolicyDefaultsInputs;
import com.apriori.acs.entity.response.getsetuserpreferences.GetUserPreferencesResponse;
import com.apriori.acs.entity.response.getunitvariantsettings.GetUnitVariantSettingsResponse;
import com.apriori.acs.entity.response.getunitvariantsettings.UnitVariantSetting;
import com.apriori.apibase.utils.APIAuthentication;
import com.apriori.entity.response.upload.ScenarioIterationKey;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.enums.EndpointEnum;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.user.UserUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AcsResources {

    private static final Logger logger = LoggerFactory.getLogger(AcsResources.class);
    private static final long WAIT_TIME = 180;

    private static final HashMap<String, String> token = new APIAuthentication()
            .initAuthorizationHeaderNoContent(UserUtil.getUser().getEmail());

    private static final HashMap<String, String> headers = new HashMap<>();

    private final String validUsername = UserUtil.getUser().getUsername();
    private final String invalidUsername = UserUtil.getUser().getUsername().substring(0, 14).concat("41");

    /**
     * Creates Missing Scenario
     *
     * @return CreateMissingScenarioResponse response instance
     */
    public CreateMissingScenarioResponse createMissingScenario() {
        setupHeader();

        final RequestEntity requestEntity = RequestEntityUtil.init(AcsApiEnum.CREATE_MISSING_SCENARIO,
                        CreateMissingScenarioResponse.class)
                .headers(headers)
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
    public GetScenarioInfoByScenarioIterationKeyResponse getScenarioInfoByScenarioIterationKey(ScenarioIterationKey scenarioIterationKey) {
        setupHeader();

        final RequestEntity requestEntity = RequestEntityUtil
                .init(AcsApiEnum.GET_SCENARIO_INFO_BY_SCENARIO_ITERATION_KEY, GetScenarioInfoByScenarioIterationKeyResponse.class)
                .headers(headers)
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
        setupHeader();

        List<ScenarioIterationKey> listOfKeys = new ArrayList<>();
        listOfKeys.add(scenarioIterationKeyOne);
        listOfKeys.add(scenarioIterationKeyTwo);

        final RequestEntity requestEntity = RequestEntityUtil.init(AcsApiEnum.GET_SCENARIOS_INFORMATION, GetScenariosInfoResponse.class)
                .headers(headers)
                .body(ScenarioIterationKeysInputs.builder()
                        .scenarioIterationKeys(listOfKeys)
                        .build())
                .inlineVariables(validUsername);

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Get Scenarios Info (one scenario)
     *
     * @param scenarioIterationKeys List of scenario iteration keys
     * @return ResponseWrapper instance
     */
    public ResponseWrapper<GetScenariosInfoResponse> getScenariosInformationOneScenario(List<ScenarioIterationKey> scenarioIterationKeys) {
        setupHeader();

        final RequestEntity requestEntity = RequestEntityUtil
                .init(AcsApiEnum.GET_SCENARIOS_INFORMATION, GetScenariosInfoResponse.class)
                .headers(headers)
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
        setupHeader();

        final RequestEntity requestEntity = RequestEntityUtil.init(AcsApiEnum.GET_SCENARIOS_INFORMATION, null)
                .headers(headers)
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
        setupHeader();

        final RequestEntity requestEntity = RequestEntityUtil.init(AcsApiEnum.GET_DISPLAY_UNITS,
                        GetDisplayUnitsResponse.class)
                .headers(headers)
                .inlineVariables(validUsername);

        return (GetDisplayUnitsResponse) HTTPRequest.build(requestEntity).get().getResponseEntity();
    }

    /**
     * Sets Display Units
     *
     * @param setDisplayUnitsInputs - inputs for body of request
     * @return Set Display Units response instance
     */
    public GenericResourceCreatedResponse setDisplayUnits(SetDisplayUnitsInputs setDisplayUnitsInputs) {
        setupHeader();

        final RequestEntity requestEntity = RequestEntityUtil.init(AcsApiEnum.SET_DISPLAY_UNITS, GenericResourceCreatedResponse.class)
                .headers(headers)
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
        setupHeader();

        final RequestEntity requestEntity = RequestEntityUtil
                .init(AcsApiEnum.GET_UNIT_VARIANT_SETTINGS, GetUnitVariantSettingsResponse.class)
                .headers(headers);

        return (GetUnitVariantSettingsResponse) HTTPRequest.build(requestEntity).get().getResponseEntity();
    }

    /**
     * Gets Custom Unit Variant Settings
     *
     * @return GetUnitVariantSettingsResponse instance
     */
    public UnitVariantSetting getCustomUnitVariantSettings() {
        setupHeader();

        final RequestEntity requestEntity = RequestEntityUtil
                .init(AcsApiEnum.GET_CUSTOM_UNIT_VARIANT_SETTINGS, UnitVariantSetting.class)
                .headers(headers)
                .inlineVariables(validUsername);

        return (UnitVariantSetting) HTTPRequest.build(requestEntity).get().getResponseEntity();
    }

    /**
     * Gets Enabled Currency Rate Versions
     * Return is void because the mapping to CurrencyRateVersionResponse is the validation, therefore no assertions are required
     */
    public void getEnabledCurrencyRateVersions() {
        setupHeader();

        final RequestEntity requestEntity = RequestEntityUtil
                .init(AcsApiEnum.GET_ENABLED_CURRENCY_RATE_VERSIONS, CurrencyRateVersionResponse.class)
                .headers(headers);

        HTTPRequest.build(requestEntity).get();
    }

    /**
     * Gets Tolerance Policy Defaults
     *
     * @return GetTolerancePolicyDefaults instance
     */
    public GetTolerancePolicyDefaultsResponse getTolerancePolicyDefaults() {
        setupHeader();

        final RequestEntity requestEntity = RequestEntityUtil
                .init(AcsApiEnum.GET_SET_TOLERANCE_POLICY_DEFAULTS, GetTolerancePolicyDefaultsResponse.class)
                .headers(headers)
                .inlineVariables(validUsername);

        return (GetTolerancePolicyDefaultsResponse) HTTPRequest.build(requestEntity).get().getResponseEntity();
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
        setupHeader();

        final RequestEntity requestEntity = RequestEntityUtil
                .init(AcsApiEnum.GET_SET_TOLERANCE_POLICY_DEFAULTS, GenericResourceCreatedResponse.class)
                .headers(headers)
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
        setupHeader();

        final RequestEntity requestEntity = RequestEntityUtil
                .init(AcsApiEnum.GET_SET_TOLERANCE_POLICY_DEFAULTS, null)
                .headers(headers)
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
        setupHeader();

        final RequestEntity requestEntity = RequestEntityUtil
                .init(AcsApiEnum.GET_SET_PRODUCTION_DEFAULTS, GetProductionDefaultsResponse.class)
                .headers(headers)
                .inlineVariables(validUsername);

        return (GetProductionDefaultsResponse) HTTPRequest.build(requestEntity).get().getResponseEntity();
    }

    /**
     * Sets production defaults
     *
     * @return GenericResourceCreatedResponse
     */
    public GenericResourceCreatedResponse setProductionDefaults() {
        setupHeader();

        final RequestEntity requestEntity = RequestEntityUtil
                .init(AcsApiEnum.GET_SET_PRODUCTION_DEFAULTS, GenericResourceCreatedResponse.class)
                .headers(headers)
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
        setupHeader();

        final RequestEntity requestEntity = RequestEntityUtil
                .init(AcsApiEnum.GET_SET_PRODUCTION_DEFAULTS, null)
                .headers(headers)
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

    /**
     * Calls get user preferences endpoint
     *
     * @return instance of response object
     */
    public GetUserPreferencesResponse getUserPreferences() {
        setupHeader();

        final RequestEntity requestEntity = RequestEntityUtil
            .init(AcsApiEnum.GET_SET_USER_PREFERENCES, GetUserPreferencesResponse.class)
            .headers(headers)
            .inlineVariables(validUsername);

        return (GetUserPreferencesResponse) HTTPRequest.build(requestEntity).get().getResponseEntity();
    }

    /**
     * Calls get user preference by name
     *
     * @param userPrefToGet - String
     * @return String response of preference value
     */
    public String getUserPreferenceByName(String userPrefToGet) {
        setupHeader();

        final RequestEntity requestEntity = RequestEntityUtil
            .init(AcsApiEnum.GET_SET_USER_PREFERENCE_BY_NAME, null)
            .headers(headers)
            .inlineVariables(validUsername, userPrefToGet);

        return HTTPRequest.build(requestEntity).get().getBody();
    }

    /**
     * Generic call for get endpoint with invalid username
     *
     * @param endpoint - endpoint to call
     * @return String - error
     */
    public String getEndpointInvalidUsername(EndpointEnum endpoint) {
        setupHeader();

        final RequestEntity requestEntity = RequestEntityUtil
            .init(endpoint, null)
            .headers(headers)
            .inlineVariables(invalidUsername);

        return HTTPRequest.build(requestEntity).get().getBody();
    }

    /**
     * Sets up header with content type and token
     */
    private void setupHeader() {
        headers.put("Content-Type", "application/json");
        Object[] tokenArray = token.keySet().toArray();
        for (Object key : tokenArray) {
            headers.put(key.toString(), token.get(key));
        }
    }
}
