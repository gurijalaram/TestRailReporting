package com.apriori.edcapi.tests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.core.Is.is;

import com.apriori.edcapi.entity.response.accounts.AccountsResponse;
import com.apriori.edcapi.utils.AccountsUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.authorization.AuthorizationUtil;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class AccountsControllerTest extends AccountsUtil {

    private static String identity;

    @Before
    public void setUp() {
        RequestEntityUtil.useTokenForRequests(new AuthorizationUtil().getTokenAsString());
        identity = postCreateNewAccount().getResponseEntity().getIdentity();
    }

    @AfterClass
    public static void deleteTestData() {
        if (identity != null) {
            deleteAccountByIdentity(identity);
        }
    }

    @Test
    @TestRail(testCaseId = "1496")
    @Description("GET List the accounts matching a specified query.")
    public void testGetAllAccounts() {
        List<AccountsResponse> allAccounts = getAllAccounts();
        assertThat(allAccounts.size(), is(greaterThan(0)));
    }

    @Test
    @TestRail(testCaseId = "1492")
    @Description("GET the current representation of an account.")
    public void testGetAccountByIdentity() {
        ResponseWrapper<AccountsResponse> accountByIdentity = getAccountByIdentity(identity);
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, accountByIdentity.getStatusCode());
    }

    @Test
    @TestRail(testCaseId = "1497")
    @Description("POST Add a new account.")
    public void testCreateNewAccount() {
        ResponseWrapper<AccountsResponse> postResponse = postCreateNewAccount();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_CREATED, postResponse.getStatusCode());
        String postResponseIdentity = postResponse.getResponseEntity().getIdentity();

        ResponseWrapper<AccountsResponse> accountByIdentity = getAccountByIdentity(postResponseIdentity);
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, accountByIdentity.getStatusCode());
    }

    @Test
    @TestRail(testCaseId = "1493")
    @Description("DELETE an account.")
    public void testDeleteAccountByIdentity() {
        ResponseWrapper<AccountsResponse> postResponse = postCreateNewAccount();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_CREATED, postResponse.getStatusCode());
        String identity = postResponse.getResponseEntity().getIdentity();

        ResponseWrapper<AccountsResponse> accountByIdentity = getAccountByIdentity(identity);
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, accountByIdentity.getStatusCode());

        ResponseWrapper<AccountsResponse> deleteAccountByIdentity = deleteAccountByIdentity(identity);
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_NO_CONTENT, deleteAccountByIdentity.getStatusCode());

        ResponseWrapper<AccountsResponse> deletedAccountIdentity = getAccountByIdentity(identity, null);
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_NOT_FOUND, deletedAccountIdentity.getStatusCode());
    }

    @Test
    @TestRail(testCaseId = "1494")
    @Description("PATCH Update an account.")
    public void testPatchUpdateAccountByIdentity() {
        ResponseWrapper<AccountsResponse> accountByIdentity = getAccountByIdentity(identity);
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, accountByIdentity.getStatusCode());

        ResponseWrapper<AccountsResponse> updateAccountByIdentity = patchUpdateAccountByIdentity(identity);
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, updateAccountByIdentity.getStatusCode());
    }

    @Test
    @TestRail(testCaseId = "1495")
    @Description("POST Activate an account.")
    public void testPostActivateAnAccount() {
        ResponseWrapper<AccountsResponse> accountByIdentity = getAccountByIdentity(identity);
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, accountByIdentity.getStatusCode());

        ResponseWrapper<AccountsResponse> activateAnAccountByIdentity = postActivateAnAccount(identity);
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, activateAnAccountByIdentity.getStatusCode());

        assertThat(activateAnAccountByIdentity.getResponseEntity().getIsActive(), is(equalTo(true)));
    }

    @Test
    @TestRail(testCaseId = "15453")
    @Description("POST Refresh license for specified account")
    public void testPostRefreshLicense() {
        ResponseWrapper<AccountsResponse> accountByIdentity = getAccountByIdentity(identity);
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, accountByIdentity.getStatusCode());

        ResponseWrapper<AccountsResponse> refreshLicenseResponse = postRefreshLicense(identity);
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, refreshLicenseResponse.getStatusCode());
    }
}
