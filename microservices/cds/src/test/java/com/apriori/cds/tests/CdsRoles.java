package com.apriori.cds.tests;

import com.apriori.cds.entity.response.Role;
import com.apriori.cds.entity.response.Roles;
import com.apriori.cds.tests.utils.CdsTestUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.constants.CommonConstants;
import com.apriori.utils.http.builder.dao.ServiceConnector;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class CdsRoles extends CdsTestUtil {
    private String url;

    @Before
    public void setServiceUrl() {
        url = ServiceConnector.getServiceUrl();
    }

    @Test
    @TestRail(testCaseId = "3243")
    @Description("API returns a list of all the available roles in the CDS DB")
    public void getRoles() {
        url = String.format(url, "roles");

        ResponseWrapper<Roles> response =  getCommonRequest(url, true, Roles.class);

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, response.getStatusCode());
        validateRoles(response.getResponseEntity());
    }

    @Test
    @TestRail(testCaseId = "3699")
    @Description("API returns a role's information based on the supplied identity")
    public void getRoleById() {
        url = String.format(url,
            String.format("roles/%s", CommonConstants.getCdsIdentityRole()));
        ResponseWrapper<Role> response = getCommonRequest(url, true, Role.class);

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, response.getStatusCode());
        validateRole(response.getResponseEntity());
    }


    /*
     * Role Validation
     */
    private void validateRoles(Roles rolesResponse) {
        Object[] roles = rolesResponse.getResponse().getItems().toArray();
        Arrays.stream(roles)
            .forEach(this::validate);
    }

    private void validateRole(Role roleResponse) {
        Role role = roleResponse.getResponse();
        validate(role);
    }

    private void validate(Object roleObj) {
        Role role = (Role) roleObj;
        Assert.assertTrue(role.getIdentity().matches("^[a-zA-Z0-9]+$"));
    }


}
