package com.apriori.internalapi.edc;

import com.apriori.apibase.http.builder.common.response.common.AccountStatus;
import com.apriori.apibase.http.builder.common.response.common.Accounts;
import com.apriori.apibase.http.builder.common.response.common.AccountsStatusWrapper;
import com.apriori.apibase.http.builder.service.HTTPRequest;
import com.apriori.apibase.http.enums.common.api.AccountEndpointEnum;
import com.apriori.internalapi.edc.util.UserDataEDC;
import com.apriori.internalapi.edc.util.UserTestDataUtil;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AccountsTest {

    private static final Logger logger = LoggerFactory.getLogger(AccountStatus.class);
    private static UserDataEDC userData;

    private String identity = "";

    @BeforeClass
    public static void setUp() {
        userData = new UserTestDataUtil().initEmptyUser();

        final AccountStatus accountStatus = getActiveAccount();

        userData.setIdentity(accountStatus.getIdentity());
        userData.setAccountId(accountStatus.getAccountId());
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
        new HTTPRequest().unauthorized()
            .customizeRequest()
            .setHeaders(userData.getAuthorizationHeaders())
            .setEndpoint(AccountEndpointEnum.GET_ACCOUNTS)
            .setReturnType(Accounts.class)
            .commitChanges()
            .connect()
            .get();
    }

    @Test
    @Issue("AP-56541")
    @Description("Test update account")
    @Severity(SeverityLevel.NORMAL)
    public void testUpdateAccount() {
        final AccountStatus accountStatus = createAndActivateNewAccount("TestNameForUpd", "TestSecretForUpd");
        final String newName = "ACCOUNT UPDATE VIA AUTOMATION";

        identity = accountStatus.getIdentity();
        updateAccount(identity,
            accountStatus.setName(newName)
        );

        Assert.assertEquals("The user name, should be updated",
            getActiveAccount().getName(),
            newName);

        activateDefaultUserAndDeleteAnotherByIdentity(identity);
        identity = "";
    }

    @Test
    @Description("Test get account status by identity")
    @Severity(SeverityLevel.NORMAL)
    public void testGetAccountByIdentity() {
        final AccountStatus receivedAccountsStatus = getAccountByIdentity(userData.getIdentity());

        Assert.assertEquals("Search identity and received identity, should be the same",
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

    private static AccountStatus getActiveAccount() {
        final AccountsStatusWrapper accountsStatusWrapper = (AccountsStatusWrapper) new HTTPRequest().unauthorized()
            .customizeRequest()
            .setHeaders(userData.getAuthorizationHeaders())
            .setEndpoint(AccountEndpointEnum.GET_ACTIVE_USER)
            .setReturnType(AccountsStatusWrapper.class)
            .commitChanges()
            .connect()
            .get();

        return accountsStatusWrapper.getAccountStatus();
    }

    private AccountStatus getAccountByIdentity(String identity) {
        final AccountsStatusWrapper accountsStatusWrapper = (AccountsStatusWrapper) new HTTPRequest().unauthorized()
            .customizeRequest()
            .setHeaders(userData.getAuthorizationHeaders())
            .setEndpoint(AccountEndpointEnum.GET_ACCOUNTS_BY_IDENTITY)
            .setInlineVariables(identity)
            .setReturnType(AccountsStatusWrapper.class)
            .commitChanges()
            .connect()
            .get();

        return accountsStatusWrapper.getAccountStatus();
    }

    private AccountStatus activateAccount(String identity) {
        final AccountsStatusWrapper accountsStatusWrapper = (AccountsStatusWrapper) new HTTPRequest().unauthorized()
            .customizeRequest()
            .setHeaders(userData.getAuthorizationHeaders())
            .setEndpoint(AccountEndpointEnum.ACTIVATE_ACCOUNTS_BY_IDENTITY)
            .setInlineVariables(identity)
            .setReturnType(AccountsStatusWrapper.class)
            .setStatusCode(HttpStatus.SC_OK)
            .commitChanges()
            .connect()
            .post();

        return accountsStatusWrapper.getAccountStatus();
    }

    private AccountStatus updateAccount(final String identity, final AccountStatus accountStatus) {
        final AccountsStatusWrapper accountsStatusWrapper = (AccountsStatusWrapper) new HTTPRequest().unauthorized()
            .customizeRequest()
            .setHeaders(userData.getAuthorizationHeaders())
            .setEndpoint(AccountEndpointEnum.UPDATE_ACCOUNTS_BY_IDENTITY)
            .setInlineVariables(identity)
            .setBody(accountStatus)
            .setReturnType(AccountsStatusWrapper.class)
            .commitChanges()
            .connect()
            .patch();

        return accountsStatusWrapper.getAccountStatus();
    }

    private AccountStatus createNewAccount(String name, String secret) {
        final AccountsStatusWrapper accountsStatusWrapper = (AccountsStatusWrapper) new HTTPRequest().unauthorized()
            .customizeRequest()
            .setHeaders(userData.getAuthorizationHeaders())
            .setEndpoint(AccountEndpointEnum.POST_ACCOUNTS)
            .setBody(new AccountStatus()
                .setAccountId("Apriori")
                .setType("Silicon Expert")
                .setName(name)
                .setAccountSecret(secret))
            .setReturnType(AccountsStatusWrapper.class)
            .setStatusCode(HttpStatus.SC_CREATED)
            .commitChanges()
            .connect()
            .post();

        return accountsStatusWrapper.getAccountStatus();
    }

    private void deleteAccountByIdentity(String identity) {
        new HTTPRequest().unauthorized()
            .customizeRequest()
            .setHeaders(userData.getAuthorizationHeaders())
            .setEndpoint(AccountEndpointEnum.DELETE_ACCOUNTS_BY_IDENTITY)
            .setInlineVariables(identity)
            .setStatusCode(HttpStatus.SC_NO_CONTENT)
            .commitChanges()
            .connect()
            .delete();
    }
}
