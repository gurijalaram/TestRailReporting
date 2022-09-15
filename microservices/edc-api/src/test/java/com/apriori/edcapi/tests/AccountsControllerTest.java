package com.apriori.edcapi.tests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.core.Is.is;

import com.apriori.edcapi.entity.response.accounts.AccountsResponse;
import com.apriori.edcapi.utils.AccountsUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.authorization.AuthorizationUtil;
import com.apriori.utils.http.utils.RequestEntityUtil;

import io.qameta.allure.Description;
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
}
