package com.apriori.bcm.api.utils;

import static com.apriori.shared.util.enums.RolesEnum.APRIORI_DEVELOPER;

import com.apriori.bcm.api.enums.BcmAppAPIEnum;
import com.apriori.bcm.api.models.request.AddInputsRequest;
import com.apriori.bcm.api.models.request.GroupItems;
import com.apriori.bcm.api.models.request.Inputrow;
import com.apriori.bcm.api.models.request.MultipleDelete;
import com.apriori.bcm.api.models.request.Worksheet;
import com.apriori.bcm.api.models.request.WorksheetInputRowsRequest;
import com.apriori.bcm.api.models.request.WorksheetRequest;
import com.apriori.bcm.api.models.response.AnalysisInput;
import com.apriori.bcm.api.models.response.ErrorResponse;
import com.apriori.bcm.api.models.response.InputRowPostResponse;
import com.apriori.bcm.api.models.response.WorkSheetInputRowGetResponse;
import com.apriori.bcm.api.models.response.WorkSheetResponse;
import com.apriori.bcm.api.models.response.WorkSheets;
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

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class BcmUtil extends TestUtil {

    protected static UserCredentials testingUser = UserUtil.getUser(APRIORI_DEVELOPER);
    private RequestEntityUtil requestEntityUtil;

    public BcmUtil(RequestEntityUtil requestEntityUtil) {
        super.requestEntityUtil = requestEntityUtil;
        this.requestEntityUtil = requestEntityUtil;
    }

    // FIXME: 01/07/2024 cn - remove this constructor when refactoring
    public BcmUtil() {

    }

    /**
     * request to create worksheet
     *
     * @param name - name of worksheet
     * @return response object
     */

    public WorkSheetResponse createWorksheet(String name) {

        WorksheetRequest body = WorksheetRequest
            .builder()
            .worksheet(Worksheet
                .builder()
                .name(name)
                .build())
            .build();

        final RequestEntity requestEntity =
            requestEntityUtil.init(BcmAppAPIEnum.WORKSHEETS, WorkSheetResponse.class)
                .body(body)
                .expectedResponseCode(HttpStatus.SC_CREATED);
        ResponseWrapper<WorkSheetResponse> response = HTTPRequest.build(requestEntity).post();

        return response.getResponseEntity();
    }

    /**
     * adding part (component) into existing worksheet with email - hch is used in UI tests
     *
     * @param componentIdentity - componentIdentity of added part
     * @param scenarioIdentity  - scenarioIdentity of added part
     * @param worksheetIdentity - param to  the request call
     * @return response object
     */

    public ResponseWrapper<InputRowPostResponse> createWorkSheetInputRowWithEmail(String componentIdentity, String scenarioIdentity, String worksheetIdentity, UserCredentials userCred) {
        WorksheetInputRowsRequest body = WorksheetInputRowsRequest
            .builder()
            .inputRow(Inputrow
                .builder()
                .componentIdentity(componentIdentity)
                .scenarioIdentity(scenarioIdentity)
                .build())
            .build();

        requestEntityUtil = RequestEntityUtilBuilder
            .useCustomUser(userCred)
            .useApUserContextInRequests();

        final RequestEntity requestEntity =
            requestEntityUtil.init(BcmAppAPIEnum.WORKSHEET_INPUT_NAME, InputRowPostResponse.class)
                .body(body)
                .inlineVariables(worksheetIdentity)
                .expectedResponseCode(HttpStatus.SC_CREATED);
        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * adding part (component) into existing worksheet
     *
     * @param componentIdentity - componentIdentity of added part
     * @param scenarioIdentity  - scenarioIdentity of added part
     * @param worksheetIdentity - param to  the request call
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
            requestEntityUtil.init(BcmAppAPIEnum.WORKSHEET_INPUT_NAME, InputRowPostResponse.class)
                .body(body)
                .inlineVariables(worksheetIdentity)
                .expectedResponseCode(HttpStatus.SC_CREATED);
        return HTTPRequest.build(requestEntity).post();
    }


    /**
     * GET worksheet rows
     *
     * @param worksheetIdentity - identity of the worksheet
     * @return
     */
    public ResponseWrapper<WorkSheetInputRowGetResponse> getWorkSheetInputRow(String worksheetIdentity) {
        final RequestEntity requestEntity =
            requestEntityUtil.init(BcmAppAPIEnum.WORKSHEET_INPUT_NAME, WorkSheetInputRowGetResponse.class)
                .inlineVariables(worksheetIdentity)
                .expectedResponseCode(HttpStatus.SC_OK);
        return HTTPRequest.build(requestEntity).get();
    }

    /**
     * Making error request with an existing name of worksheet
     *
     * @param name - worksheet name
     * @return error response
     */
    public ResponseWrapper<ErrorResponse> createWorksheetAlreadyExists(String name) {

        WorksheetRequest body = WorksheetRequest
            .builder()
            .worksheet(Worksheet
                .builder()
                .name(name)
                .build())
            .build();

        final RequestEntity requestEntity =
            requestEntityUtil.init(BcmAppAPIEnum.WORKSHEETS, ErrorResponse.class)
                .body(body)
                .expectedResponseCode(HttpStatus.SC_CONFLICT);
        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Get the list of worksheets
     *
     * @return object ResponseWrapper
     */
    public ResponseWrapper<WorkSheets> getWorksheets() {
        final RequestEntity requestEntity =
            requestEntityUtil.init(BcmAppAPIEnum.WORKSHEETS, WorkSheets.class)
                .expectedResponseCode(HttpStatus.SC_OK);
        return HTTPRequest.build(requestEntity).get();
    }

    /**
     * Updating worksheet request
     *
     * @param name                 - worksheet name
     * @param description          - worksheet description
     * @param klass                - class
     * @param worksheetIdentity    - worksheet identity
     * @param expectedResponseCode = expected response code
     * @return response object
     */
    public <T> ResponseWrapper<T> updateWorksheet(String name, String description, Class<T> klass, String worksheetIdentity, Integer expectedResponseCode) {
        WorksheetRequest body = WorksheetRequest
            .builder()
            .worksheet(Worksheet
                .builder()
                .name(name)
                .description(description)
                .build())
            .build();

        RequestEntity requestEntity =
            requestEntityUtil.init(BcmAppAPIEnum.WORKSHEET_BY_ID, klass)
                .inlineVariables(worksheetIdentity)
                .expectedResponseCode(expectedResponseCode)
                .body(body);
        return HTTPRequest.build(requestEntity).patch();
    }

    /**
     * Adding analysis inputs for an input row
     *
     * @param klass                - class
     * @param processGroupName     - process group name
     * @param worksheetIdentity    - identity of worksheet
     * @param expectedResponseCode - expected response code
     * @param inputRowIdentity     - identity of input row
     * @return response object
     */
    public <T> ResponseWrapper<T> addAnalysisInputs(Class<T> klass, String processGroupName, String worksheetIdentity, Integer expectedResponseCode, String inputRowIdentity) {
        AnalysisInput requestBody = JsonManager.deserializeJsonFromFile(FileResourceUtil.getResourceAsFile("AddAnalysisInputsData.json").getPath(), AnalysisInput.class);
        requestBody.setProcessGroupName(processGroupName);

        RequestEntity requestEntity = requestEntityUtil.init(BcmAppAPIEnum.ANALYSIS_INPUTS, klass)
            .inlineVariables(worksheetIdentity)
            .expectedResponseCode(expectedResponseCode)
            .body(AddInputsRequest.builder()
                .groupItems(Collections.singletonList(Inputrow.builder().inputRowIdentity(inputRowIdentity).build()))
                .analysisInput(requestBody).build());

        return HTTPRequest.build(requestEntity).patch();
    }

    /**
     * GET worksheet by its identity
     *
     * @param klass                - class
     * @param worksheetIdentity    - worksheet identity
     * @param expectedResponseCode = expected response code
     * @return response object
     */
    public <T> ResponseWrapper<T> getWorksheet(Class<T> klass, String worksheetIdentity, Integer expectedResponseCode) {

        RequestEntity requestEntity =
            requestEntityUtil.init(BcmAppAPIEnum.WORKSHEET_BY_ID, klass)
                .inlineVariables(worksheetIdentity)
                .expectedResponseCode(expectedResponseCode);
        return HTTPRequest.build(requestEntity).get();
    }

    public <T> ResponseWrapper<T> deleteInputRow(Class<T> klass, String worksheetIdentity, String inputRowIdentity, Integer expectedResponseCode) {

        RequestEntity requestEntity =
            requestEntityUtil.init(BcmAppAPIEnum.DELETE_INPUTS, klass)
                .inlineVariables(worksheetIdentity)
                .body(MultipleDelete.builder()
                    .groupItems(Arrays.asList(GroupItems
                        .builder()
                        .inputRowIdentity(inputRowIdentity)
                        .build()))
                    .build())
                .expectedResponseCode(expectedResponseCode);
        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Deletes worksheet
     *
     * @param klass                - class
     * @param worksheetIdentity    - worksheet identity
     * @param expectedResponseCode - expected response code
     * @return response object
     */
    public <T> ResponseWrapper<T> deleteWorksheet(Class<T> klass, String worksheetIdentity, Integer expectedResponseCode) {
        RequestEntity requestEntity = requestEntityUtil.init(BcmAppAPIEnum.WORKSHEET_BY_ID, klass)
            .inlineVariables(worksheetIdentity)
            .expectedResponseCode(expectedResponseCode);
        return HTTPRequest.build(requestEntity).delete();
    }

    /**
     * Deletes worksheet - from UI for specific user
     *
     * @param klass                - class
     * @param worksheetIdentity    - worksheet identity
     * @param expectedResponseCode - expected response code
     * @return response object
     */
    public <T> ResponseWrapper<T> deleteWorksheetWithEmail(Class<T> klass, String worksheetIdentity, Integer expectedResponseCode, UserCredentials userCred) {
        requestEntityUtil = RequestEntityUtilBuilder
            .useCustomUser(userCred)
            .useApUserContextInRequests();

        RequestEntity requestEntity = requestEntityUtil.init(BcmAppAPIEnum.WORKSHEET_BY_ID, klass)
            .inlineVariables(worksheetIdentity)
            .expectedResponseCode(expectedResponseCode);
        return HTTPRequest.build(requestEntity).delete();
    }

    /**
     * Edits public input row
     *
     * @param klass                - class
     * @param worksheetIdentity    - worksheet identity
     * @param inputRowIdentity     - identity of input row
     * @param expectedResponseCode - expected response code
     * @return response object
     */
    public <T> ResponseWrapper<T> editPublicInputRow(Class<T> klass, String worksheetIdentity, String inputRowIdentity, Integer expectedResponseCode) {
        RequestEntity requestEntity = requestEntityUtil.init(BcmAppAPIEnum.EDIT_INPUTS, klass)
            .inlineVariables(worksheetIdentity)
            .body(MultipleDelete.builder()
                .groupItems(Collections.singletonList(GroupItems.builder()
                    .inputRowIdentity(inputRowIdentity)
                    .build()))
                .build())
            .expectedResponseCode(expectedResponseCode);
        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Costs worksheet
     *
     * @param klass                - class
     * @param worksheetIdentity    - worksheet identity
     * @param expectedResponseCode - expected response code
     * @return response object
     */
    public <T> ResponseWrapper<T> costWorksheet(Class<T> klass, String worksheetIdentity, Integer expectedResponseCode) {
        RequestEntity requestEntity = requestEntityUtil.init(BcmAppAPIEnum.COST_WORKSHEET, klass)
            .inlineVariables(worksheetIdentity)
            .expectedResponseCode(expectedResponseCode);
        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Adds multiple input rows to a worksheet
     *
     * @param klass                             - class
     * @param worksheetIdentity                 - worksheet identity
     * @param componentIdentityScenarioIdentity - list of component and scenario identities
     * @param expectedResponseCode              - expected response code
     * @return response object
     */
    public <T> ResponseWrapper<T> addMultipleInputRows(Class<T> klass, String worksheetIdentity, List<String> componentIdentityScenarioIdentity, Integer expectedResponseCode) {
        List<String[]> componentIdScenarioId = componentIdentityScenarioIdentity.stream().map(o -> o.split(",")).collect(Collectors.toList());

        RequestEntity requestEntity = requestEntityUtil.init(BcmAppAPIEnum.MULTIPLE_ROWS, klass)
            .inlineVariables(worksheetIdentity)
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
     * Deletes multiple worksheets at once
     *
     * @param klass                - class
     * @param expectedResponseCode - expected response code
     * @param worksheetIdentity    - worksheet identity
     * @return response object
     */
    public <T> ResponseWrapper<T> deleteMultipleWorksheets(Class<T> klass, Integer expectedResponseCode, String... worksheetIdentity) {
        RequestEntity requestEntity = requestEntityUtil.init(BcmAppAPIEnum.DELETE_MULTIPLE_WORKSHEETS, klass)
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
     * Returns list of scenario iteration candidates
     *
     * @param worksheetIdentity - worksheet identity
     * @return response object
     */
    public ResponseWrapper<ComponentResponse> getCandidates(String worksheetIdentity) {
        RequestEntity requestEntity = requestEntityUtil.init(BcmAppAPIEnum.CANDIDATES, ComponentResponse.class)
            .inlineVariables(worksheetIdentity)
            .expectedResponseCode(HttpStatus.SC_OK);
        return HTTPRequest.build(requestEntity).get();
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
        RequestEntity requestEntity = requestEntityUtil.init(BcmAppAPIEnum.CANDIDATES, ComponentResponse.class)
            .inlineVariables(worksheetIdentity)
            .expectedResponseCode(HttpStatus.SC_OK)
            .queryParams(new QueryParams().use(param, value));
        return HTTPRequest.build(requestEntity).get();
    }
}
