package main.java.api;


import main.java.http.builder.common.response.common.AccountsStatus;
import main.java.http.builder.service.HTTPRequest;
import main.java.http.enums.EndpointType;
import main.java.http.enums.common.AuthEndpointEnum;
import main.java.http.enums.common.ExternalEndpointEnum;
import main.java.http.enums.common.InternalEndpointEnum;
import main.java.http.enums.common.TemporaryAPIEnum;
import org.junit.Test;

public class AccountsTest {


    @Test
    public void testAccountsStatus() {
        new HTTPRequest().unauthorized()
                .customizeRequest()
                .setEndpoint(TemporaryAPIEnum.GET_ACCOUNTS_STATUS)
                .setReturnType(AccountsStatus.class)
                .setFollowRedirection(false)
                .commitChanges()
                .connect()
                .get();
    }
}
