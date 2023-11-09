package com.apriori.bcm.api.utils;

import com.apriori.bcm.api.enums.BcmAppAPIEnum;
import com.apriori.bcm.api.models.request.Worksheet;
import com.apriori.bcm.api.models.request.WorksheetRequest;
import com.apriori.bcm.api.models.response.ErrorResponse;
import com.apriori.bcm.api.models.response.WorkSheetResponse;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.AuthUserContextUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.http.utils.TestUtil;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;

@Slf4j
public class BcmUtil extends TestUtil {

    protected static UserCredentials testingUser = UserUtil.getUser("admin");
    protected static String testingApUserContext =  new AuthUserContextUtil().getAuthUserContext(testingUser.getEmail());

    public  ResponseWrapper<WorkSheetResponse> createWorksheet(String name){

        WorksheetRequest body = WorksheetRequest
            .builder()
            .worksheet(Worksheet
                .builder()
                .name(name)
                .build())
            .build();

        final RequestEntity requestEntity =
            RequestEntityUtil.init(BcmAppAPIEnum.WORKSHEETS, WorkSheetResponse.class)
                .body(body)
                .apUserContext(testingApUserContext)
                .expectedResponseCode(HttpStatus.SC_CREATED);
        return HTTPRequest.build(requestEntity).post();
    }

    public  ResponseWrapper<ErrorResponse> createWorksheetAlreadyExists(String name){

        WorksheetRequest body = WorksheetRequest
            .builder()
            .worksheet(Worksheet
                .builder()
                .name(name)
                .build())
            .build();

        final RequestEntity requestEntity =
            RequestEntityUtil.init(BcmAppAPIEnum.WORKSHEETS, ErrorResponse.class)
                .body(body)
                .apUserContext(testingApUserContext)
                .expectedResponseCode(HttpStatus.SC_BAD_REQUEST);
        return HTTPRequest.build(requestEntity).post();
    }
}
