package com.apriori;

import com.apriori.cds.models.response.Role;
import com.apriori.cds.models.response.Roles;
import com.apriori.cds.utils.CdsTestUtil;
import com.apriori.rules.TestRulesAPI;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.stream.Collectors;

@ExtendWith(TestRulesAPI.class)
public class CdsRolesTests {
    private CdsTestUtil cdsTestUtil = new CdsTestUtil();
    private SoftAssertions soft = new SoftAssertions();

    @Test
    @TestRail(id = {3243, 17159})
    @Description("API returns a list of all the available roles in the CDS DB")
    public void getRoles() {
        Roles response = cdsTestUtil.getRoles();

        soft.assertThat(response.getTotalItemCount()).isEqualTo(13);
        soft.assertThat(response.getItems().stream().map(Role::getName).collect(Collectors.toList()))
            .contains("AP_ANALYST", "AP_AUTOMATION", "AP_CONNECT_USER", "AP_CONTRIBUTOR","AP_DESIGNER","AP_EDC","AP_EXPERT","AP_HIGH_MEM","AP_PREVIEW","AP_SANDBOX","AP_USER","AP_USER_ADMIN","AP_EXPORT_ADMIN");
        soft.assertAll();
    }

    @Test
    @TestRail(id = {3699})
    @Description("API returns a role's information based on the supplied identity")
    public void getRoleById() {
        Roles responseWrapper = cdsTestUtil.getRoles();

        String roleIdentity = responseWrapper.getItems().get(0).getIdentity();

        Roles response = cdsTestUtil.getRoles(roleIdentity);

        soft.assertThat(response.getItems().get(0).getName()).isEqualTo("AP_ANALYST");
        soft.assertAll();
    }
}
