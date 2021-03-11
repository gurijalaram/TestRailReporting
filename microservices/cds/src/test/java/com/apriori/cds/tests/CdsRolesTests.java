package com.apriori.cds.tests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cds.objects.response.Role;
import com.apriori.cds.objects.response.Roles;
import com.apriori.cds.tests.utils.CdsTestUtil;
import com.apriori.cds.utils.Constants;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

public class CdsRolesTests extends CdsTestUtil {
    private String url;

    @Before
    public void setServiceUrl() {
        url = Constants.getServiceUrl();
    }

    @Test
    @TestRail(testCaseId = "3243")
    @Description("API returns a list of all the available roles in the CDS DB")
    public void getRoles() {
        url = String.format(url, "roles");

        ResponseWrapper<Roles> response = get(url, Roles.class);

        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(response.getResponseEntity().getResponse().getTotalItemCount(), is(2));
        assertThat(response.getResponseEntity().getResponse().getItems().get(0).getName(), is("USER"));
        assertThat(response.getResponseEntity().getResponse().getItems().get(1).getName(), is("ADMIN"));
    }

    @Test
    @TestRail(testCaseId = "3699")
    @Description("API returns a role's information based on the supplied identity")
    public void getRoleById() {
        String rolesUrl = String.format(url, "roles");

        ResponseWrapper<Roles> responseWrapper = get(rolesUrl, Roles.class);

        String roleIdentity = responseWrapper.getResponseEntity().getResponse().getItems().get(0).getIdentity();

        String identityUrl = String.format(url, String.format("roles/%s", roleIdentity));
        ResponseWrapper<Role> response = get(identityUrl, Role.class);

        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(response.getResponseEntity().getResponse().getName(), is("USER"));
    }
}
