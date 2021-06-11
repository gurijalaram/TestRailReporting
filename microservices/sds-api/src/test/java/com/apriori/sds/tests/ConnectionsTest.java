package com.apriori.sds.tests;

import com.apriori.sds.entity.enums.SDSAPIEnum;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.http2.builder.common.entity.RequestEntity;
import com.apriori.utils.http2.builder.service.HTTP2Request;
import com.apriori.utils.http2.utils.RequestEntityUtil;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Test;
import util.SDSTestUtil;

public class ConnectionsTest extends SDSTestUtil {

    @Test
    @TestRail(testCaseId = {"6936"})
    @Description("Find connections for a customer matching a specified query.")
    public void getConnections() {
        final RequestEntity requestEntity =
            RequestEntityUtil.initWithApUserContext(SDSAPIEnum.GET_CONNECTIONS, null);

        ResponseWrapper<Void> response = HTTP2Request.build(requestEntity).get();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, response.getStatusCode());
    }
}
