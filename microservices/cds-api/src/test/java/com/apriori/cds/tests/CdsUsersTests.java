package com.apriori.cds.tests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.objects.response.User;
import com.apriori.cds.objects.response.Users;
import com.apriori.cds.objects.response.credentials.CredentialsItems;
import com.apriori.cds.tests.utils.CdsTestUtil;
import com.apriori.cds.utils.Constants;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

public class CdsUsersTests {
    private CdsTestUtil cdsTestUtil = new CdsTestUtil();

    @Test
    @TestRail(testCaseId = {"3697"})
    @Description("API returns a list of all the available users in the CDS DB")
    public void getUsers() {
        ResponseWrapper<Users> response = cdsTestUtil.getCommonRequest(CDSAPIEnum.GET_USERS, Users.class);

        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(response.getResponseEntity().getResponse().getTotalItemCount(), is(greaterThanOrEqualTo(1)));
        assertThat(response.getResponseEntity().getResponse().getItems().get(0).getUserType(), is(not(emptyString())));
    }


    @Test
    @TestRail(testCaseId = {"3698"})
    @Description("API returns a user's information based on the supplied identity")
    public void getUserById() {
        ResponseWrapper<Users> responseWrapper = cdsTestUtil.getCommonRequest(CDSAPIEnum.GET_USERS, Users.class);

        String userIdentity = responseWrapper.getResponseEntity().getResponse().getItems().get(0).getIdentity();

        ResponseWrapper<User> response = cdsTestUtil.getCommonRequest(CDSAPIEnum.GET_USER_BY_ID, User.class, userIdentity);

        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(response.getResponseEntity().getCustomerIdentity(), is(not(emptyString())));
        assertThat(response.getResponseEntity().getIdentity(), is(equalTo(userIdentity)));
    }

    @Test
    @TestRail(testCaseId = {"5971"})
    @Description("API returns a user's credentials based on the supplied identity")
    public void getUsersCredentials() {
        ResponseWrapper<Users> responseWrapper = cdsTestUtil.getCommonRequest(CDSAPIEnum.GET_USERS, Users.class);

        String userIdentity = responseWrapper.getResponseEntity().getResponse().getItems().get(0).getIdentity();

        ResponseWrapper<CredentialsItems> response = cdsTestUtil.getCommonRequest(CDSAPIEnum.GET_USER_CREDENTIALS_BY_ID, CredentialsItems.class, userIdentity);

        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(response.getResponseEntity().getResponse().getPasswordHash(), is(not(emptyString())));
    }
}
