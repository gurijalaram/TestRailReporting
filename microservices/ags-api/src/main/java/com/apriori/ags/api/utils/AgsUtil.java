package com.apriori.ags.api.utils;

import static com.apriori.shared.util.enums.RolesEnum.APRIORI_DEVELOPER;

import com.apriori.ags.api.enums.AgsApiEnum;
import com.apriori.bcm.api.models.request.AddInputsRequest;
import com.apriori.bcm.api.models.request.GroupItems;
import com.apriori.bcm.api.models.request.Inputrow;
import com.apriori.bcm.api.models.request.MultipleDelete;
import com.apriori.bcm.api.models.request.Worksheet;
import com.apriori.bcm.api.models.request.WorksheetInputRowsRequest;
import com.apriori.bcm.api.models.request.WorksheetRequest;
import com.apriori.bcm.api.models.response.AnalysisInput;
import com.apriori.bcm.api.models.response.InputRowPostResponse;
import com.apriori.bcm.api.models.response.InputRowsGroupsResponse;
import com.apriori.bcm.api.models.response.MultipleInputRowsResponse;
import com.apriori.bcm.api.models.response.SuccessAddingAnalysisInputs;
import com.apriori.bcm.api.models.response.WorkSheetInputRowGetResponse;
import com.apriori.bcm.api.models.response.WorkSheetResponse;
import com.apriori.bcm.api.models.response.WorkSheets;
import com.apriori.shared.util.KeyValueUtil;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.FileResourceUtil;
import com.apriori.shared.util.http.utils.QueryParams;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.RequestEntityUtilBuilder;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.http.utils.TestUtil;
import com.apriori.shared.util.json.JsonManager;
import com.apriori.shared.util.models.response.component.ComponentResponse;
import com.apriori.shared.util.models.response.component.ScenarioItem;
import com.apriori.shared.util.properties.PropertiesContext;

import com.google.common.net.HttpHeaders;
import org.apache.hc.core5.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AgsUtil extends TestUtil {

    protected static UserCredentials testingUser = UserUtil.getUser(APRIORI_DEVELOPER);
    protected static RequestEntityUtil requestEntityUtil;
    private Map<String, String> headers = new HashMap<String, String>() {
        {
            put("Referer", PropertiesContext.get("cidapp.ui_url"));
        }
    };

    @BeforeAll
    public static void init() {
        requestEntityUtil = RequestEntityUtilBuilder
            .useRandomUser(APRIORI_DEVELOPER)
            .useTokenInRequests();

        testingUser = requestEntityUtil.getEmbeddedUser();
    }

    /**
     * Returns list of worksheets according to parameters
     *
     * @param param - name of parameter
     * @param value - value of parameter
     * @return response object
     */
    public ResponseWrapper<WorkSheets> getWorksheetsWithParams(String param, String value) {
        final RequestEntity requestEntity =
            requestEntityUtil.init(AgsApiEnum.WORKSHEETS, WorkSheets.class)
                .expectedResponseCode(HttpStatus.SC_OK)
                .queryParams(new QueryParams().use(param, value))
                .headers(headers);
        return HTTPRequest.build(requestEntity).get();
    }

    /**
     * Creates a new worksheet
     *
     * @param name - name of worksheet
     * @return response object
     */
    public ResponseWrapper<WorkSheetResponse> createWorksheet(String name) {

        WorksheetRequest body = WorksheetRequest
            .builder()
            .worksheet(Worksheet
                .builder()
                .name(name)
                .build())
            .build();

        final RequestEntity requestEntity =
            requestEntityUtil.init(AgsApiEnum.WORKSHEETS, WorkSheetResponse.class)
                .headers(headers)
                .body(body)
                .expectedResponseCode(HttpStatus.SC_CREATED);
        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Returns single worksheet
     *
     * @param worksheetIdentity - the worksheet identity
     * @return response object
     */
    public ResponseWrapper<WorkSheetResponse> getWorksheet(String worksheetIdentity) {
        RequestEntity requestEntity =
            requestEntityUtil.init(AgsApiEnum.WORKSHEET_BY_ID, WorkSheetResponse.class)
                .inlineVariables(worksheetIdentity)
                .headers(headers)
                .expectedResponseCode(HttpStatus.SC_OK);
        return HTTPRequest.build(requestEntity).get();
    }

    /**
     * Deletes a worksheet
     *
     * @param worksheetIdentity - the worksheet identity
     */
    public void deleteWorksheet(String worksheetIdentity) {
        RequestEntity requestEntity = requestEntityUtil.init(AgsApiEnum.WORKSHEET_BY_ID, null)
            .inlineVariables(worksheetIdentity)
            .headers(headers)
            .expectedResponseCode(HttpStatus.SC_NO_CONTENT);
        HTTPRequest.build(requestEntity).delete();
    }

    /**
     * Updates worksheet name and description
     *
     * @param name              - new name of worksheet
     * @param description       - description
     * @param worksheetIdentity - the worksheet identity
     * @return response object
     */
    public ResponseWrapper<WorkSheetResponse> updateWorksheet(String name, String description, String worksheetIdentity) {
        WorksheetRequest body = WorksheetRequest
            .builder()
            .worksheet(Worksheet
                .builder()
                .name(name)
                .description(description)
                .build())
            .build();

        RequestEntity requestEntity =
            requestEntityUtil.init(AgsApiEnum.WORKSHEET_BY_ID, WorkSheetResponse.class)
                .inlineVariables(worksheetIdentity)
                .headers(headers)
                .expectedResponseCode(HttpStatus.SC_OK)
                .body(body);
        return HTTPRequest.build(requestEntity).patch();
    }

    /**
     * Adds scenario to a worksheet
     *
     * @param componentIdentity - component identity
     * @param scenarioIdentity  - scenario identity
     * @param worksheetIdentity - worksheet identity
     * @return response object
     */
    public ResponseWrapper<InputRowPostResponse> createWorkSheetInputRow(String componentIdentity, String scenarioIdentity, String worksheetIdentity) {
        WorksheetInputRowsRequest body = WorksheetInputRowsRequest
            .builder()
            .inputRow(Inputrow
                .builder()
                .componentIdentity(componentIdentity)
                .scenarioIdentity(scenarioIdentity)
                .build())
            .build();
        final RequestEntity requestEntity =
            requestEntityUtil.init(AgsApiEnum.WORKSHEET_INPUT_NAME, InputRowPostResponse.class)
                .body(body)
                .inlineVariables(worksheetIdentity)
                .headers(headers)
                .expectedResponseCode(HttpStatus.SC_CREATED);
        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Returns all scenarios in the worksheet
     *
     * @param worksheetIdentity - the worksheet identity
     * @return response object
     */
    public ResponseWrapper<WorkSheetInputRowGetResponse> getWorkSheetInputRow(String worksheetIdentity) {
        final RequestEntity requestEntity =
            requestEntityUtil.init(AgsApiEnum.WORKSHEET_INPUT_NAME, WorkSheetInputRowGetResponse.class)
                .inlineVariables(worksheetIdentity)
                .headers(headers)
                .expectedResponseCode(HttpStatus.SC_OK);
        return HTTPRequest.build(requestEntity).get();
    }

    /**
     * Adds multiple scenarios to a worksheet
     *
     * @param worksheetIdentity                 - the worksheet identity
     * @param componentIdentityScenarioIdentity - component identity and scenario identity
     * @param expectedResponseCode              - expected response code
     * @return response object
     */
    public ResponseWrapper<MultipleInputRowsResponse> addMultipleInputRows(String worksheetIdentity, List<String> componentIdentityScenarioIdentity, Integer expectedResponseCode) {
        List<String[]> componentIdScenarioId = componentIdentityScenarioIdentity.stream().map(o -> o.split(",")).collect(Collectors.toList());

        RequestEntity requestEntity = requestEntityUtil.init(AgsApiEnum.MULTIPLE_ROWS, MultipleInputRowsResponse.class)
            .inlineVariables(worksheetIdentity)
            .headers(headers)
            .body(AddInputsRequest.builder()
                .groupItems(componentIdScenarioId.stream().map(o -> Inputrow.builder()
                        .componentIdentity(o[0])
                        .scenarioIdentity(o[1])
                        .build())
                    .collect(Collectors.toList()))
                .build())
            .expectedResponseCode(expectedResponseCode);
        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Edits public input rows
     *
     * @param klass                - response class
     * @param worksheetIdentity    - the worksheet identity
     * @param inputRowIdentity     - input row identity
     * @param expectedResponseCode - expected response code
     * @return response object
     */
    public <T> ResponseWrapper<T> editInputRow(Class<T> klass, String worksheetIdentity, String inputRowIdentity, Integer expectedResponseCode) {
        RequestEntity requestEntity = requestEntityUtil.init(AgsApiEnum.EDIT_INPUTS, klass)
            .inlineVariables(worksheetIdentity)
            .headers(headers)
            .body(MultipleDelete.builder()
                .groupItems(Collections.singletonList(GroupItems.builder()
                    .inputRowIdentity(inputRowIdentity)
                    .build()))
                .build())
            .expectedResponseCode(expectedResponseCode);
        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Deletes scenario from a worksheet
     *
     * @param worksheetIdentity - the worksheet identity
     * @param inputRowIdentity  - input row identity
     * @return response object
     */
    public ResponseWrapper<InputRowsGroupsResponse> deleteInputRow(String worksheetIdentity, String inputRowIdentity) {

        RequestEntity requestEntity =
            requestEntityUtil.init(AgsApiEnum.DELETE_INPUTS, InputRowsGroupsResponse.class)
                .inlineVariables(worksheetIdentity)
                .headers(headers)
                .body(MultipleDelete.builder()
                    .groupItems(Arrays.asList(GroupItems
                        .builder()
                        .inputRowIdentity(inputRowIdentity)
                        .build()))
                    .build())
                .expectedResponseCode(HttpStatus.SC_OK);
        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Adds costing input for a scenario in worksheet
     *
     * @param processGroupName  - process group name
     * @param worksheetIdentity - the worksheet identity
     * @param inputRowIdentity  - the input row identity
     * @return response object
     */
    public ResponseWrapper<SuccessAddingAnalysisInputs> addAnalysisInputs(String processGroupName, String worksheetIdentity, String inputRowIdentity) {
        AnalysisInput requestBody = JsonManager.deserializeJsonFromFile(FileResourceUtil.getResourceAsFile("AddAnalysisInputsData.json").getPath(), AnalysisInput.class);
        requestBody.setProcessGroupName(processGroupName);

        RequestEntity requestEntity = requestEntityUtil.init(AgsApiEnum.ANALYSIS_INPUTS, SuccessAddingAnalysisInputs.class)
            .inlineVariables(worksheetIdentity)
            .headers(headers)
            .expectedResponseCode(HttpStatus.SC_OK)
            .body(AddInputsRequest.builder()
                .groupItems(Collections.singletonList(Inputrow.builder().inputRowIdentity(inputRowIdentity).build()))
                .analysisInput(requestBody).build());

        return HTTPRequest.build(requestEntity).patch();
    }

    /**
     * Costs worksheet
     *
     * @param klass                - response class
     * @param worksheetIdentity    - the worksheet identity
     * @param expectedResponseCode - expected response code
     * @return response object
     */
    public <T> ResponseWrapper<T> costWorksheet(Class<T> klass, String worksheetIdentity, Integer expectedResponseCode) {
        RequestEntity requestEntity = requestEntityUtil.init(AgsApiEnum.COST_WORKSHEET, klass)
            .inlineVariables(worksheetIdentity)
            .headers(headers)
            .expectedResponseCode(expectedResponseCode);
        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Returns filtered/sorted list of scenario iteration candidates
     *
     * @param worksheetIdentity - worksheet identity
     * @param param             - name of parameter
     * @param value             - value of parameter
     * @return response object
     */
    public ResponseWrapper<ComponentResponse> getCandidatesWithParams(String worksheetIdentity, String param, String value) {
        RequestEntity requestEntity = requestEntityUtil.init(AgsApiEnum.CANDIDATES, ComponentResponse.class)
            .inlineVariables(worksheetIdentity)
            .headers(headers)
            .expectedResponseCode(HttpStatus.SC_OK)
            .queryParams(new QueryParams().use(param, value));
        return HTTPRequest.build(requestEntity).get();
    }

    /**
     * Deletes multiple worksheets at once
     *
     * @param klass                - class
     * @param expectedResponseCode - expected response code
     * @param worksheetIdentity    - worksheet identity
     * @return response object
     */
    public <T> ResponseWrapper<T> deleteMultipleWorksheets(Class<T> klass, Integer expectedResponseCode, String... worksheetIdentity) {
        RequestEntity requestEntity = requestEntityUtil.init(AgsApiEnum.DELETE_MULTIPLE_WORKSHEETS, klass)
            .headers(headers)
            .body(MultipleDelete.builder()
                .groupItems(Arrays.stream(worksheetIdentity)
                    .map(worksheetId -> GroupItems.builder().identity(worksheetId)
                        .build())
                    .collect(Collectors.toList()))
                .build())
            .expectedResponseCode(expectedResponseCode);
        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Gets request of common iteration endpoint
     *
     * @return response object
     */
    public ResponseWrapper<ComponentResponse> getIterationsRequest() {
        RequestEntity requestEntity = requestEntityUtil.init(AgsApiEnum.SCENARIO_ITERATIONS, ComponentResponse.class)
            .expectedResponseCode(HttpStatus.SC_OK)
            .headers(headers);
        return HTTPRequest.build(requestEntity).get();
    }

    /**
     * Calls an api with GET verb
     *
     * @param queryParams - the query parameters
     * @return response object
     */
    private ResponseWrapper<ComponentResponse> getBaseCssComponents(QueryParams queryParams) {
        int socketTimeout = 630000;
        Map<String, String> headers = new HashMap<String, String>() {
            {
                put(HttpHeaders.REFERER, PropertiesContext.get("cidapp.ui_url"));
                put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_FORM_URLENCODED.toString());
            }
        };

        RequestEntity requestEntity = requestEntityUtil.init(AgsApiEnum.SCENARIO_ITERATIONS_SEARCH, ComponentResponse.class)
            .headers(headers)
            .queryParams(queryParams)
            .socketTimeout(socketTimeout)
            .expectedResponseCode(HttpStatus.SC_OK);

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Calls an api with GET verb. No wait is performed for this call.
     *
     * @param paramKeysValues - the query param key and value. Comma separated for key/value pair eg. "scenarioState[EQ], not_costed". The operand (eg. [CN]) MUST be included in the query.
     * @return response object
     */
    public List<ScenarioItem> getBaseCssComponents(String... paramKeysValues) {
        return getBaseCssComponents(new KeyValueUtil().keyValue(paramKeysValues, ","))
            .getResponseEntity()
            .getItems();
    }

    /**
     * Creates search request by component type
     *
     * @param componentType   - the component type
     * @return the response wrapper that contains the response data
     */
    public ResponseWrapper<ComponentResponse> postSearchRequest(String componentType) {
        Map<String, String> headers = new HashMap<String, String>() {
            {
                put(HttpHeaders.REFERER, PropertiesContext.get("cidapp.ui_url"));
                put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_FORM_URLENCODED.toString());
            }
        };
        RequestEntity requestEntity = requestEntityUtil.init(AgsApiEnum.SCENARIO_ITERATIONS_SEARCH, ComponentResponse.class)
            .headers(headers)
            .xwwwwFormUrlEncodeds(Collections.singletonList(new HashMap<String, String>() {
                {
                    put("pageNumber", "1");
                    put("pageSize", "10");
                    put("latest[EQ]", "true");
                    put("componentType[IN]", componentType);
                    put("sortBy[DESC]", "scenarioCreatedAt");
                }
            })).expectedResponseCode(HttpStatus.SC_OK);

        return HTTPRequest.build(requestEntity).post();
    }
}