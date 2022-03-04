package com.apriori.customer;

import com.apriori.customer.systemconfiguration.SystemConfigurationPage;
import com.apriori.customer.users.UsersPage;
import com.apriori.customeradmin.CustomerAdminPage;
import com.apriori.newcustomer.CustomerProfilePage;
import com.apriori.newcustomer.InfrastructurePage;
import com.apriori.siteLicenses.SitesLicensesPage;
import com.apriori.utils.PageUtils;
import com.apriori.utils.properties.PropertiesContext;
import com.apriori.utils.web.components.EagerPageComponent;
import com.apriori.utils.web.components.RoutingComponent;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.Arrays;

/**
 * Represents the root of the customer page that contains all the tabs for customer editing.
 */
@Slf4j
public class CustomerWorkspacePage extends EagerPageComponent<CustomerWorkspacePage> {
    @FindBy(xpath = "//a[.='Profile']")
    private WebElement profileTabRoot;
    private RoutingComponent profileTab;

    @FindBy(xpath = "//a[.='Users']")
    private WebElement usersTabRoot;
    private RoutingComponent usersTab;

    @FindBy(xpath = "//a[.='Sites & Licenses']")
    private WebElement siteLicenseTabRoot;
    private RoutingComponent siteLicenseTab;

    @FindBy(xpath = "//a[.='Infrastructure']")
    private WebElement infrastructureTabRoot;
    private RoutingComponent infrastructureTab;

    @FindBy(xpath = "//a[.='Security']")
    private WebElement securityTabRoot;
    private RoutingComponent securityTab;

    @FindBy(xpath = "//a[.='System Configuration']")
    private WebElement systemConfigurationTabRoot;
    private RoutingComponent systemConfigurationTab;

    @FindBy(css = ".btn-link")
    private WebElement customersButton;

    /**
     * Initializes a new instance of this object.
     *
     * @param driver The web driver that the page exists on.
     */
    public CustomerWorkspacePage(WebDriver driver) {
        super(driver, log);

        // Required Tabs
        profileTab = new RoutingComponent(getDriver(), profileTabRoot);
        usersTab = new RoutingComponent(getDriver(), usersTabRoot);
        siteLicenseTab = new RoutingComponent(getDriver(), siteLicenseTabRoot);
        systemConfigurationTab = new RoutingComponent(getDriver(), systemConfigurationTabRoot);

        // Optional Tabs
        securityTab = getPageUtils().isElementDisplayed(securityTabRoot) ? new RoutingComponent(getDriver(), securityTabRoot) : null;
    }

    /**
     * Determines if the workspace page is loaded by validating that all required tabs are available.
     *
     * @throws Error Occurs if any of the necessary tabs are missing.
     */
    @Override
    protected void isLoaded() throws Error {
        getPageUtils().waitForElementAppear(profileTabRoot);
        getPageUtils().waitForElementAppear(usersTabRoot);
        getPageUtils().waitForElementToAppear(infrastructureTabRoot);
        getPageUtils().waitForElementToAppear(systemConfigurationTabRoot);
    }

    /**
     * Gets the profile tab.
     *
     * @return The profile tab.
     */
    public RoutingComponent getProfileTab() {
        return profileTab;
    }

    /**
     * Gets the users tab.
     *
     * @return The users tab.
     */
    public RoutingComponent getUsersTab() {
        return usersTab;
    }

    /**
     * Gets the sites and licenses tab.
     *
     * @return The sites and licenses tab.
     */
    public RoutingComponent getSiteLicenseTab() {
        return siteLicenseTab;
    }

    /**
     * Gets the infrastructure tab.
     *
     * @return The infrastructure tab.
     */
    public RoutingComponent getInfrastructureTab() {
        return infrastructureTab;
    }

    /**
     * Gets the system configuration tab.
     *
     * @return The system configuration tab.
     */
    public RoutingComponent getSystemConfigurationTab() {
        return systemConfigurationTab;
    }

    /**
     * Gets the security tab.
     *
     * @return The security tab.
     */
    public RoutingComponent getSecurityTab() {
        return securityTab;
    }

    /**
     * Go to the profile tab.
     *
     * @return The profile page.
     */
    public CustomerProfilePage goToProfile() {
        profileTab.navigate();
        return new CustomerProfilePage(getDriver());
    }

    /**
     * Go to users tab
     *
     * @return new page object
     */
    public UsersPage goToUsersPage() {
        usersTab.navigate();
        return new UsersPage(getDriver());
    }

    /**
     * Go to sites and licenses tab
     *
     * @return new page object
     */
    public SitesLicensesPage goToSitesLicenses() {
        siteLicenseTab.navigate();
        return new SitesLicensesPage(getDriver());
    }

    /**
     * Go to infrastructure tab
     *
     * @return new page object
     */
    public InfrastructurePage goToInfrastructure() {
        infrastructureTab.navigate();
        return new InfrastructurePage(getDriver());
    }

    /**
     * Go to the system configuration tab.
     *
     * @return The POM for the system configuration.
     */
    public SystemConfigurationPage goToSystemConfiguration() {
        systemConfigurationTab.navigate();
        return new SystemConfigurationPage(getDriver());
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

    /**
     * Same as findCustomerIdentity() but only succeeds if an existing customer identity is found.
     * @return The identity of an existing customer.
     */
    public String findExistingCustomerIdentity() {
        getPageUtils().waitForCondition(() -> findCustomerIdentity().length() > 0, PageUtils.DURATION_SLOW);
        getPageUtils().waitForCondition(() -> !StringUtils.equalsIgnoreCase(findCustomerIdentity(), "new"),
            PageUtils.DURATION_SLOW);
        return findCustomerIdentity();
    }

    /**
     * Retrieves CustomerProfilePage for customer via URL and returns Page object.
     *
     * @param driver - WebDriver
     * @param customer - Customer ID
     * @return CustomerWorkspacePage
     */
    public static CustomerWorkspacePage getViaURL(WebDriver driver, String customer) {
        String url = PropertiesContext.get("${env}.cas.ui_url") + "customers/%s";
        driver.navigate().to(String.format(url, customer));
        return new CustomerWorkspacePage(driver);
    }

    /**
     * Returns to customer list page
     *
     * @return new page object
     */
    public CustomerAdminPage goToCustomersList() {
        getPageUtils().waitForElementAndClick(customersButton);
        return new CustomerAdminPage(getDriver());
    }
}
