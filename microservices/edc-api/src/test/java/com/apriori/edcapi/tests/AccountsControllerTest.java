package com.apriori.edcapi.tests;

import static org.hamcrest.MatcherAssert.assertThat;
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
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class AccountsControllerTest extends AccountsUtil {

    @Before
    public void setUp() {
        RequestEntityUtil.useTokenForRequests(new AuthorizationUtil().getTokenAsString());
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
        ResponseWrapper<AccountsResponse> accountByIdentity = getAccountByIdentity(getAllAccounts().get(0).getIdentity());
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, accountByIdentity.getStatusCode());
    }

    @Test
    @TestRail(testCaseId = "1493")
    @Description("DELETE an account.")
    public void testDeleteAccountByIdentity() {
        AccountsResponse accountsResponse = getAllAccounts().get(1);
        String identity = accountsResponse.getIdentity();

        ResponseWrapper<AccountsResponse> deleteAccountByIdentity = deleteAccountByIdentity(identity);
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_NO_CONTENT, deleteAccountByIdentity.getStatusCode());

        ResponseWrapper<AccountsResponse> deletedAccountIdentity = getAccountByIdentity(identity, null);
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_NOT_FOUND, deletedAccountIdentity.getStatusCode());
    }
}
