package com.apriori.edc.tests.util;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.ats.utils.JwtTokenUtil;
import com.apriori.edc.entity.enums.EDCAPIEnum;
import com.apriori.edc.tests.BillOfMaterialsTest;
import com.apriori.utils.http2.builder.common.entity.RequestEntity;
import com.apriori.utils.http2.builder.service.HTTP2Request;
import com.apriori.utils.http2.utils.RequestEntityUtil;

import org.apache.http.HttpStatus;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.util.HashSet;
import java.util.Set;

public class EdcTestUtil extends TestUtil {

    protected static UserTestDataUtil userTestDataUtil;
    protected static UserDataEDC userData;

    protected static final Set<String> billOfMaterialsIdsToDelete = new HashSet<>();

    @BeforeClass
    public static void setUp() {

        RequestEntityUtil.useTokenForRequests(new JwtTokenUtil().retrieveJwtToken());
    }

    @AfterClass
    public static void deleteTestingData() {
        billOfMaterialsIdsToDelete.forEach(BillOfMaterialsTest::deleteBillOfMaterialById);
    }

    public static void deleteBillOfMaterialById(final String identity) {
        RequestEntity requestEntity =
            RequestEntityUtil.init(EDCAPIEnum.DELETE_BILL_OF_MATERIALS_BY_IDENTITY, null)
                .inlineVariables(identity);
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_NO_CONTENT, HTTP2Request.build(requestEntity).delete().getStatusCode());
    }
}
