package com.navigation;

import com.apriori.customer.CustomerWorkspacePage;
import com.apriori.customer.systemconfiguration.SystemConfigurationPage;
import com.apriori.login.CasLoginPage;
import com.apriori.newcustomer.InfrastructurePage;
import com.apriori.newcustomer.SitesLicensesPage;
import com.apriori.newcustomer.users.ImportPage;
import com.apriori.newcustomer.users.UsersListPage;
import com.apriori.testsuites.categories.SmokeTest;
import com.apriori.utils.TestRail;
import com.apriori.utils.properties.PropertiesContext;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.support.ui.LoadableComponent;

public class NavigationTests extends TestBase {

    private CustomerWorkspacePage customerProfilePage;
    private String customerID;

    @Before
    public void setup() {
        customerProfilePage = new CasLoginPage(driver)
            .login(UserUtil.getUser())
            .searchForCustomer("aPriori Internal")
            .selectCustomer("aPriori Internal");
        customerID = customerProfilePage.findCustomerIdentity();
    }

    @Test
    @Description("Test that pages can be accessed directly by their URL")
    @TestRail(testCaseId = {"9907"})
    public void testAccessPagesViaDirectURL() {
        SoftAssertions soft = new SoftAssertions();
        String errorMessage = "Navigating to %s URL doesn't load expected page content";

        soft.assertThat(CustomerWorkspacePage.getViaURL(driver, customerID))
            .overridingErrorMessage(errorMessage, "CustomerWorkspacePage")
            .isInstanceOf(LoadableComponent.class);
        soft.assertThat(UsersListPage.getViaURL(driver, customerID))
            .overridingErrorMessage(errorMessage, "UsersListPage")
            .isInstanceOf(LoadableComponent.class);
        soft.assertThat(ImportPage.getViaURL(driver, customerID))
            .overridingErrorMessage(errorMessage, "ImportPage")
            .isInstanceOf(LoadableComponent.class);
        soft.assertThat(SitesLicensesPage.getViaURL(driver, customerID))
            .overridingErrorMessage(errorMessage, "SitesLicensesPage")
            .isInstanceOf(LoadableComponent.class);
        soft.assertThat(InfrastructurePage.getViaURL(driver, customerID))
            .overridingErrorMessage(errorMessage, "InfrastructurePage")
            .isInstanceOf(LoadableComponent.class);
        soft.assertThat(SystemConfigurationPage.getViaURL(driver, customerID))
            .overridingErrorMessage(errorMessage, "SystemConfigurationPage")
            .isInstanceOf(LoadableComponent.class);

        soft.assertAll();
    }

    @Test
    @Category(SmokeTest.class)
    @Description("Test that the URL updates when navigating between pages")
    @TestRail(testCaseId = {"9908"})
    public void testURLUpdatesWithPageChange() {

        SoftAssertions soft = new SoftAssertions();

        validateOnPageURL(soft,  "/profile");

        // Click through different menus
        UsersListPage usersListPage = customerProfilePage.goToUsersList();
        validateOnPageURL(soft, "/users/customer-staff");
        usersListPage.goToImport();
        validateOnPageURL(soft,  "/users/import");
        customerProfilePage.goToSitesLicenses();
        validateOnPageURL(soft, "/sites-and-licenses");
        customerProfilePage.goToInfrastructure();
        validateOnPageURL(soft, "/infrastructure");

        soft.assertAll();
    }

    private void validateOnPageURL(SoftAssertions soft, String pageURL) {
        String url = PropertiesContext.get("${env}.cas.ui_url") + "customers/%s";
        String expected = String.format("%s%s", String.format(url, customerID), pageURL);
        soft.assertThat(driver.getCurrentUrl())
            .overridingErrorMessage("Current clicked through page %s not expected page %s",
                driver.getCurrentUrl(), expected)
            .isEqualTo(expected);
    }
}
