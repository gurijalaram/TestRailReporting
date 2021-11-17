package com.apriori.customer;

import com.apriori.customer.systemconfiguration.SystemConfigurationPage;
import com.apriori.newcustomer.CustomerProfilePage;
import com.apriori.newcustomer.InfrastructurePage;
import com.apriori.newcustomer.SitesLicensesPage;
import com.apriori.newcustomer.users.UsersListPage;
import com.apriori.utils.properties.PropertiesContext;
import com.apriori.utils.web.components.EagerPageComponent;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.Arrays;
import java.util.function.Supplier;

/**
 * Represents the root of the customer page that contains all the tabs for customer editing.
 */
@Slf4j
public class CustomerWorkspacePage extends EagerPageComponent<CustomerWorkspacePage> {
    @FindBy(xpath = "//a[.='Profile']")
    private WebElement profileTab;

    @FindBy(xpath = "//a[.='Users']")
    private WebElement usersTab;

    @FindBy(xpath = "//a[.='Sites & Licenses']")
    private WebElement siteLicenseTab;

    @FindBy(xpath = "//a[.='Infrastructure']")
    private WebElement infrastructureTab;

    @FindBy(xpath = "//a[.='Security']")
    private WebElement securityTab;

    @FindBy(xpath = "//a[.='System Configuration']")
    private WebElement systemConfigurationTab;

    /**
     * Initializes a new instance of this object.
     *
     * @param driver The web driver that the page exists on.
     */
    public CustomerWorkspacePage(WebDriver driver) {
        super(driver, log);
    }

    /**
     * Determines if the workspace page is loaded by validating that all required tabs are available.
     *
     * @throws Error Occurs if any of the necessary tabs are missing.
     */
    @Override
    protected void isLoaded() throws Error {
        getPageUtils().waitForElementAppear(profileTab);
        getPageUtils().waitForElementAppear(usersTab);
        getPageUtils().waitForElementToAppear(siteLicenseTab);
        getPageUtils().waitForElementToAppear(infrastructureTab);
        getPageUtils().waitForElementToAppear(systemConfigurationTab);
    }

    /**
     * Gets a value that indicates if the profile tab is displayed.
     *
     * @return True if the profile tab is displayed.
     */
    public boolean isProfileTabDisplayed() {
        return profileTab.isDisplayed();
    }

    /**
     * Gets a value that indicates if the users tab is enabled and can be clicked.
     *
     * @return A value that indicates if the users tab is enabled
     */
    public boolean isUsersTabEnabled() {
        return !StringUtils.equalsIgnoreCase("true", usersTab.getAttribute("disabled"));
    }

    /**
     * Gets a value that indicates if the sites & licenses tab is enabled and can be clicked.
     *
     * @return A value that indicates if the sites & licenses tab is enabled
     */
    public boolean isSitesAndLicensesEnabled() {
        return !StringUtils.equalsIgnoreCase("true", siteLicenseTab.getAttribute("disabled"));
    }

    /**
     * Gets a value that indicates if the infrastructure tab is enabled and can be clicked.
     *
     * @return A value that indicates if the infrastructure tab is enabled
     */
    public boolean isInfrastructureEnabled() {
        return !StringUtils.equalsIgnoreCase("true", infrastructureTab.getAttribute("disabled"));
    }

    /**
     * Gets a value that indicates if the security tab is enabled and can be clicked.
     *
     * @return A value that indicates if the security tab is enabled
     */
    public boolean isSecurityEnabled() {
        return !StringUtils.equalsIgnoreCase("true", securityTab.getAttribute("disabled"));
    }

    /**
     * Gets a value that indicates if the system configuration tab is enabled and can be clicked.
     *
     * @return A value that indicates if the system configuration tab is enabled
     */
    public boolean isSystemConfigurationEnabled() {
        return !StringUtils.equalsIgnoreCase("true", systemConfigurationTab.getAttribute("disabled"));
    }

    /**
     * Moves to a specific tab.
     *
     * If the requested tab is already active, then this method does nothing.
     *
     * @param tab The root element of the tab to move to.
     * @param factory The factory that constructs the page object model for the content under the tab.
     * @param <T> The type that represents the page object model under the wanted tab.
     *
     * @return The page object model for the tab content.
     */
    private <T> T goToTab(WebElement tab, Supplier<T> factory) {
        // It's possible that this is the only tab that can be viewed;
        // it may be disabled - this is true for new customers.  If we are already on
        // the tab, then we can just return the profile page.

        boolean isTabActive = getPageUtils().doesElementHaveClass(tab, "active");

        if (!isTabActive) {
            getPageUtils().waitForElementAndClick(tab);
        }

        return factory.get();
    }

    /**
     * Go to the profile tab.
     *
     * @return The profile page.
     */
    public CustomerProfilePage goToProfile() {
        return goToTab(profileTab, () -> new CustomerProfilePage(getDriver()));
    }

    /**
     * Go to users tab
     *
     * @return new page object
     */
    public UsersListPage goToUsersList() {
        return goToTab(usersTab, () -> new UsersListPage(getDriver()));
    }

    /**
     * Go to sites and licenses tab
     *
     * @return new page object
     */
    public SitesLicensesPage goToSitesLicenses() {
        return goToTab(siteLicenseTab, () -> new SitesLicensesPage(getDriver()));
    }

    /**
     * Go to infrastructure tab
     *
     * @return new page object
     */
    public InfrastructurePage goToInfrastructure() {
        return goToTab(infrastructureTab, () -> new InfrastructurePage(getDriver()));
    }

    /**
     * Go to the system configuration tab.
     *
     * @return The POM for the system configuration.
     */
    public SystemConfigurationPage goToSystemConfiguration() {
        return goToTab(systemConfigurationTab, () -> new SystemConfigurationPage(getDriver()));
    }

    /**
     * Attempts to discover the customer identity.
     *
     * @return The customer identity for an existing customer, "new" for a new customer,
     *         and the empty string if it cannot determine the identity.
     */
    public String findCustomerIdentity() {
        String baseUrl = PropertiesContext.get("${env}.cas.ui_url");
        String url = getDriver().getCurrentUrl().replace(String.format("%scustomers/", baseUrl), "");
        return Arrays.stream(url.split("/")).findFirst().orElse("");
    }
}
