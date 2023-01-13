package com.apriori.cds.tests;

import com.apriori.cds.enums.CDSAPIEnum;
import com.apriori.cds.objects.response.Role;
import com.apriori.cds.objects.response.Roles;
import com.apriori.cds.utils.CdsTestUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

import java.util.stream.Collectors;

public class CdsRolesTests {
    private CdsTestUtil cdsTestUtil = new CdsTestUtil();
    private SoftAssertions soft = new SoftAssertions();

    @Test
    @TestRail(testCaseId = {"3243"})
    @Description("API returns a list of all the available roles in the CDS DB")
    public void getRoles() {
        ResponseWrapper<Roles> response = cdsTestUtil.getCommonRequest(CDSAPIEnum.ROLES, Roles.class, HttpStatus.SC_OK);

        soft.assertThat(response.getResponseEntity().getTotalItemCount()).isEqualTo(4);
        soft.assertThat(response.getResponseEntity().getItems().stream().map(Role::getName).collect(Collectors.toList())).contains("AP_USER", "AP_ADMIN");
        soft.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"3699"})
    @Description("API returns a role's information based on the supplied identity")
    public void getRoleById() {
        ResponseWrapper<Roles> responseWrapper = cdsTestUtil.getCommonRequest(CDSAPIEnum.ROLES, Roles.class, HttpStatus.SC_OK);

        String roleIdentity = responseWrapper.getResponseEntity().getItems().get(0).getIdentity();

        ResponseWrapper<Roles> response = cdsTestUtil.getCommonRequest(CDSAPIEnum.ROLES, Roles.class,HttpStatus.SC_OK,  roleIdentity);

        soft.assertThat(response.getResponseEntity().getItems().get(0).getName()).isEqualTo("AP_ADMIN");
        soft.assertAll();
    }
}
