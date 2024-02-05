package com.apriori.bcm.api.utils;

import com.apriori.bcm.api.enums.BcmAppAPIEnum;
import com.apriori.bcm.api.models.request.AddInputsRequest;
import com.apriori.bcm.api.models.request.Inputrow;
import com.apriori.bcm.api.models.request.Worksheet;
import com.apriori.bcm.api.models.request.WorksheetInputRowsRequest;
import com.apriori.bcm.api.models.request.WorksheetRequest;
import com.apriori.bcm.api.models.response.AnalysisInput;
import com.apriori.bcm.api.models.response.ErrorResponse;
import com.apriori.bcm.api.models.response.WorkSheetInputRowGetResponse;
import com.apriori.bcm.api.models.response.WorkSheetInputRowResponse;
import com.apriori.bcm.api.models.response.WorkSheetResponse;
import com.apriori.bcm.api.models.response.WorkSheets;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.FileResourceUtil;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.RequestEntityUtilBuilder;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.http.utils.TestUtil;
import com.apriori.shared.util.json.JsonManager;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;

import java.util.Collections;

@Slf4j
public class BcmUtil extends TestUtil {

    protected static UserCredentials testingUser = UserUtil.getUser("admin");
    protected static RequestEntityUtil requestEntityUtil;

    @BeforeAll
    public static  void init() {
        requestEntityUtil = RequestEntityUtilBuilder
            .useRandomUser("admin")
            .useApUserContextInRequests();

        testingUser = requestEntityUtil.getEmbeddedUser();
    }

    /**
     *
     * request to create worksheet
     * @param name - name of worksheet
     *
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
            requestEntityUtil.init(BcmAppAPIEnum.WORKSHEETS, WorkSheetResponse.class)
                .body(body)
                .expectedResponseCode(HttpStatus.SC_CREATED);
        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * adding part (component) int existing worksheet
     * @param componentIdentity - componentIdentity of added part
     * @param scenarioIdentity - scenarioIdentity of added part
     * @param worksheetIdentity - param to  the request call
     * @return response object
     */

    public ResponseWrapper<WorkSheetInputRowResponse> createWorkSheetInputRow(String componentIdentity, String scenarioIdentity, String worksheetIdentity) {
        WorksheetInputRowsRequest body = WorksheetInputRowsRequest
            .builder()
            .inputRow(Inputrow
                .builder()
                .componentIdentity(componentIdentity)
                .scenarioIdentity(scenarioIdentity)
                .build())
            .build();
        final RequestEntity requestEntity =
            requestEntityUtil.init(BcmAppAPIEnum.WORKSHEET_INPUT_NAME, WorkSheetInputRowResponse.class)
                .body(body)
                .inlineVariables(worksheetIdentity)
                .expectedResponseCode(HttpStatus.SC_CREATED);
        return HTTPRequest.build(requestEntity).post();
    }


    /**
     * GET worksheet rows
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
                .expectedResponseCode(HttpStatus.SC_BAD_REQUEST);
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
     * @param name - worksheet name
     * @param description - worksheet description
     * @param klass - class
     * @param worksheetIdentity - worksheet identity
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
     * @param klass - class
     * @param processGroupName - process group name
     * @param worksheetIdentity - identity of worksheet
     * @param expectedResponseCode - expected response code
     * @param inputRowIdentity - identity of input row
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
     * @param klass - class
     * @param worksheetIdentity - worksheet identity
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
}
