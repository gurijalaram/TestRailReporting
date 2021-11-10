package com.customer.systemconfiguration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import com.apriori.customer.systemconfiguration.SystemConfigurationGroupsPage;
import com.apriori.login.CasLoginPage;
import com.apriori.utils.TestRail;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.components.SelectionTreeItemComponent;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class SystemConfigurationGroupsTests extends TestBase {
    private SystemConfigurationGroupsPage systemConfigurationGroupsPage;

    @Before
    public void setup() {
        systemConfigurationGroupsPage = new CasLoginPage(driver)
            .login(UserUtil.getUser())
            .openAprioriInternal()
            .goToSystemConfiguration()
            .goToGroupsPage();
    }


    @Test
    @Description("Test that the groups are displayed and the first one is selected.")
    @TestRail(testCaseId = {"9880", "9883", "9881"})
    public void testValidateShouldDisplayGroupsForTheCustomer() {

        List<SelectionTreeItemComponent> groups = systemConfigurationGroupsPage.getGroupsTree().getHierarchy();
        SelectionTreeItemComponent firstGroup = groups.stream().findFirst().orElse(null);
        assertThat(firstGroup.isSelected(), is(true));
    }
}
