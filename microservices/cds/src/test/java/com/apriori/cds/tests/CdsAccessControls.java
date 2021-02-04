package com.apriori.cds.tests;

import com.apriori.apibase.services.cds.objects.AccessControls.AccessControlResponse;
import com.apriori.cds.entity.response.User;
import com.apriori.cds.entity.response.Users;
import com.apriori.cds.tests.utils.CdsTestUtil;
import com.apriori.cds.utils.Constants;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class CdsAccessControls extends CdsTestUtil {
    private String url;

    @Before
    public void setServiceUrl() {
        url = Constants.getServiceUrl();
    }


    @Test
    @TestRail(testCaseId = "3289")
    @Description("API returns a list of all the access controls in the CDS DB")
    public void getAccessControls() {
        url = String.format(url, "access-controls");
        ResponseWrapper<AccessControlResponse> response = getCommonRequest(url, true, AccessControlResponse.class);

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, response.getStatusCode());
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
