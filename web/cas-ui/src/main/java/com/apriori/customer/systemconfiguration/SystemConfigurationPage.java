package com.apriori.customer.systemconfiguration;

import com.apriori.utils.web.components.EagerPageComponent;

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
    private WebElement groupsTab;

    @FindBy(css = ".system-configuration-tab-permissions a")
    private WebElement permissionsTab;

    /**
     * Initializes a new instance of this object.
     *
     * @param driver The web driver.
     */
    public SystemConfigurationPage(WebDriver driver) {
        super(driver, log);
    }

    /**
     * Clicks on the Groups tab.
     *
     * @return The groups page object model.
     */
    public SystemConfigurationGroupsPage goToGroupsPage() {
        this.getPageUtils().waitForElementAndClick(groupsTab);
        return new SystemConfigurationGroupsPage(getDriver());
    }

    /**
     * Clicks on the Permissions tab.
     *
     * @return The permissions page object model.
     */
    public SystemConfigurationPermissionsPage goToPermissionsPage() {
        this.getPageUtils().waitForElementAndClick(permissionsTab);
        return new SystemConfigurationPermissionsPage(getDriver());
    }

    /**
     * Checks to make sure all the necessary tabs are loaded.
     *
     * @throws Error If any of the expected tabs are missing.
     */
    @Override
    protected void isLoaded() throws Error {
        this.getPageUtils().waitForElementToAppear(groupsTab);
        this.getPageUtils().waitForElementToAppear(permissionsTab);
    }
}
