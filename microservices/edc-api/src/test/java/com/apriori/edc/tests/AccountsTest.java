package com.apriori.edc.tests;

import static org.junit.Assert.assertEquals;

import com.apriori.apibase.services.response.objects.AccountStatus;
import com.apriori.apibase.services.response.objects.Accounts;
import com.apriori.apibase.services.response.objects.AccountsStatusWrapper;
import com.apriori.apibase.utils.TestUtil;
import com.apriori.edc.tests.util.UserDataEDC;
import com.apriori.edc.tests.util.UserTestDataUtil;
import com.apriori.edc.utils.Constants;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.enums.common.api.AccountEndpointEnum;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.users.UserUtil;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AccountsTest extends TestUtil {

    private String identity = "";
    private String token = "";
    private UserDataEDC userData = new UserDataEDC(UserUtil.getUser());


    @Before
    public void initUser() {
        Constants.getDefaultUrl();
        token = new UserTestDataUtil().initToken(userData.getUserCredentials());
        RequestEntityUtil.useTokenForRequests(token);

        userData.setIdentity(createAndActivateNewAccount(userData.getUsername(), "TestSecretForTesting").getIdentity());
    }

    @After
    public void deleteUserIfFailed() {
        if (identity != null && !identity.isEmpty()) {
            activateDefaultUserAndDeleteAnotherByIdentity(identity);
        }
    }

    @Test
    @Description("Test get accounts")
    @Severity(SeverityLevel.NORMAL)
    public void testGetAccounts() {
        RequestEntity requestEntity = RequestEntityUtil.init(AccountEndpointEnum.GET_ACCOUNTS, Accounts.class);

        ResponseWrapper<Accounts> accountsResponseWrapper = HTTPRequest.build(requestEntity).get();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, accountsResponseWrapper.getStatusCode());
    }

    @Test
    @Issue("AP-56541")
    @Description("Test update account")
    @Severity(SeverityLevel.NORMAL)
    public void testUpdateAccount() {
        final AccountStatus accountStatus = createAndActivateNewAccount("TestNameForUpd", "TestSecretForUpd");
        final String newName = "ACCOUNT UPDATE VIA AUTOMATION";

        identity = accountStatus.getIdentity();
        final AccountStatus updatedAccount = updateAccount(identity,
            accountStatus.setName(newName)
        );

        assertEquals("The user name, should be updated",
            updatedAccount.getName(),
            newName);

        activateDefaultUserAndDeleteAnotherByIdentity(identity);
        identity = "";
    }

    @Test
    @Description("Test get account status by identity")
    @Severity(SeverityLevel.NORMAL)
    public void testGetAccountByIdentity() {
        final AccountStatus receivedAccountsStatus = getAccountByIdentity(userData.getIdentity());

        assertEquals("Search identity and received identity, should be the same",
            userData.getIdentity(),
            receivedAccountsStatus.getIdentity());
    }

    @Test
    @Description("Test create, activate and delete new account")
    @Severity(SeverityLevel.NORMAL)
    public void testCreateActivateAndDeleteNewAccount() {
        AccountStatus createdAccount = createAndActivateNewAccount("TestNameForActive", "ActiveSecret");
        identity = createdAccount.getIdentity();

        activateDefaultUserAndDeleteAnotherByIdentity(identity);
        identity = "";
    }

    @Test
    @Description("Test get account status")
    @Severity(SeverityLevel.NORMAL)
    public void testGetAccountActive() {
        getActiveAccount();
    }

    private AccountStatus createAndActivateNewAccount(String name, String secret) {
        return activateAccount(
            createNewAccount(name, secret).getIdentity()
        );
    }

    private void activateDefaultUserAndDeleteAnotherByIdentity(String identity) {
        activateAccount(userData.getIdentity());
        deleteAccountByIdentity(identity);
    }

    private AccountStatus getActiveAccount() {
        RequestEntity requestEntity = RequestEntityUtil.init(
            AccountEndpointEnum.GET_ACTIVE_USER, AccountsStatusWrapper.class);

        ResponseWrapper<AccountsStatusWrapper> responseWrapper = HTTPRequest.build(requestEntity).get();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, responseWrapper.getStatusCode());

        return responseWrapper.getResponseEntity().getAccountStatus();
    }

    private AccountStatus getAccountByIdentity(String identity) {
        RequestEntity requestEntity = RequestEntityUtil.init(
            AccountEndpointEnum.GET_ACCOUNTS_BY_IDENTITY, AccountsStatusWrapper.class)
            .inlineVariables(identity);

        ResponseWrapper<AccountsStatusWrapper> responseWrapper = HTTPRequest.build(requestEntity).get();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, responseWrapper.getStatusCode());

        return responseWrapper.getResponseEntity().getAccountStatus();
    }

    private AccountStatus activateAccount(String identity) {
        RequestEntity requestEntity = RequestEntityUtil.init(AccountEndpointEnum.ACTIVATE_ACCOUNTS_BY_IDENTITY, AccountsStatusWrapper.class)
            .inlineVariables(identity);

        ResponseWrapper<AccountsStatusWrapper> responseWrapper = HTTPRequest.build(requestEntity).post();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, responseWrapper.getStatusCode());

        return responseWrapper.getResponseEntity().getAccountStatus();
    }

    private AccountStatus updateAccount(final String identity, final AccountStatus accountStatus) {
        final RequestEntity requestEntity = RequestEntityUtil.init(
            AccountEndpointEnum.UPDATE_ACCOUNTS_BY_IDENTITY, AccountsStatusWrapper.class)
            .inlineVariables(identity)
            .body(accountStatus);

        ResponseWrapper<AccountsStatusWrapper> responseWrapper = HTTPRequest.build(requestEntity).patch();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, responseWrapper.getStatusCode());

        return responseWrapper.getResponseEntity().getAccountStatus();
    }

    private AccountStatus createNewAccount(String name, String secret) {
        final AccountStatus accountStatus = new AccountStatus()
            .setAccountId("Apriori")
            .setType("Silicon Expert")
            .setName(name)
            .setAccountSecret(secret);

        final RequestEntity requestEntity = RequestEntityUtil.init(
            AccountEndpointEnum.POST_ACCOUNTS, AccountsStatusWrapper.class)
            .body(accountStatus);

        ResponseWrapper<AccountsStatusWrapper> responseWrapper = HTTPRequest.build(requestEntity).post();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_CREATED, responseWrapper.getStatusCode());

        return responseWrapper.getResponseEntity()
            .getAccountStatus();
    }

    private void deleteAccountByIdentity(String identity) {
        final RequestEntity requestEntity = RequestEntityUtil.init(
            AccountEndpointEnum.DELETE_ACCOUNTS_BY_IDENTITY, null)
            .inlineVariables(identity);

        ResponseWrapper<AccountsStatusWrapper> responseWrapper = HTTPRequest.build(requestEntity).delete();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_NO_CONTENT, responseWrapper.getStatusCode());
    }
}
