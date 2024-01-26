package com.apriori.bcm.api.utils;

import com.apriori.bcm.api.enums.BcmAppAPIEnum;
import com.apriori.bcm.api.models.request.Inputrow;
import com.apriori.bcm.api.models.request.Worksheet;
import com.apriori.bcm.api.models.request.WorksheetInputRowsRequest;
import com.apriori.bcm.api.models.request.WorksheetRequest;
import com.apriori.bcm.api.models.response.ErrorResponse;
import com.apriori.bcm.api.models.response.WorkSheetInputRowResponse;
import com.apriori.bcm.api.models.response.WorkSheetResponse;
import com.apriori.bcm.api.models.response.WorkSheets;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.RequestEntityUtilBuilder;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.http.utils.TestUtil;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;

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

    // TODO: 15/01/2024 missing javadocs
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

    // TODO: 15/01/2024 missing javadocs
    public ResponseWrapper<WorkSheets> getWorksheets() {
        final RequestEntity requestEntity =
            requestEntityUtil.init(BcmAppAPIEnum.WORKSHEETS, WorkSheets.class)
                .expectedResponseCode(HttpStatus.SC_OK);
        return HTTPRequest.build(requestEntity).get();
    }
}
