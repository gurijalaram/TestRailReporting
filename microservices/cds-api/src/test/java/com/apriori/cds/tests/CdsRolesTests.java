package com.apriori.cds.tests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.objects.response.Role;
import com.apriori.cds.objects.response.Roles;
import com.apriori.cds.tests.utils.CdsTestUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Test;

import java.util.stream.Collectors;

public class CdsRolesTests {
    private CdsTestUtil cdsTestUtil = new CdsTestUtil();

    @Test
    @TestRail(testCaseId = {"3243"})
    @Description("API returns a list of all the available roles in the CDS DB")
    public void getRoles() {
        ResponseWrapper<Roles> response = cdsTestUtil.getCommonRequest(CDSAPIEnum.GET_ROLES, Roles.class);

        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(response.getResponseEntity().getTotalItemCount(), is(2));
        assertThat(response.getResponseEntity().getItems().stream().map(Role::getName).collect(Collectors.toList()), hasItems("USER", "ADMIN"));
    }

    @Test
    @TestRail(testCaseId = {"3699"})
    @Description("API returns a role's information based on the supplied identity")
    public void getRoleById() {
        ResponseWrapper<Roles> responseWrapper = cdsTestUtil.getCommonRequest(CDSAPIEnum.GET_ROLES, Roles.class);

        String roleIdentity = responseWrapper.getResponseEntity().getItems().get(0).getIdentity();

        ResponseWrapper<Roles> response = cdsTestUtil.getCommonRequest(CDSAPIEnum.GET_ROLES, Roles.class, roleIdentity);

        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(response.getResponseEntity().getItems().get(0).getName(), is("ADMIN"));
    }
}
