package com.customer.systemconfiguration;

import com.apriori.customer.systemconfiguration.SystemConfigurationPermissionsPage;
import com.apriori.login.CasLoginPage;
import com.apriori.utils.ListUtils;
import com.apriori.utils.TestRail;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.components.SelectionTreeItemComponent;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.Test;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SystemConfigurationPermissionsTests extends TestBase {
    private SystemConfigurationPermissionsPage systemConfigurationPermissionsPage;

    @Before
    public void setup() {
        systemConfigurationPermissionsPage  = new CasLoginPage(driver)
            .login(UserUtil.getUser())
            .openAprioriInternal()
            .goToSystemConfiguration()
            .goToPermissionsPage();
    }

    private void testValidateThatPermissionsAreSortedInAscendingOrder(SoftAssertions soft) {
        List<SelectionTreeItemComponent> permissions = systemConfigurationPermissionsPage
            .getPermissionsList()
            .getHierarchy();
        List<String> order = permissions.stream().map((p) -> p.getText()).collect(Collectors.toList());
        soft.assertThat(ListUtils.isSorted(order, Comparator.nullsFirst(Comparator.naturalOrder())))
            .overridingErrorMessage("The list of permissions is not sorted.")
            .isTrue();
    }

    private void testValidateFirstPermissionIsSelected(SoftAssertions soft) {
        SelectionTreeItemComponent firstNode = systemConfigurationPermissionsPage
            .getPermissionsList()
            .getHierarchy()
            .get(0);
        soft.assertThat(firstNode.isSelected())
            .overridingErrorMessage("The first permission is not selected.")
            .isTrue();
    }

    @Test
    @Description("System Configuration Permissions should be displayed and sorted in alphabetical order.  The first permission should also be selected.")
    @TestRail(testCaseId = {"9880", "9883", "9882"})
    public void testValidateShouldDisplayPermissions() {

        SoftAssertions soft = new SoftAssertions();
        testValidateThatPermissionsAreSortedInAscendingOrder(soft);
        testValidateFirstPermissionIsSelected(soft);
        soft.assertAll();
    }
}
