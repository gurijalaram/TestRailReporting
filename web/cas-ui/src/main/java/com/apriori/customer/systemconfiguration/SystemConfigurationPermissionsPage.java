package com.apriori.customer.systemconfiguration;

import com.apriori.utils.web.components.EagerPageComponent;

import com.apriori.utils.web.components.SelectionTreeComponent;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.slf4j.LoggerFactory;

/**
 * Represents the page object model of the system configuration permissions page.
 */
public class SystemConfigurationPermissionsPage extends EagerPageComponent<SystemConfigurationPermissionsPage> {
    @FindBy(css = ".system-configuration-permissions .selection-tree")
    private WebElement permissionsListRoot;
    private SelectionTreeComponent permissionsList;

    /**
     * Initializes a new instance of this object.
     *
     * @param driver The web driver.
     */
    public SystemConfigurationPermissionsPage(WebDriver driver) {
        super(driver, LoggerFactory.getLogger(SystemConfigurationPermissionsPage.class));
        permissionsList = new SelectionTreeComponent(getDriver(), permissionsListRoot);
    }

    /**
     * Gets the permissions list.
     *
     * @return The permissions list.
     */
    public SelectionTreeComponent getPermissionsList() {
        return permissionsList;
    }

    /**
     * Checks to make sure that the selection tree is available.
     *
     * @throws Error If the selection tree cannot be found.
     */
    @Override
    protected void isLoaded() throws Error {
        getPageUtils().waitForElementToAppear(permissionsListRoot);
    }
}
