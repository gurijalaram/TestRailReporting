package com.apriori.customer.systemconfiguration;

import com.apriori.utils.properties.PropertiesContext;
import com.apriori.utils.web.components.EagerPageComponent;
import com.apriori.utils.web.components.RoutingComponent;
import com.apriori.utils.web.components.SelectComponent;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Represents the page object model for the root of the system configurations tab under a customer.
 */
@Slf4j
public final class SystemConfigurationPage extends EagerPageComponent<SystemConfigurationPage> {
    @FindBy(css = ".system-configuration-tab-groups a")
    private WebElement groupsTabRoot;
    private final RoutingComponent groupsTab;

    @FindBy(css = ".system-configuration-tab-permissions a")
    private WebElement permissionsTabRoot;
    private final RoutingComponent permissionsTab;

    @FindBy(css = ".site-drop-down")
    private WebElement siteDropDown;
    private SelectComponent siteSelectField;

    @FindBy(css = ".deployment-drop-down")
    private WebElement deploymentDropDown;
    private SelectComponent deploymentSelectField;

    /**
     * Initializes a new instance of this object.
     *
     * @param driver The web driver.
     */
    public SystemConfigurationPage(WebDriver driver) {
        super(driver, log);
        groupsTab = new RoutingComponent(getDriver(), groupsTabRoot);
        permissionsTab = new RoutingComponent(getDriver(), permissionsTabRoot);
    }

    /**
     * Clicks on the Groups tab.
     *
     * @return The groups page object model.
     */
    public SystemConfigurationGroupsPage goToGroupsPage() {
        groupsTab.navigate();
        return new SystemConfigurationGroupsPage(getDriver());
    }

    /**
     * Clicks on the Permissions tab.
     *
     * @return The permissions page object model.
     */
    public SystemConfigurationPermissionsPage goToPermissionsPage() {
        permissionsTab.navigate();
        return new SystemConfigurationPermissionsPage(getDriver());
    }

    /**
     * Checks to make sure all the necessary tabs are loaded.
     *
     * @throws Error If any of the expected tabs are missing.
     */
    @Override
    protected void isLoaded() throws Error {
        this.getPageUtils().waitForElementToAppear(groupsTabRoot);
        this.getPageUtils().waitForElementToAppear(permissionsTabRoot);
    }

    /**
     * Retrieves CustomerProfilePage for customer via URL and returns Page object.
     *
     * @param driver - WebDriver
     * @param customer - Customer ID
     * @return SystemConfigurationPage
     */
    public static SystemConfigurationPage getViaURL(WebDriver driver, String customer) {
        String url = PropertiesContext.get("cas.ui_url") + "customers/%s/system-configuration/";
        driver.navigate().to(String.format(url, customer));
        return new SystemConfigurationPage(driver);
    }

    /**
     * @return string name of selected site
     */
    public String getSiteInDropDown() {
        return siteDropDown.getAttribute("textContent");
    }

    /**
     * @return string name of selected deployment
     */
    public String getDeploymentInDropDown() {
        return deploymentDropDown.getAttribute("textContent");
    }

    /**
     * @param site - name of site to select
     * @return this object
     */
    public SystemConfigurationPage selectSite(String site) {
        this.siteSelectField = new SelectComponent(getDriver(), siteDropDown);
        siteSelectField.select(site);
        return this;
    }

    /**
     * @param deployment - name of deployment to select
     * @return - this object
     */
    public SystemConfigurationPage selectDeployment(String deployment) {
        this.deploymentSelectField = new SelectComponent(getDriver(), deploymentDropDown);
        deploymentSelectField.select(deployment);
        return this;
    }
}
