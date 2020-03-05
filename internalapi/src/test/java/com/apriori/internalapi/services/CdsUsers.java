package com.apriori.internalapi.services;

import com.apriori.apibase.http.builder.dao.ServiceConnector;
import com.apriori.apibase.services.objects.User;
import com.apriori.apibase.services.objects.Users;
import com.apriori.utils.TestRail;
import com.apriori.utils.constants.Constants;

import io.qameta.allure.Description;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class CdsUsers {
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
        Users response = (Users) ServiceConnector.getService(url, Users.class);
        validateUsers(response);
    }

    @Test
    @TestRail(testCaseId = "3698")
    @Description("API returns a user's information based on the supplied identity")
    public void getUserById() {
        url = String.format(url,
            String.format("users/%s", Constants.getCdsIdentityUser()));
        User response = (User) ServiceConnector.getService(url, User.class);
        validateUser(response);
    }


    /*
     * User Validation
     */
    private void validateUsers(Users usersResponse) {
        Object[] users = usersResponse.getResponse().getItems().toArray();
        Arrays.stream(users)
            .forEach(u -> validate(u));
    }

    private void validateUser(User userResponse) {
        User user = userResponse.getResponse();
        validate(user);
    }

    private void validate(Object userObj) {
        User user = (User) userObj;
        Assert.assertTrue(user.getIdentity().matches("^[a-zA-Z0-9]+$"));
        Assert.assertTrue(user.getEmail().matches("^[a-zA-Z0-9]+@apriori.com$"));
    }

}
