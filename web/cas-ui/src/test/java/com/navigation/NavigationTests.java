package com.navigation;

import com.apriori.customer.CustomerWorkspacePage;
import com.apriori.login.CasLoginPage;
import com.apriori.newcustomer.users.UsersListPage;
import com.apriori.testsuites.categories.SmokeTest;
import com.apriori.utils.PageUtils;
import com.apriori.utils.TestRail;
import com.apriori.utils.properties.PropertiesContext;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.By;

public class NavigationTests extends TestBase {

    private CustomerWorkspacePage customerProfilePage;
    private String customerURL;
    private PageUtils pageUtils;

    @Before
    public void setup() {
        pageUtils = new PageUtils(driver);
        customerProfilePage = new CasLoginPage(driver)
            .login(UserUtil.getUser())
            .searchForCustomer("aPriori Internal")
            .selectCustomer("aPriori Internal");
        String customerID = customerProfilePage.findCustomerIdentity();
        String customerBase = String.format("customers/%s", customerID);
        String baseURL = PropertiesContext.get("${env}.cas.ui_url");
        customerURL = String.format("%s%s", baseURL, customerBase);
    }

    @Test
    @Description("Test that pages can be accessed directly by their URL")
    @TestRail(testCaseId = {"9907"})
    public void testAccessPagesViaDirectURL() {
        SoftAssertions soft = new SoftAssertions();

        validatePageContainsClass(
            soft,
            String.format("%s%s", customerURL, "/profile"),
            "customer-profile"
        );
        validatePageContainsClass(
            soft,
            String.format("%s%s", customerURL, "/users/customer-staff"),
            "user-table"
        );
        validatePageContainsClass(
            soft,
            String.format("%s%s", customerURL, "/users/import"),
            "batch-item-list"
        );
        validatePageContainsClass(
            soft,
            String.format("%s%s", customerURL, "/sites-and-licenses"),
            "license-workspace"
        );
        validatePageContainsClass(
            soft,
            String.format("%s%s", customerURL, "/infrastructure"),
            "infrastructure-workspace"
        );

        soft.assertAll();
    }

    private void validatePageContainsClass(SoftAssertions soft, String url, String definingElementClass) {
        driver.navigate().to(url);
        pageUtils.isPageLoaded(By.className(definingElementClass));
        soft.assertThat(getDriver().findElements(By.className(definingElementClass)))
            .overridingErrorMessage("Navigating to %s doesn't load expected content %s", url, definingElementClass)
            .isNotEmpty();
    }

    @Test
    @Category(SmokeTest.class)
    @Description("Test that the URL updates when navigating between pages")
    @TestRail(testCaseId = {"9908"})
    public void testURLUpdatesWithPageChange() {

        SoftAssertions soft = new SoftAssertions();

        validatePageURLMatches(soft,  "/profile");

        // Click through different menus
        UsersListPage usersListPage = customerProfilePage.goToUsersList();
        validatePageURLMatches(soft, "/users/customer-staff");
        usersListPage.goToImport();
        validatePageURLMatches(soft,  "/users/import");
        customerProfilePage.goToSitesLicenses();
        validatePageURLMatches(soft, "/sites-and-licenses");
        customerProfilePage.goToInfrastructure();
        validatePageURLMatches(soft, "/infrastructure");

        soft.assertAll();
    }

    private void validatePageURLMatches(SoftAssertions soft, String pageURL) {
        String expected = String.format("%s%s", customerURL, pageURL);
        soft.assertThat(driver.getCurrentUrl())
            .overridingErrorMessage("Current clicked through page %s not expected page %s",
                driver.getCurrentUrl(), expected)
            .isEqualTo(expected);
    }
}
