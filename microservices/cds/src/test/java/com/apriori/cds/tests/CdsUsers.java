package com.apriori.cds.tests;

import com.apriori.cds.entity.response.User;
import com.apriori.cds.entity.response.Users;
import com.apriori.cds.tests.utils.CdsTestUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.constants.CommonConstants;
import com.apriori.utils.http.builder.dao.ServiceConnector;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class CdsUsers extends CdsTestUtil {
    private String url;

    @Before
    public void setServiceUrl() {
        url = ServiceConnector.getServiceUrl();
    }


    @Test
    @TestRail(testCaseId = "3697")
    @Description("API returns a list of all the available users in the CDS DB")
    public void getUsers() {
        url = String.format(url, "users");
        ResponseWrapper<Users> response = getCommonRequest(url, true, Users.class);

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, response.getStatusCode());
        validateUsers(response.getResponseEntity());
    }


    @Test
    @TestRail(testCaseId = "3698")
    @Description("API returns a user's information based on the supplied identity")
    public void getUserById() {
        url = String.format(url,
                String.format("users/%s", CommonConstants.getCdsIdentityUser()));
        ResponseWrapper<User> response = getCommonRequest(url, true, User.class);

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, response.getStatusCode());
        validateUser(response.getResponseEntity());
    }


    /*
     * User Validation
     */
    private void validateUsers(Users usersResponse) {
        Object[] users = usersResponse.getResponse().getItems().toArray();
        Arrays.stream(users)
                .forEach(this::validate);
    }

    private void validateUser(User userResponse) {
        User user = userResponse.getResponse();
        validate(user);
    }

    private void validate(Object userObj) {
        EmailValidator validator = EmailValidator.getInstance();
        User user = (User) userObj;
        Assert.assertTrue(user.getIdentity().matches("^[a-zA-Z0-9]+$"));
        Assert.assertTrue(validator.isValid(user.getEmail()));
    }

}
