package com.apriori.cds.tests;

import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.objects.response.User;
import com.apriori.cds.objects.response.Users;
import com.apriori.cds.objects.response.credentials.CredentialsItems;
import com.apriori.cds.utils.CdsTestUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

public class CdsUsersTests {
    private SoftAssertions soft = new SoftAssertions();
    private CdsTestUtil cdsTestUtil = new CdsTestUtil();

    @Test
    @TestRail(testCaseId = {"3697"})
    @Description("API returns a list of all the available users in the CDS DB")
    public void getUsers() {
        ResponseWrapper<Users> response = cdsTestUtil.getCommonRequest(CDSAPIEnum.USERS, Users.class, HttpStatus.SC_OK);

        soft.assertThat(response.getResponseEntity().getTotalItemCount()).isGreaterThanOrEqualTo(1);
        soft.assertThat(response.getResponseEntity().getItems().get(0).getUserType()).isNotEmpty();
        soft.assertAll();
    }


    @Test
    @TestRail(testCaseId = {"3698"})
    @Description("API returns a user's information based on the supplied identity")
    public void getUserById() {
        ResponseWrapper<Users> responseWrapper = cdsTestUtil.getCommonRequest(CDSAPIEnum.USERS, Users.class, HttpStatus.SC_OK);

        String userIdentity = responseWrapper.getResponseEntity().getItems().get(0).getIdentity();

        ResponseWrapper<User> response = cdsTestUtil.getCommonRequest(CDSAPIEnum.USER_BY_ID, User.class, HttpStatus.SC_OK, userIdentity);

        soft.assertThat(response.getResponseEntity().getCustomerIdentity()).isNotEmpty();
        soft.assertThat(response.getResponseEntity().getIdentity()).isEqualTo(userIdentity);
        soft.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"5971"})
    @Description("API returns a user's credentials based on the supplied identity")
    public void getUsersCredentials() {
        ResponseWrapper<Users> responseWrapper = cdsTestUtil.getCommonRequest(CDSAPIEnum.USERS, Users.class, HttpStatus.SC_OK);

        String userIdentity = responseWrapper.getResponseEntity().getItems().get(0).getIdentity();

        ResponseWrapper<CredentialsItems> response = cdsTestUtil.getCommonRequest(CDSAPIEnum.USER_CREDENTIALS_BY_ID, CredentialsItems.class, HttpStatus.SC_OK, userIdentity);

        soft.assertThat(response.getResponseEntity().getPasswordHash()).isNotEmpty();
        soft.assertAll();
    }
}
