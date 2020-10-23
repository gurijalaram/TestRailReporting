package com.apriori.edc.tests;

import static org.junit.Assert.assertEquals;

import com.apriori.apibase.services.response.objects.AccountStatus;
import com.apriori.apibase.services.response.objects.Accounts;
import com.apriori.apibase.services.response.objects.AccountsStatusWrapper;
import com.apriori.apibase.utils.TestUtil;
import com.apriori.edc.tests.util.UserDataEDC;
import com.apriori.edc.tests.util.UserTestDataUtil;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.dao.GenericRequestUtil;
import com.apriori.utils.http.builder.service.RequestAreaUiAuth;
import com.apriori.utils.http.enums.common.api.AccountEndpointEnum;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.users.UserCredentials;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;

import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccountsTest extends TestUtil {

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

        ResponseWrapper<Accounts> accountsResponseWrapper = GenericRequestUtil.get(
                RequestEntity.init(AccountEndpointEnum.GET_ACCOUNTS, UserCredentials.init(userData.getUsername(), userData.getPassword()), Accounts.class),
                new RequestAreaUiAuth());

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
        updateAccount(identity,
                accountStatus.setName(newName)
        );

        assertEquals("The user name, should be updated",
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

    private static AccountStatus getActiveAccount() {

        RequestEntity requestEntity = RequestEntity.init(
                AccountEndpointEnum.GET_ACTIVE_USER, userData.getUserCredentials(), AccountsStatusWrapper.class)
                .setStatusCode(HttpStatus.SC_OK);

        return ((AccountsStatusWrapper) GenericRequestUtil.get(requestEntity, new RequestAreaUiAuth()).getResponseEntity()).getAccountStatus();
    }

    private AccountStatus getAccountByIdentity(String identity) {

        final RequestEntity requestEntity = RequestEntity.init(
                AccountEndpointEnum.GET_ACCOUNTS_BY_IDENTITY,
                userData.getUserCredentials(),
                AccountsStatusWrapper.class)
                .setInlineVariables(identity)
                .setStatusCode(HttpStatus.SC_OK);

        return ((AccountsStatusWrapper) GenericRequestUtil.get(requestEntity, new RequestAreaUiAuth())
                .getResponseEntity()).getAccountStatus();
    }

    private AccountStatus activateAccount(String identity) {
        RequestEntity requestEntity = RequestEntity.init(
                AccountEndpointEnum.ACTIVATE_ACCOUNTS_BY_IDENTITY, userData.getUserCredentials(), AccountsStatusWrapper.class)
                .setInlineVariables(identity)
                .setStatusCode(HttpStatus.SC_OK);

        return ((AccountsStatusWrapper) GenericRequestUtil.post(requestEntity, new RequestAreaUiAuth()).getResponseEntity()).getAccountStatus();
    }

    private AccountStatus updateAccount(final String identity, final AccountStatus accountStatus) {

        RequestEntity requestEntity = RequestEntity.init(
                AccountEndpointEnum.UPDATE_ACCOUNTS_BY_IDENTITY, userData.getUserCredentials(), AccountsStatusWrapper.class)
                .setStatusCode(HttpStatus.SC_OK)
                .setInlineVariables(identity)
                .setBody(accountStatus);

        return ((AccountsStatusWrapper) GenericRequestUtil.patch(requestEntity, new RequestAreaUiAuth())
                .getResponseEntity()).getAccountStatus();
    }

    private AccountStatus createNewAccount(String name, String secret) {

        final AccountStatus accountStatus = new AccountStatus()
                .setAccountId("Apriori")
                .setType("Silicon Expert")
                .setName(name)
                .setAccountSecret(secret);

        final RequestEntity requestEntity = RequestEntity.init(
                AccountEndpointEnum.POST_ACCOUNTS, userData.getUserCredentials(), AccountsStatusWrapper.class)
                .setBody(accountStatus)
                .setStatusCode(HttpStatus.SC_CREATED);

        return ((AccountsStatusWrapper) GenericRequestUtil.post(requestEntity, new RequestAreaUiAuth())
                .getResponseEntity()).getAccountStatus();
    }

    private void deleteAccountByIdentity(String identity) {

        RequestEntity requestEntity = RequestEntity.init(
                AccountEndpointEnum.DELETE_ACCOUNTS_BY_IDENTITY, userData.getUserCredentials(), null)
                .setInlineVariables(identity)
                .setStatusCode(HttpStatus.SC_NO_CONTENT);

        GenericRequestUtil.delete(requestEntity, new RequestAreaUiAuth());
    }
}
