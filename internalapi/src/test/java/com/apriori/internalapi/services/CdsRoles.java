package com.apriori.internalapi.services;

import com.apriori.apibase.http.builder.dao.ServiceConnector;
import com.apriori.apibase.services.objects.Role;
import com.apriori.apibase.services.objects.Roles;
import com.apriori.utils.TestRail;
import com.apriori.utils.constants.Constants;

import io.qameta.allure.Description;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class CdsRoles {
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
        Roles response = (Roles) ServiceConnector.getService(url, Roles.class);
        validateRoles(response);
    }

    @Test
    @TestRail(testCaseId = "3699")
    @Description("API returns a role's information based on the supplied identity")
    public void getRoleById() {
        url = String.format(url,
            String.format("roles/%s", Constants.getCdsIdentityRole()));
        Role response = (Role) ServiceConnector.getService(url, Role.class);
        validateRole(response);
    }


    /*
     * Role Validation
     */
    private void validateRoles(Roles rolesResponse) {
        Object[] roles = rolesResponse.getResponse().getItems().toArray();
        Arrays.stream(roles)
            .forEach(r -> validate(r));
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
