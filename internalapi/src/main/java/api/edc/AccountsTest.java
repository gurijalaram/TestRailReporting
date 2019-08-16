package main.java.api.edc;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import main.java.api.edc.util.UserDataEDC;
import main.java.api.edc.util.UserTestDataUtil;
import main.java.http.builder.common.response.common.AccountsStatus;
import main.java.http.builder.service.HTTPRequest;
import main.java.http.enums.common.api.BillOfMaterialsAPIEnum;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;


public class AccountsTest {

    private static UserTestDataUtil userTestDataUtil;
    private static UserDataEDC userData;

    @BeforeClass
    public static void setUp() {
        userTestDataUtil = new UserTestDataUtil();
        userData = userTestDataUtil.initBillOfMaterials();
    }

    @AfterClass
    public static void clearTestData(){
        userTestDataUtil.clearTestData(userData);
    }

    @Test
    @Description("Test account status")
    @Severity(SeverityLevel.NORMAL)
    public void testAccountsStatus() {
        new HTTPRequest().unauthorized()
                .customizeRequest()
                .setHeaders(userData.getAuthorizationHeaders())
                .setEndpoint(BillOfMaterialsAPIEnum.GET_ACCOUNTS_STATUS)
                .setReturnType(AccountsStatus.class)
                .commitChanges()
                .connect()
                .get();
    }
}
