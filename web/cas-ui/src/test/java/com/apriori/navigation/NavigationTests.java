package com.apriori.navigation;

import static com.apriori.testconfig.TestSuiteType.TestSuite.SMOKE;

import com.apriori.pageobjects.customer.CustomerWorkspacePage;
import com.apriori.pageobjects.customer.users.ImportPage;
import com.apriori.pageobjects.customer.users.UsersListPage;
import com.apriori.pageobjects.customer.users.UsersPage;
import com.apriori.pageobjects.login.CasLoginPage;
import com.apriori.pageobjects.newcustomer.CustomerProfilePage;
import com.apriori.pageobjects.newcustomer.InfrastructurePage;
import com.apriori.properties.PropertiesContext;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testconfig.TestBaseUI;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.support.ui.LoadableComponent;

public class NavigationTests extends TestBaseUI {

    private CustomerWorkspacePage customerProfilePage;
    private String customerID;

    @BeforeEach
    public void setup() {
        customerProfilePage = new CasLoginPage(driver)
            .login(UserUtil.getUser())
            .openAprioriInternal();
        customerID = customerProfilePage.findCustomerIdentity();
    }

    // System Configuration page is disabled in CAS
    @Test
    @Description("Test that pages can be accessed directly by their URL")
    @TestRail(id = {9907})
    public void testAccessPagesViaDirectURL() {
        SoftAssertions soft = new SoftAssertions();
        String errorMessage = "Navigating to %s URL doesn't load expected page content";

        soft.assertThat(CustomerWorkspacePage.getViaURL(driver, customerID))
            .overridingErrorMessage(errorMessage, "CustomerWorkspacePage")
            .isInstanceOf(LoadableComponent.class);
        soft.assertThat(UsersListPage.getViaURL(driver, customerID))
            .overridingErrorMessage(errorMessage, "UsersListPage")
            .isInstanceOf(LoadableComponent.class);
        soft.assertThat(CustomerProfilePage.getViaURL(driver, customerID))
            .overridingErrorMessage(errorMessage, "CustomerProfilePage")
            .isInstanceOf(LoadableComponent.class);
        soft.assertThat(ImportPage.getViaURL(driver, customerID))
            .overridingErrorMessage(errorMessage, "ImportPage")
            .isInstanceOf(LoadableComponent.class);
        soft.assertThat(InfrastructurePage.getViaURL(driver, customerID))
            .overridingErrorMessage(errorMessage, "InfrastructurePage")
            .isInstanceOf(LoadableComponent.class);
        /*soft.assertThat(SystemConfigurationPage.getViaURL(driver, customerID))
            .overridingErrorMessage(errorMessage, "SystemConfigurationPage")
            .isInstanceOf(LoadableComponent.class);*/

        soft.assertAll();
    }

    @Test
    @Tag(SMOKE)
    @Description("Test that the URL updates when navigating between pages")
    @TestRail(id = {9908})
    public void testURLUpdatesWithPageChange() {

        SoftAssertions soft = new SoftAssertions();

        validateOnPageURL(soft, "/profile");

        // Click through different menus
        UsersPage usersPage = customerProfilePage.goToUsersPage();
        validateOnPageURL(soft, "/users/customer-staff");
        usersPage.goToImport();
        validateOnPageURL(soft, "/users/import");
        customerProfilePage.goToInfrastructure();
        validateOnPageURL(soft, "/infrastructure");

        soft.assertAll();
    }

    private void validateOnPageURL(SoftAssertions soft, String pageURL) {
        String url = PropertiesContext.get("cas.ui_url") + "customers/%s";
        String expected = String.format("%s%s", String.format(url, customerID), pageURL);
        soft.assertThat(driver.getCurrentUrl())
            .overridingErrorMessage("Current clicked through page %s not expected page %s",
                driver.getCurrentUrl(), expected)
            .isEqualTo(expected);
    }
}
