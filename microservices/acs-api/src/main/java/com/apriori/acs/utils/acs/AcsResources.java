package com.apriori.acs.utils.acs;

import com.apriori.acs.entity.enums.acs.AcsApiEnum;
import com.apriori.acs.entity.response.acs.createmissingscenario.CreateMissingScenarioInputs;
import com.apriori.acs.entity.response.acs.createmissingscenario.CreateMissingScenarioResponse;
import com.apriori.acs.entity.response.acs.genericclasses.GenericErrorResponse;
import com.apriori.acs.entity.response.acs.genericclasses.GenericResourceCreatedIdResponse;
import com.apriori.acs.entity.response.acs.genericclasses.GenericResourceCreatedResponse;
import com.apriori.acs.entity.response.acs.getactiveaxesbyscenarioiterationkey.GetActiveAxesByScenarioIterationKeyResponse;
import com.apriori.acs.entity.response.acs.getactivedimensionsbyscenarioiterationkey.GetActiveDimensionsResponse;
import com.apriori.acs.entity.response.acs.getartifactproperties.GetArtifactPropertiesResponse;
import com.apriori.acs.entity.response.acs.getartifacttableinfo.GetArtifactTableInfoResponse;
import com.apriori.acs.entity.response.acs.getenabledcurrencyrateversions.CurrencyRateVersionResponse;
import com.apriori.acs.entity.response.acs.getgcdmapping.GetGcdMappingResponse;
import com.apriori.acs.entity.response.acs.getpartprimaryprocessgroups.GetPartPrimaryProcessGroupsResponse;
import com.apriori.acs.entity.response.acs.getscenarioinfobyscenarioiterationkey.GetScenarioInfoByScenarioIterationKeyResponse;
import com.apriori.acs.entity.response.acs.getscenariosinfo.GetScenariosInfoResponse;
import com.apriori.acs.entity.response.acs.getscenariosinfo.ScenarioIterationKeysInputs;
import com.apriori.acs.entity.response.acs.getsetdisplayunits.GetDisplayUnitsResponse;
import com.apriori.acs.entity.response.acs.getsetdisplayunits.SetDisplayUnitsInputs;
import com.apriori.acs.entity.response.acs.getsetproductiondefaults.GetProductionDefaultsResponse;
import com.apriori.acs.entity.response.acs.getsetproductiondefaults.SetProductionDefaultsInputs;
import com.apriori.acs.entity.response.acs.getsetproductioninfo.GetProductionInfoResponse;
import com.apriori.acs.entity.response.acs.getsettolerancepolicydefaults.GetTolerancePolicyDefaultsResponse;
import com.apriori.acs.entity.response.acs.getsettolerancepolicydefaults.SetTolerancePolicyDefaultsInputs;
import com.apriori.acs.entity.response.acs.getsetuserpreferences.GetUserPreferencesResponse;
import com.apriori.acs.entity.response.acs.getsetuserpreferences.SetUserPreferencesInputs;
import com.apriori.acs.entity.response.acs.getunitvariantsettings.GetUnitVariantSettingsResponse;
import com.apriori.acs.entity.response.acs.getunitvariantsettings.UnitVariantSetting;
import com.apriori.acs.entity.response.acs.getavailableroutings.GetAvailableRoutingsResponse;
import com.apriori.acs.entity.response.acs.saveroutingselection.SaveRoutingSelectionInputs;
import com.apriori.acs.entity.response.workorders.genericclasses.ScenarioIterationKey;
import com.apriori.acs.utils.Constants;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.authorization.OldAuthorizationUtil;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.enums.EndpointEnum;
import com.apriori.utils.http.utils.QueryParams;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.user.UserUtil;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
public class AcsResources {

    private static final String token = new OldAuthorizationUtil().getTokenAsString();

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

        final RequestEntity requestEntity = RequestEntityUtil
            .init(AcsApiEnum.CREATE_MISSING_SCENARIO, CreateMissingScenarioResponse.class)
            .headers(headers)
            .body(CreateMissingScenarioInputs.builder()
                    .baseName(Constants.PART_FILE_NAME)
                    .configurationName(Constants.PART_CONFIG_NAME)
                    .modelName(Constants.PART_MODEL_NAME)
                    .scenarioName(new GenerateStringUtil().generateScenarioName())
                    .scenarioType(Constants.PART_COMPONENT_TYPE)
                    .missing(true)
                    .publicItem(true)
                    .createdBy(validUsername)
                    .userId(validUsername)
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
            .init(AcsApiEnum.SCENARIO_INFO_BY_SCENARIO_ITERATION_KEY, GetScenarioInfoByScenarioIterationKeyResponse.class)
            .headers(headers)
            .inlineVariables(
                scenarioIterationKey.getScenarioKey().getWorkspaceId().toString(),
                scenarioIterationKey.getScenarioKey().getTypeName(),
                scenarioIterationKey.getScenarioKey().getMasterName(),
                scenarioIterationKey.getScenarioKey().getStateName(),
                scenarioIterationKey.getIteration().toString()
            );

        return (GetScenarioInfoByScenarioIterationKeyResponse) HTTPRequest
                .build(requestEntity).get().getResponseEntity();
    }

    /**
     * Get Scenario Information by Scenario Iteration Key
     * Negative version for use after scenario deletion to confirm deletion worked
     *
     * @param scenarioIterationKey - ScenarioIterationKey to use in API request
     * @return String of 404 and error message
     */
    public GenericErrorResponse getScenarioInfoByScenarioIterationKeyNegative(ScenarioIterationKey scenarioIterationKey) {
        setupHeader();

        final RequestEntity requestEntity = RequestEntityUtil
            .init(AcsApiEnum.SCENARIO_INFO_BY_SCENARIO_ITERATION_KEY, GenericErrorResponse.class)
            .headers(headers)
            .inlineVariables(
                scenarioIterationKey.getScenarioKey().getWorkspaceId().toString(),
                scenarioIterationKey.getScenarioKey().getTypeName(),
                scenarioIterationKey.getScenarioKey().getMasterName(),
                scenarioIterationKey.getScenarioKey().getStateName(),
                scenarioIterationKey.getIteration().toString()
            );

        return (GenericErrorResponse) HTTPRequest.build(requestEntity).get().getResponseEntity();
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

        final RequestEntity requestEntity = RequestEntityUtil
            .init(AcsApiEnum.SCENARIOS_INFORMATION, GetScenariosInfoResponse.class)
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
            .init(AcsApiEnum.SCENARIOS_INFORMATION, GetScenariosInfoResponse.class)
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
     * @return ResponseWrapper of type GetScenariosInfoResponse instance
     */
    public ResponseWrapper<GetScenariosInfoResponse> getScenariosInfoNullBody() {
        setupHeader();

        final RequestEntity requestEntity = RequestEntityUtil
            .init(AcsApiEnum.SCENARIOS_INFORMATION, null)
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

        final RequestEntity requestEntity = RequestEntityUtil
            .init(AcsApiEnum.DISPLAY_UNITS, GetDisplayUnitsResponse.class)
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

        final RequestEntity requestEntity = RequestEntityUtil
            .init(AcsApiEnum.DISPLAY_UNITS, GenericResourceCreatedResponse.class)
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
            .init(AcsApiEnum.UNIT_VARIANT_SETTINGS, GetUnitVariantSettingsResponse.class)
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
            .init(AcsApiEnum.CUSTOM_UNIT_VARIANT_SETTINGS, UnitVariantSetting.class)
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
            .init(AcsApiEnum.ENABLED_CURRENCY_RATE_VERSIONS, CurrencyRateVersionResponse.class)
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
            .init(AcsApiEnum.TOLERANCE_POLICY_DEFAULTS, GetTolerancePolicyDefaultsResponse.class)
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
     * @return GenericResourceCreatedResponse instance
     */
    public GenericResourceCreatedResponse setTolerancePolicyDefaults(double totalRunoutOverride,
                                                                     String toleranceMode,
                                                                     boolean useCadToleranceThreshhold) {
        setupHeader();

        final RequestEntity requestEntity = RequestEntityUtil
            .init(AcsApiEnum.TOLERANCE_POLICY_DEFAULTS, GenericResourceCreatedResponse.class)
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
     * @return instance of GenericErrorResponse
     */
    public GenericErrorResponse setTolerancePolicyDefaultsInvalidUsername() {
        setupHeader();

        final RequestEntity requestEntity = RequestEntityUtil
            .init(AcsApiEnum.TOLERANCE_POLICY_DEFAULTS, GenericErrorResponse.class)
            .headers(headers)
            .body(null)
            .inlineVariables(invalidUsername);

        return (GenericErrorResponse) HTTPRequest.build(requestEntity).post().getResponseEntity();
    }

    /**
     * Gets Production Defaults
     *
     * @return GetProductionDefaultsResponse instance - response from API
     */
    public GetProductionDefaultsResponse getProductionDefaults() {
        setupHeader();

        final RequestEntity requestEntity = RequestEntityUtil
            .init(AcsApiEnum.PRODUCTION_DEFAULTS, GetProductionDefaultsResponse.class)
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
            .init(AcsApiEnum.PRODUCTION_DEFAULTS, GenericResourceCreatedResponse.class)
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
     * @return instance of GenericErrorResponse
     */
    public GenericErrorResponse setProductionDefaultsInvalidUsername() {
        setupHeader();

        final RequestEntity requestEntity = RequestEntityUtil
            .init(AcsApiEnum.PRODUCTION_DEFAULTS, GenericErrorResponse.class)
            .headers(headers)
            .body(null)
            .inlineVariables(invalidUsername);

        return (GenericErrorResponse) HTTPRequest.build(requestEntity).post().getResponseEntity();
    }

    /**
     * Calls get user preferences endpoint
     *
     * @return instance of response object
     */
    public GetUserPreferencesResponse getUserPreferences() {
        setupHeader();

        final RequestEntity requestEntity = RequestEntityUtil
            .init(AcsApiEnum.USER_PREFERENCES, GetUserPreferencesResponse.class)
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
            .init(AcsApiEnum.USER_PREFERENCE_BY_NAME, null)
            .headers(headers)
            .inlineVariables(validUsername, userPrefToGet);

        return HTTPRequest.build(requestEntity).get().getBody();
    }

    /**
     * Gets user preferences with invalid username
     *
     * @return instance of GenericErrorResponse
     */
    public GenericErrorResponse getUserPreferenceByNameInvalidUser() {
        setupHeader();

        final RequestEntity requestEntity = RequestEntityUtil
            .init(AcsApiEnum.USER_PREFERENCE_BY_NAME, GenericErrorResponse.class)
            .headers(headers)
            .inlineVariables(invalidUsername, "TolerancePolicyDefaults.toleranceMode");

        return (GenericErrorResponse) HTTPRequest.build(requestEntity).get().getResponseEntity();
    }

    /**
     * Generic call for get endpoint with invalid username
     *
     * @param endpoint - endpoint to call
     * @return instance of GenericErrorResponse
     */
    public GenericErrorResponse getEndpointInvalidUsername(EndpointEnum endpoint) {
        setupHeader();

        final RequestEntity requestEntity = RequestEntityUtil
            .init(endpoint, GenericErrorResponse.class)
            .headers(headers)
            .inlineVariables(invalidUsername);

        return (GenericErrorResponse) HTTPRequest.build(requestEntity).get().getResponseEntity();
    }

    /**
     * Gets production info and returns the response
     *
     * @param scenarioIterationKey - Scenario Iteration Key to use
     * @return GetProductionInfoResponse instance
     */
    public GetProductionInfoResponse getProductionInfo(ScenarioIterationKey scenarioIterationKey) {
        setupHeader();

        final RequestEntity requestEntity = RequestEntityUtil
            .init(AcsApiEnum.PRODUCTION_INFO, GetProductionInfoResponse.class)
            .headers(headers)
            .queryParams(new QueryParams().use("applyEdits", "true"))
            .inlineVariables(
                scenarioIterationKey.getScenarioKey().getWorkspaceId().toString(),
                scenarioIterationKey.getScenarioKey().getTypeName(),
                scenarioIterationKey.getScenarioKey().getMasterName(),
                scenarioIterationKey.getScenarioKey().getStateName(),
                scenarioIterationKey.getIteration().toString()
            );

        return (GetProductionInfoResponse) HTTPRequest.build(requestEntity).get().getResponseEntity();
    }

    /**
     * Set user preferences
     *
     * @param costTableDecimalPlaces - String - value to set
     * @param useVpe - String - value to set
     * @param toleranceMode - String - value to set
     * @return GenericResourceCreatedResponse instance
     */
    public GenericResourceCreatedResponse setUserPreferences(String costTableDecimalPlaces, String useVpe, String toleranceMode) {
        setupHeader();

        final RequestEntity requestEntity = RequestEntityUtil
            .init(AcsApiEnum.USER_PREFERENCES, GenericResourceCreatedResponse.class)
            .headers(headers)
            .body(SetUserPreferencesInputs.builder()
                .costTableDecimalPlaces(costTableDecimalPlaces)
                .prodInfoDefaultUseVpeForAllProcesses(useVpe)
                .tolerancePolicyDefaultsToleranceMode(toleranceMode)
                .build())
            .inlineVariables(validUsername);

        return (GenericResourceCreatedResponse) HTTPRequest.build(requestEntity).post().getResponseEntity();
    }

    /**
     * Set user preferences with invalid user
     *
     * @return String - 400 error response body
     */
    public GenericErrorResponse setUserPreferencesInvalidUser() {
        setupHeader();

        final RequestEntity requestEntity = RequestEntityUtil
            .init(AcsApiEnum.USER_PREFERENCES, GenericErrorResponse.class)
            .headers(headers)
            .body(null)
            .inlineVariables(invalidUsername);

        return (GenericErrorResponse) HTTPRequest.build(requestEntity).post().getResponseEntity();
    }

    /**
     * Sets user preference by name
     *
     * @param prefToSetKey - String - key of preference to set
     * @param prefToSetValue - String - value of preference to set
     * @return GenericResourceCreatedResponse instance
     */
    public GenericResourceCreatedResponse setUserPreferenceByName(String prefToSetKey, String prefToSetValue) {
        setupHeader();

        final RequestEntity requestEntity = RequestEntityUtil
            .init(AcsApiEnum.USER_PREFERENCE_BY_NAME, GenericResourceCreatedResponse.class)
            .headers(headers)
            .body(prefToSetValue)
            .inlineVariables(validUsername, prefToSetKey);

        return (GenericResourceCreatedResponse) HTTPRequest.build(requestEntity).post().getResponseEntity();
    }

    /**
     * Set production info
     *
     * @param getProductionInfoResponse - for use in body of request
     * @param scenarioIterationKey - scenario to set production info for
     * @return GenericResourceCreatedIdResponse
     */
    public GenericResourceCreatedIdResponse setProductionInfo(GetProductionInfoResponse getProductionInfoResponse,
                                                              ScenarioIterationKey scenarioIterationKey) {
        setupHeader();

        final RequestEntity requestEntity = RequestEntityUtil
            .init(AcsApiEnum.PRODUCTION_INFO, GenericResourceCreatedIdResponse.class)
            .headers(headers)
            .body(getProductionInfoResponse)
            .inlineVariables(
                scenarioIterationKey.getScenarioKey().getWorkspaceId().toString(),
                scenarioIterationKey.getScenarioKey().getTypeName(),
                scenarioIterationKey.getScenarioKey().getMasterName(),
                scenarioIterationKey.getScenarioKey().getStateName(),
                scenarioIterationKey.getIteration().toString()
            );

        return (GenericResourceCreatedIdResponse) HTTPRequest.build(requestEntity).post().getResponseEntity();
    }

    /**
     * Get Available Routings
     *
     * @param scenarioIterationKey - details of scenario to use (ScenarioIterationKey)
     * @return GetAvailableRoutingsResponse instance
     */

    public GetAvailableRoutingsResponse getAvailableRoutings(ScenarioIterationKey scenarioIterationKey, String vpeName, String processGroupName) {
        setupHeader();

        final RequestEntity requestEntity = RequestEntityUtil
                .init(AcsApiEnum.AVAILABLE_ROUTINGS, GetAvailableRoutingsResponse.class)
                .headers(headers)
                .inlineVariables(
                        scenarioIterationKey.getScenarioKey().getWorkspaceId().toString(),
                        scenarioIterationKey.getScenarioKey().getTypeName(),
                        scenarioIterationKey.getScenarioKey().getMasterName(),
                        scenarioIterationKey.getScenarioKey().getStateName(),
                        scenarioIterationKey.getIteration().toString(),
                        vpeName,
                        processGroupName
                );
        return (GetAvailableRoutingsResponse) HTTPRequest.build(requestEntity).get().getResponseEntity();
    }

    /**
     * Save routing selection
     *
     * @param scenarioIterationKey - details of scenario to use (ScenarioIterationKey)
     * @return GenericResourceCreatedIdResponse instance
     */
    public GenericResourceCreatedIdResponse saveRoutingSelection(ScenarioIterationKey scenarioIterationKey) {
        setupHeader();

        List<SaveRoutingSelectionInputs> childrenList = new ArrayList<>();
        childrenList.add(SaveRoutingSelectionInputs.builder()
            .name("Sheet Metal")
            .plantName("aPriori USA")
            .processGroupName("Sheet Metal")
            .alternNode(true)
            .build()
        );

        final RequestEntity requestEntity = RequestEntityUtil
            .init(AcsApiEnum.ROUTING_SELECTION, GenericResourceCreatedIdResponse.class)
            .headers(headers)
            .body(SaveRoutingSelectionInputs.builder()
                .name("Sheet Metal/Machining")
                .plantName("aPriori USA")
                .processGroupName("Sheet Metal")
                .children(childrenList)
                .alternNode(false)
                .build())
            .inlineVariables(
                scenarioIterationKey.getScenarioKey().getWorkspaceId().toString(),
                scenarioIterationKey.getScenarioKey().getTypeName(),
                scenarioIterationKey.getScenarioKey().getMasterName(),
                scenarioIterationKey.getScenarioKey().getStateName(),
                scenarioIterationKey.getIteration().toString()
            );

        return (GenericResourceCreatedIdResponse) HTTPRequest.build(requestEntity).post().getResponseEntity();
    }

    /**
     * Get part primary process groups
     *
     * @return GetPartPrimaryProcessGroupsResponse instance
     */
    public GetPartPrimaryProcessGroupsResponse getPartPrimaryProcessGroups() {
        setupHeader();

        final RequestEntity requestEntity = RequestEntityUtil
            .init(AcsApiEnum.PART_PRIMARY_PROCESS_GROUPS, GetPartPrimaryProcessGroupsResponse.class)
            .headers(headers);

        return (GetPartPrimaryProcessGroupsResponse) HTTPRequest.build(requestEntity).get().getResponseEntity();
    }

    /**
     * Gets 2D image of costed part by scenario iteration key
     *
     * @param scenarioIterationKey - values for url
     * @return String - base64 encoded image
     */
    public String get2DImageByScenarioIterationKey(ScenarioIterationKey scenarioIterationKey) {
        setupHeader();

        final RequestEntity requestEntity = RequestEntityUtil
            .init(AcsApiEnum.TWO_DIMENSIONAL_IMAGE, null)
            .headers(headers)
            .inlineVariables(
                scenarioIterationKey.getScenarioKey().getWorkspaceId().toString(),
                scenarioIterationKey.getScenarioKey().getTypeName(),
                scenarioIterationKey.getScenarioKey().getMasterName(),
                scenarioIterationKey.getScenarioKey().getStateName(),
                scenarioIterationKey.getIteration().toString()
            );

        return HTTPRequest.build(requestEntity).get().getBody();
    }

    /**
     * Gets image by scenario iteration key
     *
     * @param scenarioIterationKey - values to input into url
     * @param getWebImage - flag to call desktop or web image endpoint (url is only different, so this reduces duplication)
     * @return String - Base64 image
     */
    public String getImageByScenarioIterationKey(ScenarioIterationKey scenarioIterationKey, boolean getWebImage) {
        setupHeader();

        AcsApiEnum getImageUrl = getWebImage ? AcsApiEnum.WEB_IMAGE_BY_SCENARIO_ITERATION_KEY :
            AcsApiEnum.DESKTOP_IMAGE_BY_SCENARIO_ITERATION_KEY;

        final RequestEntity requestEntity = RequestEntityUtil
            .init(getImageUrl, null)
            .headers(headers)
            .inlineVariables(
                scenarioIterationKey.getScenarioKey().getWorkspaceId().toString(),
                scenarioIterationKey.getScenarioKey().getTypeName(),
                scenarioIterationKey.getScenarioKey().getMasterName(),
                scenarioIterationKey.getScenarioKey().getStateName(),
                scenarioIterationKey.getIteration().toString()
            );

        return HTTPRequest.build(requestEntity).get().getBody();
    }

    /**
     * Gets Active Dimensions by Scenario Iteration Key
     *
     * @param paramsForUrl - list of values to input into url
     * @return GetActiveDimensionsResponse instance
     */
    public GetActiveDimensionsResponse getActiveDimensionsByScenarioIterationKeyEndpoint(List<String> paramsForUrl) {
        setupHeader();

        final RequestEntity requestEntity = RequestEntityUtil
            .init(AcsApiEnum.ACTIVE_DIMENSIONS, GetActiveDimensionsResponse.class)
            .headers(headers)
            .inlineVariables(
                paramsForUrl.get(0),
                paramsForUrl.get(1),
                paramsForUrl.get(2),
                paramsForUrl.get(3),
                paramsForUrl.get(4)
            );

        return (GetActiveDimensionsResponse) HTTPRequest.build(requestEntity).get().getResponseEntity();
    }

    /**
     * Gets Active Axes by Scenario Iteration Key
     *
     * @param paramsForUrl - list of values to input into url
     * @return GetActiveAxesByScenarioIterationKeyResponse instance
     */
    public GetActiveAxesByScenarioIterationKeyResponse getActiveAxesByScenarioIterationKey(List<String> paramsForUrl) {
        setupHeader();

        final RequestEntity requestEntity = RequestEntityUtil
            .init(AcsApiEnum.ACTIVE_AXES, GetActiveAxesByScenarioIterationKeyResponse.class)
            .headers(headers)
            .inlineVariables(
                paramsForUrl.get(0),
                paramsForUrl.get(1),
                paramsForUrl.get(2),
                paramsForUrl.get(3),
                paramsForUrl.get(4)
            );

        return (GetActiveAxesByScenarioIterationKeyResponse) HTTPRequest.build(requestEntity).get().getResponseEntity();
    }

    /**
     * Gets Artifact Table Info
     *
     * @return Instance of GetArtifactTableInfoResponse
     */
    public GetArtifactTableInfoResponse getArtifactTableInfo() {
        setupHeader();

        final RequestEntity requestEntity = RequestEntityUtil
            .init(AcsApiEnum.ARTIFACT_TABLE_INFO, GetArtifactTableInfoResponse.class)
            .headers(headers)
            .inlineVariables(
                "Sheet Metal",
                "SimpleHole"
            );

        return (GetArtifactTableInfoResponse) HTTPRequest.build(requestEntity).get().getResponseEntity();
    }

    /**
     * Negative version of Get Artifact Table Info - invalid process group
     *
     * @return String of body, containing 404 status code and error
     */
    public GenericErrorResponse getArtifactTableInfoInvalidProcessGroup() {
        setupHeader();

        final RequestEntity requestEntity = RequestEntityUtil
            .init(AcsApiEnum.ARTIFACT_TABLE_INFO, GenericErrorResponse.class)
            .headers(headers)
            .inlineVariables(
                "Sheet Metals",
                "SimpleHole"
            );

        return (GenericErrorResponse) HTTPRequest.build(requestEntity).get().getResponseEntity();
    }

    /**
     * Get GCD Mapping by Scenario Iteration Key
     *
     * @param scenarioIterationKey - ScenarioIterationKey instance
     * @return GetGcdMappingResponse instance
     */
    public GetGcdMappingResponse getGcdMapping(ScenarioIterationKey scenarioIterationKey) {
        setupHeader();

        final RequestEntity requestEntity = RequestEntityUtil
            .init(AcsApiEnum.GCD_IMAGE_MAPPING, GetGcdMappingResponse.class)
            .headers(headers)
            .inlineVariables(
                scenarioIterationKey.getScenarioKey().getWorkspaceId().toString(),
                scenarioIterationKey.getScenarioKey().getTypeName(),
                scenarioIterationKey.getScenarioKey().getMasterName(),
                scenarioIterationKey.getScenarioKey().getStateName(),
                scenarioIterationKey.getIteration().toString()
            );

        return (GetGcdMappingResponse) HTTPRequest.build(requestEntity).get().getResponseEntity();
    }

    /**
     * Gets Artifact Properties
     *
     * @param scenarioIterationKey - ScenarioIterationKey to use in url
     * @param getGcdMappingResponse - GetGcdMappingResponse to use in body
     * @return GetArtifactPropertiesResponse instance
     */
    public GetArtifactPropertiesResponse getArtifactProperties(ScenarioIterationKey scenarioIterationKey, GetGcdMappingResponse getGcdMappingResponse) {
        setupHeader();

        String displayNameOne = getGcdMappingResponse.getDrawableNodesByArtifactKeyEntries().get(0).getKey().getDisplayName();
        String displayNameTwo = getGcdMappingResponse.getDrawableNodesByArtifactKeyEntries().get(1).getKey().getDisplayName();

        final RequestEntity requestEntity = RequestEntityUtil
            .init(AcsApiEnum.ARTIFACT_PROPERTIES, GetArtifactPropertiesResponse.class)
            .headers(headers)
            .customBody(
                String.format(
                    "[\"%s\", \"%s\"]",
                    displayNameOne,
                    displayNameTwo
                ))
            .inlineVariables(
                scenarioIterationKey.getScenarioKey().getWorkspaceId().toString(),
                scenarioIterationKey.getScenarioKey().getTypeName(),
                scenarioIterationKey.getScenarioKey().getMasterName(),
                scenarioIterationKey.getScenarioKey().getStateName(),
                scenarioIterationKey.getIteration().toString(),
                displayNameOne.split(":")[0]
            );

        return (GetArtifactPropertiesResponse) HTTPRequest.build(requestEntity).post().getResponseEntity();
    }

    /**
     * Sets up header with content type and token
     */
    private void setupHeader() {
        String defaultString = "default";
        headers.put("Content-Type", "application/json");
        headers.put("Accept", "*/*");
        headers.put("apriori.tenantgroup", defaultString);
        headers.put("apriori.tenant", defaultString);
        headers.put("Authorization", "Bearer " + token);
    }
}
