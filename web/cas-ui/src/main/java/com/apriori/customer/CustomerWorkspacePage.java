package com.apriori.customer;

import com.apriori.customer.systemconfiguration.SystemConfigurationPage;
import com.apriori.customer.users.UsersPage;
import com.apriori.customeradmin.CustomerAdminPage;
import com.apriori.customeradmin.NavToolbar;
import com.apriori.newcustomer.CustomerProfilePage;
import com.apriori.newcustomer.InfrastructurePage;
import com.apriori.security.SecurityPage;
import com.apriori.utils.PageUtils;
import com.apriori.utils.properties.PropertiesContext;
import com.apriori.utils.web.components.RoutingComponent;
import com.apriori.utils.web.components.SelectComponent;

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
public class CustomerWorkspacePage extends NavToolbar {
    @FindBy(xpath = "//a[.='Profile']")
    private WebElement profileTabRoot;
    private RoutingComponent profileTab;

    @FindBy(xpath = "//a[.='Users']")
    private WebElement usersTabRoot;
    private RoutingComponent usersTab;

    @FindBy(xpath = "//a[.='Infrastructure']")
    private WebElement infrastructureTabRoot;
    private RoutingComponent infrastructureTab;

    @FindBy(xpath = "//a[.='Security']")
    private WebElement securityTabRoot;
    private RoutingComponent securityTab;

    @FindBy(xpath = "//a[.='System Configuration']")
    private WebElement systemConfigurationTabRoot;
    private RoutingComponent systemConfigurationTab;

    @FindBy(xpath = "//div[@class='access-auth-buttons btn-group']//button[.='Request Access']")
    private WebElement requestAccessButton;

    @FindBy(xpath = "//div[@class='access-auth-buttons btn-group']//button[.='Revoke Access']")
    private WebElement revokeAccessButton;

    @FindBy(css = ".btn-link")
    private WebElement customersButton;

    @FindBy(css = ".service-account-drop-down")
    private WebElement serviceAccountsDropDown;
    private SelectComponent serviceAccountsSelectField;

    @FindBy(css = ".MuiDialogActions-spacing [data-testid='secondary-button']")
    private WebElement requestCancelButton;

    @FindBy(css = ".MuiDialogActions-spacing [data-testid='secondary-button']")
    private WebElement revokeCancelButton;

    @FindBy(css = ".MuiDialogActions-spacing [data-testid='primary-button']")
    private WebElement requestOkButton;

    @FindBy(xpath = "//button[.='Ok']")
    private WebElement confirmRevokeOkButton;

    @FindBy(css = ".Toastify__toast.Toastify__toast--error .Toastify__toast-body")
    private WebElement modalError;

    @FindBy(css = ".Toastify__toast.Toastify__toast--success .Toastify__toast-body")
    private WebElement successMessage;

    @FindBy(css = ".Toastify__close-button.Toastify__close-button--success")
    private WebElement closeMessage;

    /**
     * Initializes a new instance of this object.
     *
     * @param driver The web driver that the page exists on.
     */
    public CustomerWorkspacePage(WebDriver driver) {
        super(driver);

        // Required Tabs
        profileTab = new RoutingComponent(driver, profileTabRoot);
        usersTab = new RoutingComponent(driver, usersTabRoot);
        infrastructureTab = new RoutingComponent(driver, infrastructureTabRoot);

        // System configuration tab was hidden from CAS
        //  systemConfigurationTab = new RoutingComponent(getDriver(), systemConfigurationTabRoot);

        // Optional Tabs
        securityTab = getPageUtils().isElementDisplayed(securityTabRoot) ? new RoutingComponent(driver, securityTabRoot) : null;
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
        //       getPageUtils().waitForElementToAppear(systemConfigurationTabRoot);
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
     * Go to the security tab
     *
     * @return new page object
     */
    public SecurityPage goToSecurityPage() {
        securityTab.navigate();
        return new SecurityPage(getDriver());
    }

    /**
     * Attempts to discover the customer identity.
     *
     * @return The customer identity for an existing customer, "new" for a new customer,
     * and the empty string if it cannot determine the identity.
     */
    public String findCustomerIdentity() {
        String baseUrl = PropertiesContext.get("cas.ui_url");
        String url = getDriver().getCurrentUrl().replace(String.format("%scustomers/", baseUrl), "");
        return Arrays.stream(url.split("/")).findFirst().orElse("");
    }

    /**
     * Same as findCustomerIdentity() but only succeeds if an existing customer identity is found.
     *
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
     * @param driver   - WebDriver
     * @param customer - Customer ID
     * @return CustomerWorkspacePage
     */
    public static CustomerWorkspacePage getViaURL(WebDriver driver, String customer) {
        String url = PropertiesContext.get("cas.ui_url") + "customers/%s";
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

    /**
     * Clicks on Request access button
     *
     * @return this page object
     */
    public CustomerWorkspacePage clickRequestAccessButton() {
        getPageUtils().waitForElementAndClick(requestAccessButton);
        return this;
    }

    /**
     * Clicks on Revoke access button
     *
     * @return this page object
     */
    public CustomerWorkspacePage clickRevokeAccessButton() {
        getPageUtils().waitForElementAndClick(revokeAccessButton);
        return this;
    }

    /**
     * Selects value in drop down
     *
     * @param account name of account
     * @return this page object
     */
    public CustomerWorkspacePage selectServiceAccount(String account) {
        this.serviceAccountsSelectField = new SelectComponent(getDriver(), serviceAccountsDropDown);
        getPageUtils().waitForElementToAppear(serviceAccountsDropDown);
        serviceAccountsSelectField.select(account);
        return this;
    }

    /**
     * Clicks on Cancel button on request access popup
     *
     * @return this page object
     */
    public CustomerWorkspacePage clickCancelRequest() {
        getPageUtils().waitForElementAndClick(requestCancelButton);
        return this;
    }

    /**
     * Clicks on Cancel button on request access popup
     *
     * @return this page object
     */
    public CustomerWorkspacePage clickCancelRevoke() {
        getPageUtils().waitForElementAndClick(revokeCancelButton);
        return this;
    }

    /**
     * Clicks on Om button on request access popup
     *
     * @return this page object
     */
    public CustomerWorkspacePage clickOkRequestAccess() {
        getPageUtils().waitForElementAndClick(requestOkButton);
        return this;
    }

    /**
     * Clicks on Ok button on revoke access popup
     *
     * @return this page object
     */
    public CustomerWorkspacePage clickOkRevokeAccess() {
        getPageUtils().waitForElementAndClick(confirmRevokeOkButton);
        return this;
    }

    /**
     * @param button button locator
     * @return true or false
     */
    private boolean isButtonEnabled(WebElement button) {
        return button != null && button.isEnabled();
    }

    /**
     * Can click the revoke button.
     *
     * @return Boolean representing can click revoke button
     */
    public boolean canRevoke() {
        return isButtonEnabled(revokeAccessButton);
    }

    /**
     * Can click the request button
     *
     * @return Boolean representing can click request button
     */
    public boolean canRequest() {
        return isButtonEnabled(requestAccessButton);
    }

    /**
     * Gets error message text
     *
     * @return string error message
     */
    public String getTextErrorMessage() {
        return getPageUtils().waitForElementToAppear(modalError).getAttribute(("textContent"));
    }

    /**
     * Gets success message text
     *
     * @return string success message
     */
    public String getTextSuccessMessage() {
        return getPageUtils().waitForElementToAppear(successMessage).getAttribute(("textContent"));
    }

    /**
     * Closes modal message
     *
     * @return this page object
     */
    public CustomerWorkspacePage closeMessage() {
        closeMessage.click();
        return this;
    }
}
