package com.apriori.bcm.api.utils;

import com.apriori.bcm.api.enums.BcmAppAPIEnum;
import com.apriori.bcm.api.models.request.Worksheet;
import com.apriori.bcm.api.models.request.WorksheetRequest;
import com.apriori.bcm.api.models.response.ErrorResponse;
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

    public ResponseWrapper<WorkSheets> getWorksheets() {
        final RequestEntity requestEntity =
            requestEntityUtil.init(BcmAppAPIEnum.WORKSHEETS, WorkSheets.class)
                .expectedResponseCode(HttpStatus.SC_OK);
        return HTTPRequest.build(requestEntity).get();
    }
}
