package com.apriori.acs.api.utils.acs;

import com.apriori.acs.api.enums.acs.AcsApiEnum;
import com.apriori.acs.api.models.request.workorders.NewPartRequest;
import com.apriori.acs.api.models.response.acs.GcdProperties.GcdPropertiesGroupItemsInputs;
import com.apriori.acs.api.models.response.acs.GcdProperties.GcdPropertiesInputs;
import com.apriori.acs.api.models.response.acs.GcdProperties.GcdPropertiesResponse;
import com.apriori.acs.api.models.response.acs.GcdProperties.PropertiesToSet;
import com.apriori.acs.api.models.response.acs.activeaxesbyscenarioiterationkey.ActiveAxesByScenarioIterationKeyResponse;
import com.apriori.acs.api.models.response.acs.activedimensionsbyscenarioiterationkey.ActiveDimensionsResponse;
import com.apriori.acs.api.models.response.acs.allmaterialstocksinfo.AllMaterialStocksInfoResponse;
import com.apriori.acs.api.models.response.acs.artifactproperties.ArtifactPropertiesResponse;
import com.apriori.acs.api.models.response.acs.artifacttableinfo.ArtifactTableInfoResponse;
import com.apriori.acs.api.models.response.acs.availableroutings.AvailableRoutingsFirstLevel;
import com.apriori.acs.api.models.response.acs.designGuidance.DesignGuidanceResponse;
import com.apriori.acs.api.models.response.acs.displayunits.DisplayUnitsInputs;
import com.apriori.acs.api.models.response.acs.displayunits.DisplayUnitsResponse;
import com.apriori.acs.api.models.response.acs.enabledcurrencyrateversions.CurrencyRateVersionResponse;
import com.apriori.acs.api.models.response.acs.gcdmapping.GcdMappingResponse;
import com.apriori.acs.api.models.response.acs.genericclasses.GenericErrorResponse;
import com.apriori.acs.api.models.response.acs.genericclasses.GenericResourceCreatedIdResponse;
import com.apriori.acs.api.models.response.acs.genericclasses.GenericResourceCreatedResponse;
import com.apriori.acs.api.models.response.acs.missingscenario.MissingScenarioInputs;
import com.apriori.acs.api.models.response.acs.missingscenario.MissingScenarioResponse;
import com.apriori.acs.api.models.response.acs.partprimaryprocessgroups.PartPrimaryProcessGroupsResponse;
import com.apriori.acs.api.models.response.acs.productiondefaults.ProductionDefaultsInputs;
import com.apriori.acs.api.models.response.acs.productiondefaults.ProductionDefaultsResponse;
import com.apriori.acs.api.models.response.acs.productioninfo.ProductionInfoResponse;
import com.apriori.acs.api.models.response.acs.routingselection.RoutingSelectionInputs;
import com.apriori.acs.api.models.response.acs.scenarioinfobyscenarioiterationkey.ScenarioInfoByScenarioIterationKeyResponse;
import com.apriori.acs.api.models.response.acs.scenariosinfo.ScenarioIterationKeysInputs;
import com.apriori.acs.api.models.response.acs.scenariosinfo.ScenariosInfoResponse;
import com.apriori.acs.api.models.response.acs.tolerancepolicydefaults.TolerancePolicyDefaultsInputs;
import com.apriori.acs.api.models.response.acs.tolerancepolicydefaults.TolerancePolicyDefaultsResponse;
import com.apriori.acs.api.models.response.acs.unitvariantsettings.UnitVariantSetting;
import com.apriori.acs.api.models.response.acs.unitvariantsettings.UnitVariantSettingsResponse;
import com.apriori.acs.api.models.response.acs.userpreferences.UserPreferencesInputs;
import com.apriori.acs.api.models.response.acs.userpreferences.UserPreferencesResponse;
import com.apriori.acs.api.models.response.workorders.cost.costworkorderstatus.CostOrderStatusOutputs;
import com.apriori.acs.api.models.response.workorders.genericclasses.ScenarioIterationKey;
import com.apriori.acs.api.models.response.workorders.upload.FileUploadOutputs;
import com.apriori.acs.api.utils.Constants;
import com.apriori.acs.api.utils.OldAuthorizationUtil;
import com.apriori.acs.api.utils.workorders.FileUploadResources;
import com.apriori.fms.api.models.response.FileResponse;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.http.utils.QueryParams;
import com.apriori.shared.util.http.utils.RequestEntityUtil_Old;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.interfaces.EndpointEnum;

import com.google.common.net.UrlEscapers;
import io.restassured.http.Headers;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
public class AcsResources {
    private static String token = "";

    private static final HashMap<String, String> headers = new HashMap<>();

    private final String validUsername;
    private final String invalidUsername;

    public AcsResources() {
        UserCredentials user = UserUtil.getUser("common");
        token = new OldAuthorizationUtil().getTokenAsString();
        validUsername = user.getEmail().split("@")[0];
        invalidUsername = user.getUsername().split("@")[0].concat("41");
    }

    /**
     * Creates Missing Scenario
     *
     * @return CreateMissingScenarioResponse response instance
     */
    public MissingScenarioResponse createMissingScenario() {
        setupHeader();

        final RequestEntity requestEntity = RequestEntityUtil_Old
            .init(AcsApiEnum.CREATE_MISSING_SCENARIO, MissingScenarioResponse.class)
            .headers(headers)
            .body(MissingScenarioInputs.builder()
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

        return (MissingScenarioResponse) HTTPRequest.build(requestEntity).post().getResponseEntity();
    }

    /**
     * Gets All Material Stocks Info
     *
     * @param vpeName      - String
     * @param processGroup - String
     * @param materialName - String
     * @return instance of AllMaterialSocksInfoResponse
     */
    public AllMaterialStocksInfoResponse getAllMaterialStocksInfo(String vpeName, String processGroup, String materialName) {
        setupHeader();
        // TODO z: fix it threads
        final RequestEntity requestEntity = new RequestEntity()
            .endpoint(AcsApiEnum.ALL_MATERIAL_STOCKS_INFO)
            .returnType(AllMaterialStocksInfoResponse.class)
            .headers(headers)
            .inlineVariables(
                vpeName,
                processGroup)
            .queryParams(new QueryParams().use("materialName", materialName))
            .expectedResponseCode(HttpStatus.SC_OK);

        return (AllMaterialStocksInfoResponse) HTTPRequest.build(requestEntity).get().getResponseEntity();
    }

    /**
     * Gets All Material Stocks Info
     *
     * @param vpeName      - String
     * @param processGroup - String
     * @return instance of AllMaterialSocksInfoResponse
     */
    public <T> ResponseWrapper<T> getMaterialsInfo(String vpeName, String processGroup, Class<T> klass) {
        setupHeader();

        final RequestEntity requestEntity = RequestEntityUtil_Old
            .init(AcsApiEnum.MATERIALS_INFO, klass)
            .headers(headers)
            .inlineVariables(
                vpeName,
                processGroup)
            .expectedResponseCode(HttpStatus.SC_OK);

        return HTTPRequest.build(requestEntity).get();
    }

    /**
     * Gets All Material Stocks Info
     *
     * @param scenarioIterationKey      - scenario to get Design Guidance for
     * @return instance of DesignGuidanceResponse
     */
    public DesignGuidanceResponse getDesignGuidance(ScenarioIterationKey scenarioIterationKey, String guidanceTopics) {
        setupHeader();

        final RequestEntity requestEntity = RequestEntityUtil_Old
            .init(AcsApiEnum.DESIGN_GUIDANCE, DesignGuidanceResponse.class)
            .headers(headers)
            .inlineVariables(
                scenarioIterationKey.getScenarioKey().getWorkspaceId().toString(),
                scenarioIterationKey.getScenarioKey().getTypeName(),
                scenarioIterationKey.getScenarioKey().getMasterName(),
                scenarioIterationKey.getScenarioKey().getStateName(),
                scenarioIterationKey.getIteration().toString())
            .queryParams(new QueryParams().use("guidanceTopics", guidanceTopics));

        return (DesignGuidanceResponse) HTTPRequest.build(requestEntity).get().getResponseEntity();
    }

    /**
     * Gets Scenario Information by Scenario Iteration Key
     *
     * @return GetScenarioInfoByScenarioIterationKeyResponse instance
     */
    public ScenarioInfoByScenarioIterationKeyResponse getScenarioInfoByScenarioIterationKey(ScenarioIterationKey scenarioIterationKey) {
        setupHeader();

        final RequestEntity requestEntity = RequestEntityUtil_Old
            .init(AcsApiEnum.SCENARIO_INFO_BY_SCENARIO_ITERATION_KEY, ScenarioInfoByScenarioIterationKeyResponse.class)
            .headers(headers)
            .inlineVariables(
                scenarioIterationKey.getScenarioKey().getWorkspaceId().toString(),
                scenarioIterationKey.getScenarioKey().getTypeName(),
                scenarioIterationKey.getScenarioKey().getMasterName(),
                scenarioIterationKey.getScenarioKey().getStateName(),
                scenarioIterationKey.getIteration().toString()
            );

        return (ScenarioInfoByScenarioIterationKeyResponse) HTTPRequest
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

        final RequestEntity requestEntity = RequestEntityUtil_Old
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
    public ResponseWrapper<ScenariosInfoResponse> getScenariosInformation(
        ScenarioIterationKey scenarioIterationKeyOne,
        ScenarioIterationKey scenarioIterationKeyTwo) {
        setupHeader();

        List<ScenarioIterationKey> listOfKeys = new ArrayList<>();
        listOfKeys.add(scenarioIterationKeyOne);
        listOfKeys.add(scenarioIterationKeyTwo);

        final RequestEntity requestEntity = RequestEntityUtil_Old
            .init(AcsApiEnum.SCENARIOS_INFORMATION, ScenariosInfoResponse.class)
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
    public ResponseWrapper<ScenariosInfoResponse> getScenariosInformationOneScenario(List<ScenarioIterationKey> scenarioIterationKeys) {
        setupHeader();

        final RequestEntity requestEntity = RequestEntityUtil_Old
            .init(AcsApiEnum.SCENARIOS_INFORMATION, ScenariosInfoResponse.class)
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
    public ResponseWrapper<ScenariosInfoResponse> getScenariosInfoNullBody() {
        setupHeader();

        final RequestEntity requestEntity = RequestEntityUtil_Old
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
    public DisplayUnitsResponse getDisplayUnits() {
        setupHeader();

        final RequestEntity requestEntity = RequestEntityUtil_Old
            .init(AcsApiEnum.DISPLAY_UNITS, DisplayUnitsResponse.class)
            .headers(headers)
            .inlineVariables(validUsername);

        return (DisplayUnitsResponse) HTTPRequest.build(requestEntity).get().getResponseEntity();
    }

    /**
     * Sets Display Units
     *
     * @param setDisplayUnitsInputs - inputs for body of request
     * @return Set Display Units response instance
     */
    public GenericResourceCreatedResponse setDisplayUnits(DisplayUnitsInputs setDisplayUnitsInputs) {
        setupHeader();

        final RequestEntity requestEntity = RequestEntityUtil_Old
            .init(AcsApiEnum.DISPLAY_UNITS, GenericResourceCreatedResponse.class)
            .headers(headers)
            .body(setDisplayUnitsInputs)
            .inlineVariables(validUsername);

        return (GenericResourceCreatedResponse) HTTPRequest.build(requestEntity).post().getResponseEntity();
    }

    /**
     * Gets Unit Variant Settings
     *
     * @return UnitVariantSettingsResponse instance
     */
    public UnitVariantSettingsResponse getUnitVariantSettings() {
        setupHeader();

        final RequestEntity requestEntity = RequestEntityUtil_Old
            .init(AcsApiEnum.UNIT_VARIANT_SETTINGS, UnitVariantSettingsResponse.class)
            .headers(headers);

        return (UnitVariantSettingsResponse) HTTPRequest.build(requestEntity).get().getResponseEntity();
    }

    /**
     * Gets Custom Unit Variant Settings
     *
     * @return UnitVariantSettingsResponse instance
     */
    public UnitVariantSetting getCustomUnitVariantSettings() {
        setupHeader();

        final RequestEntity requestEntity = RequestEntityUtil_Old
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

        final RequestEntity requestEntity = RequestEntityUtil_Old
            .init(AcsApiEnum.ENABLED_CURRENCY_RATE_VERSIONS, CurrencyRateVersionResponse.class)
            .headers(headers);

        HTTPRequest.build(requestEntity).get();
    }

    /**
     * Gets Tolerance Policy Defaults
     *
     * @return GetTolerancePolicyDefaults instance
     */
    public TolerancePolicyDefaultsResponse getTolerancePolicyDefaults() {
        setupHeader();

        final RequestEntity requestEntity = RequestEntityUtil_Old
            .init(AcsApiEnum.TOLERANCE_POLICY_DEFAULTS, TolerancePolicyDefaultsResponse.class)
            .headers(headers)
            .inlineVariables(validUsername);

        return (TolerancePolicyDefaultsResponse) HTTPRequest.build(requestEntity).get().getResponseEntity();
    }

    /**
     * Sets Tolerance Policy Defaults Values
     *
     * @param totalRunoutOverride       - double
     * @param toleranceMode             - String
     * @param useCadToleranceThreshhold - boolean
     * @return GenericResourceCreatedResponse instance
     */
    public GenericResourceCreatedResponse setTolerancePolicyDefaults(double totalRunoutOverride,
                                                                     String toleranceMode,
                                                                     boolean useCadToleranceThreshhold) {
        setupHeader();

        final RequestEntity requestEntity = RequestEntityUtil_Old
            .init(AcsApiEnum.TOLERANCE_POLICY_DEFAULTS, GenericResourceCreatedResponse.class)
            .headers(headers)
            .body(TolerancePolicyDefaultsInputs.builder()
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

        final RequestEntity requestEntity = RequestEntityUtil_Old
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
    public ProductionDefaultsResponse getProductionDefaults() {
        setupHeader();

        final RequestEntity requestEntity = RequestEntityUtil_Old
            .init(AcsApiEnum.PRODUCTION_DEFAULTS, ProductionDefaultsResponse.class)
            .headers(headers)
            .inlineVariables(validUsername);

        return (ProductionDefaultsResponse) HTTPRequest.build(requestEntity).get().getResponseEntity();
    }

    /**
     * Sets production defaults
     *
     * @return GenericResourceCreatedResponse
     */
    public GenericResourceCreatedResponse setProductionDefaults() {
        setupHeader();
        //TODO: 16/03/2023 Use JSON for Production Defaults entry
        final RequestEntity requestEntity = RequestEntityUtil_Old
            .init(AcsApiEnum.PRODUCTION_DEFAULTS, GenericResourceCreatedResponse.class)
            .headers(headers)
            .body(ProductionDefaultsInputs.builder()
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

        final RequestEntity requestEntity = RequestEntityUtil_Old
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
    public UserPreferencesResponse getUserPreferences() {
        setupHeader();

        final RequestEntity requestEntity = RequestEntityUtil_Old
            .init(AcsApiEnum.USER_PREFERENCES, UserPreferencesResponse.class)
            .headers(headers)
            .inlineVariables(validUsername);

        ResponseWrapper<UserPreferencesResponse> response = HTTPRequest.build(requestEntity).get();

        return response.getResponseEntity();
    }

    /**
     * Calls get user preferences endpoint
     *
     * @return headers of the response
     */
    public Headers getUserPreferencesHeaders() {
        setupHeader();

        final RequestEntity requestEntity = RequestEntityUtil_Old
            .init(AcsApiEnum.USER_PREFERENCES, UserPreferencesResponse.class)
            .headers(headers)
            .inlineVariables(validUsername);

        return HTTPRequest.build(requestEntity).get().getHeaders();
    }

    /**
     * Calls get user preference by name
     *
     * @param userPrefToGet - String
     * @return String response of preference value
     */
    public String getUserPreferenceByName(String userPrefToGet) {
        setupHeader();

        final RequestEntity requestEntity = RequestEntityUtil_Old
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

        final RequestEntity requestEntity = RequestEntityUtil_Old
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

        final RequestEntity requestEntity = RequestEntityUtil_Old
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
    public ProductionInfoResponse getProductionInfo(ScenarioIterationKey scenarioIterationKey) {
        setupHeader();

        final RequestEntity requestEntity = RequestEntityUtil_Old
            .init(AcsApiEnum.PRODUCTION_INFO, ProductionInfoResponse.class)
            .headers(headers)
            .queryParams(new QueryParams().use("applyEdits", "true"))
            .inlineVariables(
                scenarioIterationKey.getScenarioKey().getWorkspaceId().toString(),
                scenarioIterationKey.getScenarioKey().getTypeName(),
                scenarioIterationKey.getScenarioKey().getMasterName(),
                scenarioIterationKey.getScenarioKey().getStateName(),
                scenarioIterationKey.getIteration().toString()
            );

        return (ProductionInfoResponse) HTTPRequest.build(requestEntity).get().getResponseEntity();
    }

    /**
     * Set user preferences
     *
     * @param costTableDecimalPlaces - String - value to set
     * @param useVpe                 - String - value to set
     * @param toleranceMode          - String - value to set
     * @return GenericResourceCreatedResponse instance
     */
    public GenericResourceCreatedResponse setUserPreferences(String costTableDecimalPlaces, String useVpe, String toleranceMode) {
        setupHeader();

        final RequestEntity requestEntity = RequestEntityUtil_Old
            .init(AcsApiEnum.USER_PREFERENCES, GenericResourceCreatedResponse.class)
            .headers(headers)
            .body(UserPreferencesInputs.builder()
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

        final RequestEntity requestEntity = RequestEntityUtil_Old
            .init(AcsApiEnum.USER_PREFERENCES, GenericErrorResponse.class)
            .headers(headers)
            .body(null)
            .inlineVariables(invalidUsername);

        return (GenericErrorResponse) HTTPRequest.build(requestEntity).post().getResponseEntity();
    }

    /**
     * Sets user preference by name
     *
     * @param prefToSetKey   - String - key of preference to set
     * @param prefToSetValue - String - value of preference to set
     * @return GenericResourceCreatedResponse instance
     */
    public GenericResourceCreatedResponse setUserPreferenceByName(String prefToSetKey, String prefToSetValue) {
        setupHeader();

        final RequestEntity requestEntity = RequestEntityUtil_Old
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
     * @param scenarioIterationKey      - scenario to set production info for
     * @return GenericResourceCreatedIdResponse
     */
    public GenericResourceCreatedIdResponse setProductionInfo(ProductionInfoResponse getProductionInfoResponse,
                                                              ScenarioIterationKey scenarioIterationKey) {
        setupHeader();

        final RequestEntity requestEntity = RequestEntityUtil_Old
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
     * Get Cost Results
     *
     * @param scenarioIterationKey - details of scenario to use (ScenarioIterationKey)
     * @param depth                - String - value to set
     * @return GetCostResults instance
     */

    public <T> ResponseWrapper<T> getCostResults(ScenarioIterationKey scenarioIterationKey, String depth, Class<T> klass) {
        setupHeader();

        final RequestEntity requestEntity;
        try {
            requestEntity = RequestEntityUtil_Old
                .init(AcsApiEnum.COST_RESULTS, klass)
                .headers(headers)
                .inlineVariables(
                    scenarioIterationKey.getScenarioKey().getWorkspaceId().toString(),
                    scenarioIterationKey.getScenarioKey().getTypeName(),
                    URLEncoder.encode(scenarioIterationKey.getScenarioKey().getMasterName(), StandardCharsets.UTF_8.toString()),
                    UrlEscapers.urlFragmentEscaper().escape(scenarioIterationKey.getScenarioKey().getStateName()),
                    scenarioIterationKey.getIteration().toString())
                .queryParams(new QueryParams().use("depth", depth))
                .urlEncodingEnabled(false);
        } catch (UnsupportedEncodingException e) {
            log.error("Failed to encode url parameters. " + e);
            throw new RuntimeException(e);
        }

        return HTTPRequest.build(requestEntity).get();
    }

    /**
     * Get Available Routings
     *
     * @param scenarioIterationKey - details of scenario to use (ScenarioIterationKey)
     * @param vpeName              - String - value to set
     * @param processGroupName     - String - Selected from ENUM
     * @return GetAvailableRoutingsResponse instance
     */

    public AvailableRoutingsFirstLevel getAvailableRoutings(ScenarioIterationKey scenarioIterationKey, String vpeName, String processGroupName) {
        setupHeader();

        final RequestEntity requestEntity;
        try {
            requestEntity = RequestEntityUtil_Old
                .init(AcsApiEnum.AVAILABLE_ROUTINGS, AvailableRoutingsFirstLevel.class)
                .headers(headers)
                .inlineVariables(
                    scenarioIterationKey.getScenarioKey().getWorkspaceId().toString(),
                    scenarioIterationKey.getScenarioKey().getTypeName(),
                    URLEncoder.encode(scenarioIterationKey.getScenarioKey().getMasterName(), StandardCharsets.UTF_8.toString()),
                    UrlEscapers.urlFragmentEscaper().escape(scenarioIterationKey.getScenarioKey().getStateName()),
                    scenarioIterationKey.getIteration().toString(),
                    URLEncoder.encode(vpeName, StandardCharsets.UTF_8.toString()),
                    URLEncoder.encode(processGroupName, StandardCharsets.UTF_8.toString()))
                .urlEncodingEnabled(false);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return (AvailableRoutingsFirstLevel) HTTPRequest.build(requestEntity).get().getResponseEntity();
    }

    /**
     * Get GCD Types
     *
     * @param processGroupName - String - Selected from ENUM
     */

    public <T> ResponseWrapper<T> getGcdTypes(String processGroupName, Class<T> klass) {
        setupHeader();

        final RequestEntity requestEntity = RequestEntityUtil_Old
                .init(AcsApiEnum.GCD_TYPES, klass)
                .headers(headers)
                .inlineVariables(processGroupName);

        return HTTPRequest.build(requestEntity).get();
    }

    /**
     * Save GCD Properties
     *
     * @param scenarioIterationKey - details of scenario to use (ScenarioIterationKey)
     */

    public GcdPropertiesResponse saveGcdProperties(ScenarioIterationKey scenarioIterationKey, String artifactKey, PropertiesToSet propertiesToSet, List<String> propertiesToReset) {
        setupHeader();

        List<GcdPropertiesGroupItemsInputs> groupItemsList = new ArrayList<>();
        groupItemsList.add(GcdPropertiesGroupItemsInputs.builder()
                .artifactKey(artifactKey)
                .propertiesToSet(propertiesToSet)
                .propertiesToReset(propertiesToReset)
            .build()
        );

        final RequestEntity requestEntity = RequestEntityUtil_Old
            .init(AcsApiEnum.GCD_PROPERTIES, GcdPropertiesResponse.class)
            .headers(headers)
            .body(GcdPropertiesInputs.builder()
                .groupItems(groupItemsList)
                .build())
            .inlineVariables(
                scenarioIterationKey.getScenarioKey().getWorkspaceId().toString(),
                scenarioIterationKey.getScenarioKey().getTypeName(),
                scenarioIterationKey.getScenarioKey().getMasterName(),
                scenarioIterationKey.getScenarioKey().getStateName(),
                scenarioIterationKey.getIteration().toString()
            );

        return (GcdPropertiesResponse) HTTPRequest.build(requestEntity).put().getResponseEntity();
    }

    /**
     * Save routing selection
     *
     * @param scenarioIterationKey - details of scenario to use (ScenarioIterationKey)
     * @return GenericResourceCreatedIdResponse instance
     */
    public GenericResourceCreatedIdResponse saveRoutingSelection(ScenarioIterationKey scenarioIterationKey, String name, String plantName, String processGroupName) {
        setupHeader();

        List<RoutingSelectionInputs> childrenList = new ArrayList<>();
        childrenList.add(RoutingSelectionInputs.builder()
            .name(name)
            .plantName(plantName)
            .processGroupName(processGroupName)
            .alternNode(true)
            .build()
        );

        final RequestEntity requestEntity = RequestEntityUtil_Old
            .init(AcsApiEnum.ROUTING_SELECTION, GenericResourceCreatedIdResponse.class)
            .headers(headers)
            .body(RoutingSelectionInputs.builder()
                .name(name)
                .plantName(plantName)
                .processGroupName(processGroupName)
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
    public PartPrimaryProcessGroupsResponse getPartPrimaryProcessGroups() {
        setupHeader();

        final RequestEntity requestEntity = RequestEntityUtil_Old
            .init(AcsApiEnum.PART_PRIMARY_PROCESS_GROUPS, PartPrimaryProcessGroupsResponse.class)
            .headers(headers);

        return (PartPrimaryProcessGroupsResponse) HTTPRequest.build(requestEntity).get().getResponseEntity();
    }

    /**
     * Gets 2D image of costed part by scenario iteration key
     *
     * @param scenarioIterationKey - values for url
     * @return String - base64 encoded image
     */
    public String get2DImageByScenarioIterationKey(ScenarioIterationKey scenarioIterationKey) {
        setupHeader();

        final RequestEntity requestEntity = RequestEntityUtil_Old
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
     * @param getWebImage          - flag to call desktop or web image endpoint (url is only different, so this reduces duplication)
     * @return String - Base64 image
     */
    public String getImageByScenarioIterationKey(ScenarioIterationKey scenarioIterationKey, boolean getWebImage) {
        setupHeader();

        AcsApiEnum getImageUrl = getWebImage ? AcsApiEnum.WEB_IMAGE_BY_SCENARIO_ITERATION_KEY :
            AcsApiEnum.DESKTOP_IMAGE_BY_SCENARIO_ITERATION_KEY;

        final RequestEntity requestEntity = RequestEntityUtil_Old
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
    public ActiveDimensionsResponse getActiveDimensionsByScenarioIterationKeyEndpoint(List<String> paramsForUrl) {
        setupHeader();

        final RequestEntity requestEntity = RequestEntityUtil_Old
            .init(AcsApiEnum.ACTIVE_DIMENSIONS, ActiveDimensionsResponse.class)
            .headers(headers)
            .inlineVariables(
                paramsForUrl.get(0),
                paramsForUrl.get(1),
                paramsForUrl.get(2),
                paramsForUrl.get(3),
                paramsForUrl.get(4)
            );

        return (ActiveDimensionsResponse) HTTPRequest.build(requestEntity).get().getResponseEntity();
    }

    /**
     * Gets Active Axes by Scenario Iteration Key
     *
     * @param paramsForUrl - list of values to input into url
     * @return GetActiveAxesByScenarioIterationKeyResponse instance
     */
    public ActiveAxesByScenarioIterationKeyResponse getActiveAxesByScenarioIterationKey(List<String> paramsForUrl) {
        setupHeader();

        final RequestEntity requestEntity = RequestEntityUtil_Old
            .init(AcsApiEnum.ACTIVE_AXES, ActiveAxesByScenarioIterationKeyResponse.class)
            .headers(headers)
            .inlineVariables(
                paramsForUrl.get(0),
                paramsForUrl.get(1),
                paramsForUrl.get(2),
                paramsForUrl.get(3),
                paramsForUrl.get(4)
            );

        return (ActiveAxesByScenarioIterationKeyResponse) HTTPRequest.build(requestEntity).get().getResponseEntity();
    }

    /**
     * Gets Artifact Table Info
     *
     * @return Instance of GetArtifactTableInfoResponse
     */
    public ArtifactTableInfoResponse getArtifactTableInfo() {
        setupHeader();

        final RequestEntity requestEntity = RequestEntityUtil_Old
            .init(AcsApiEnum.ARTIFACT_TABLE_INFO, ArtifactTableInfoResponse.class)
            .headers(headers)
            .inlineVariables(
                "Sheet Metal",
                "SimpleHole"
            );

        return (ArtifactTableInfoResponse) HTTPRequest.build(requestEntity).get().getResponseEntity();
    }

    /**
     * Negative version of Get Artifact Table Info - invalid process group
     *
     * @return String of body, containing 404 status code and error
     */
    public GenericErrorResponse getArtifactTableInfoInvalidProcessGroup() {
        setupHeader();

        final RequestEntity requestEntity = RequestEntityUtil_Old
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
    public GcdMappingResponse getGcdMapping(ScenarioIterationKey scenarioIterationKey) {
        setupHeader();

        final RequestEntity requestEntity = RequestEntityUtil_Old
            .init(AcsApiEnum.GCD_IMAGE_MAPPING, GcdMappingResponse.class)
            .headers(headers)
            .inlineVariables(
                scenarioIterationKey.getScenarioKey().getWorkspaceId().toString(),
                scenarioIterationKey.getScenarioKey().getTypeName(),
                scenarioIterationKey.getScenarioKey().getMasterName(),
                scenarioIterationKey.getScenarioKey().getStateName(),
                scenarioIterationKey.getIteration().toString()
            );

        return (GcdMappingResponse) HTTPRequest.build(requestEntity).get().getResponseEntity();
    }

    /**
     * Gets Artifact Properties
     *
     * @param scenarioIterationKey  - ScenarioIterationKey to use in url
     * @param getGcdMappingResponse - GetGcdMappingResponse to use in body
     * @return GetArtifactPropertiesResponse instance
     */
    public ArtifactPropertiesResponse getArtifactProperties(ScenarioIterationKey scenarioIterationKey, GcdMappingResponse getGcdMappingResponse) {
        setupHeader();

        String displayNameOne = getGcdMappingResponse.getDrawableNodesByArtifactKeyEntries().get(0).getKey().getDisplayName();
        String displayNameTwo = getGcdMappingResponse.getDrawableNodesByArtifactKeyEntries().get(1).getKey().getDisplayName();

        final RequestEntity requestEntity = RequestEntityUtil_Old
            .init(AcsApiEnum.ARTIFACT_PROPERTIES, ArtifactPropertiesResponse.class)
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

        return (ArtifactPropertiesResponse) HTTPRequest.build(requestEntity).post().getResponseEntity();
    }

    /**
     * Upload and cost part
     *
     * @param processGroup         - the process group
     * @param fileName             - the filename
     * @param productionInfoInputs - the production information
     * @return CostResultsResponse object
     */
    public CostOrderStatusOutputs uploadAndCost(String processGroup, String fileName, NewPartRequest productionInfoInputs) {
        FileUploadResources fileUploadResources = new FileUploadResources();

        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        fileUploadResources.checkValidProcessGroup(processGroup);

        FileResponse fileResponse = fileUploadResources.initializePartUpload(
            fileName,
            processGroup
        );

        FileUploadOutputs fileUploadOutputs = fileUploadResources.createFileUploadWorkorderSuppressError(
            fileResponse,
            testScenarioName
        );

        return fileUploadResources.costAssemblyOrPart(
            productionInfoInputs,
            fileUploadOutputs,
            processGroup,
            false
        );
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

    /**
     * Reset All User Preferences
     *
     * @return GenericResourceCreatedResponse instance
     */
    public GenericResourceCreatedResponse resetSettings() {
        setupHeader();

        final RequestEntity requestEntity = RequestEntityUtil_Old
            .init(AcsApiEnum.USER_PREFERENCES, GenericResourceCreatedResponse.class)
            .headers(headers)
            .body(UserPreferencesInputs.builder()
                .costTableDecimalPlaces("2")
                .tolerancePolicyDefaultsToleranceMode("SYSTEMDEFAULT")
                .prodInfoVpe("aPriori USA")
                .defaultScenarioName("Initial")
                .prodInfoDefaultAnnualVolume("5500")
                .prodInfoDefaultBatchSize(null)
                .prodInfoDefaultMaterial(null)
                .prodInfoDefaultPg(null)
                .prodInfoDefaultMaterialCatalogName(null)
                .prodInfoDefaultProductionLife("5")
                .tolerancePolicyDefaultsBendAngleToleranceOverride(null)
                .tolerancePolicyDefaultsCadToleranceReplacement(null)
                .tolerancePolicyDefaultsDiamToleranceOverride(null)
                .tolerancePolicyDefaultsFlatnessOverride(null)
                .tolerancePolicyDefaultsMinCadToleranceThreshhold(null)
                .tolerancePolicyDefaultsFlatnessOverride(null)
                .tolerancePolicyDefaultsPerpendicularityOverride(null)
                .tolerancePolicyDefaultsPositionToleranceOverride(null)
                .tolerancePolicyDefaultsPerpendicularityOverride(null)
                .tolerancePolicyDefaultsParallelismOverride(null)
                .tolerancePolicyDefaultsProfileOfSurfaceOverride(null)
                .tolerancePolicyDefaultsRoughnessOverride(null)
                .tolerancePolicyDefaultsRoughnessRzOverride(null)
                .tolerancePolicyDefaultsRunoutOverride(null)
                .tolerancePolicyDefaultsStraightnessOverride(null)
                .tolerancePolicyDefaultsSymmetryOverride(null)
                .tolerancePolicyDefaultsToleranceOverride(null)
                .tolerancePolicyDefaultsTotalRunoutOverride(null)
                .tolerancePolicyDefaultsBendAngleToleranceOverride(null)
                .build())
            .inlineVariables(validUsername);

        return (GenericResourceCreatedResponse) HTTPRequest.build(requestEntity).post().getResponseEntity();
    }

}
