package com.apriori.sds.tests;

import com.apriori.apibase.utils.APIAuthentication;
import com.apriori.apibase.utils.CommonRequestUtil;
import com.apriori.sds.entity.enums.SDSAPIEnum;
import com.apriori.sds.tests.util.SDSTestUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Test;

// TODO z: issue with requests
public class ConnectionsTest extends SDSTestUtil {

    @Test
    @TestRail(testCaseId = {"6936"})
    @Description("Find connections for a customer matching a specified query.")
    public void getConnections() {
        ResponseWrapper response =
            new CommonRequestUtil().getCommonRequestWithInlineVariables(SDSAPIEnum.GET_CONNECTIONS, null,
                new APIAuthentication().initAuthorizationHeaderContent(token)
            );

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, response.getStatusCode());
    }
}
