package com.apriori.edc.tests.util;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.edc.utils.Constants;
import com.apriori.utils.http.utils.RequestEntityUtil;

import org.junit.AfterClass;
import org.junit.BeforeClass;

public class EdcTestUtil extends TestUtil {

    protected static UserTestDataUtil userTestDataUtil;
    protected static UserDataEDC userData;
    protected static String token;

    @BeforeClass
    public static void setUp() {
        Constants.getDefaultUrl();
        userTestDataUtil = new UserTestDataUtil();
        userData = userTestDataUtil.initBillOfMaterials();
        token = userTestDataUtil.getToken();

        RequestEntityUtil.useTokenForRequests(token);
    }

    @AfterClass
    public static void clearTestData() {
        userTestDataUtil.clearTestData(userData);
    }
}
