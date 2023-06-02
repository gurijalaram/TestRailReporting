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

import java.util.List;
import java.util.stream.Collectors;

public class CdsRolesTests {
    private CdsTestUtil cdsTestUtil = new CdsTestUtil();
    private SoftAssertions soft = new SoftAssertions();

    @Test
    @TestRail(testCaseId = {"3243","17159"})
    @Description("API returns a list of all the available roles in the CDS DB")
    public void getRoles() {
        Roles response = cdsTestUtil.getRoles();

        soft.assertThat(response.getTotalItemCount()).isEqualTo(12);
        soft.assertThat(response.getItems().stream().map(Role::getName).collect(Collectors.toList()))
            .contains("AP_ANALYST", "AP_AUTOMATION", "AP_CONNECT_USER", "AP_CONTRIBUTOR","AP_DESIGNER","AP_EDC","AP_EXPERT","AP_HIGH_MEM","AP_PREVIEW","AP_SANDBOX","AP_USER","AP_USER_ADMIN");
        soft.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"3699"})
    @Description("API returns a role's information based on the supplied identity")
    public void getRoleById() {
        Roles responseWrapper = cdsTestUtil.getRoles();

        String roleIdentity = responseWrapper.getItems().get(0).getIdentity();

        Roles response = cdsTestUtil.getRoles(roleIdentity);

        soft.assertThat(response.getItems().get(0).getName()).isEqualTo("AP_ANALYST");
        soft.assertAll();
    }
}
