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
import org.assertj.core.api.SoftAssertions;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

public class AccountsControllerTest extends AccountsUtil {

    private static String identity;

    @Before
    public void setUp() {
        RequestEntityUtil.useTokenForRequests(new AuthorizationUtil().getTokenAsString());
        if (identity == null) {
            identity = postCreateNewAccount().getResponseEntity().getIdentity();
        }
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
    @TestRail(testCaseId = {"1492", "1497"})
    @Description("GET the current representation of an account.")
    public void testGetAccountByIdentity() {
        getAccountByIdentity(identity);
    }

    @Test
    @TestRail(testCaseId = "1493")
    @Description("DELETE an account.")
    public void testDeleteAccountByIdentity() {
        String identity = postCreateNewAccount().getResponseEntity().getIdentity();

        getAccountByIdentity(identity);

        deleteAccountByIdentity(identity);

        getAccountByIdentity(identity, null, HttpStatus.SC_NOT_FOUND);
    }

    @Test
    @TestRail(testCaseId = "1494")
    @Description("PATCH Update an account.")
    public void testPatchUpdateAccountByIdentity() {
        getAccountByIdentity(identity);

        patchUpdateAccountByIdentity(identity);
    }

    @Ignore
    @Test
    @TestRail(testCaseId = "15453")
    @Description("POST Refresh license for specified account")
    public void testPostRefreshLicense() {
        getAccountByIdentity(identity);

        postRefreshLicense(identity);
    }

    @Test
    @TestRail(testCaseId = "1495")
    @Description("POST Activate an account.")
    public void testPostActivateAnAccount() {
        ResponseWrapper<AccountsResponse> originalAccount = getActiveAccount();

        String originalIdentity = originalAccount.getResponseEntity().getIdentity();

        getAccountByIdentity(identity);

        ResponseWrapper<AccountsResponse> activateAnAccountByIdentity = postActivateAnAccount(identity);

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(activateAnAccountByIdentity.getResponseEntity().getIsActive()).isEqualTo(true);

        ResponseWrapper<AccountsResponse> activateOriginalAccount = postActivateAnAccount(originalIdentity);

        softAssertions.assertThat(activateOriginalAccount.getResponseEntity().getIsActive()).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = "1498")
    @Description("GET the current representation of the active account.")
    public void testGetActiveAccount() {
        ResponseWrapper<AccountsResponse> activeAccount = getActiveAccount();

        assertThat(activeAccount.getResponseEntity().getIsActive(), is(equalTo(true)));
    }
}
