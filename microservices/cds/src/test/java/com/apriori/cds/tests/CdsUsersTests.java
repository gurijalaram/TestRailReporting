package com.apriori.cds.tests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

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

public class CdsUsersTests extends CdsTestUtil {
    private String url;

    @Before
    public void setServiceUrl() {
        url = Constants.getServiceUrl();
    }


    @Test
    @TestRail(testCaseId = "3697")
    @Description("API returns a list of all the available users in the CDS DB")
    public void getUsers() {
        url = String.format(url, "users");
        ResponseWrapper<Users> response = get(url, Users.class);

        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(response.getResponseEntity().getResponse().getTotalItemCount(), is(greaterThanOrEqualTo(1)));
        assertThat(response.getResponseEntity().getResponse().getItems().get(0).getUserType(), is(not(emptyString())));
    }


    @Test
    @TestRail(testCaseId = "3698")
    @Description("API returns a user's information based on the supplied identity")
    public void getUserById() {
        String usersUrl = String.format(url, "users");
        ResponseWrapper<Users> responseWrapper = get(usersUrl, Users.class);

        String userIdentity = responseWrapper.getResponseEntity().getResponse().getItems().get(0).getIdentity();

        String identityUrl = String.format(url, String.format("users/%s", userIdentity));
        ResponseWrapper<User> response = get(identityUrl, User.class);

        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(response.getResponseEntity().getResponse().getCustomerIdentity(), is(not(emptyString())));
        assertThat(response.getResponseEntity().getResponse().getIdentity(), is(equalTo(userIdentity)));
    }

    @Test
    @TestRail(testCaseId = "5971")
    @Description("API returns a user's credentials based on the supplied identity")
    public void getUsersCredentials() {
        String usersUrl = String.format(url, "users");
        ResponseWrapper<Users> responseWrapper = get(usersUrl, Users.class);

        String userIdentity = responseWrapper.getResponseEntity().getResponse().getItems().get(0).getIdentity();

        String credentialsUrl = String.format(url, String.format("users/%s/credentials", userIdentity));
        ResponseWrapper<CredentialsItems> response = get(credentialsUrl, CredentialsItems.class);

        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(response.getResponseEntity().getResponse().getPasswordHash(), is(equalTo("e68b4ec50e5f9996af36b0e5dc6be6267fd545ad")));
    }
}
