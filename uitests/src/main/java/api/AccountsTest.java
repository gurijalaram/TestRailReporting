package main.java.api;


import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import main.java.http.builder.common.response.common.AccountsStatus;
import main.java.http.builder.service.HTTPRequest;
import main.java.http.enums.common.api.BillOfMaterialsAPIEnum;
import org.junit.Test;

public class AccountsTest {


    @Test
    @Description("Test account status")
    @Severity(SeverityLevel.NORMAL)
    public void testAccountsStatus() {
        new HTTPRequest().unauthorized()
                .customizeRequest()
                .setEndpoint(BillOfMaterialsAPIEnum.GET_ACCOUNTS_STATUS)
                .setReturnType(AccountsStatus.class)
                .commitChanges()
                .connect()
                .get();
    }
}
