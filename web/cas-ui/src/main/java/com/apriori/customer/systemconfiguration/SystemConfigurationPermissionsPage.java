package com.apriori.customer.systemconfiguration;

import com.apriori.utils.web.components.EagerPageComponent;

import com.apriori.utils.web.components.SelectionTreeComponent;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Represents the page object model of the system configuration permissions page.
 */
@Slf4j
public final class SystemConfigurationPermissionsPage extends EagerPageComponent<SystemConfigurationPermissionsPage> {
    @FindBy(css = ".system-configuration-permissions .selection-tree")
    private WebElement permissionsListRoot;
    private final SelectionTreeComponent permissionsList;

    /**
     * Initializes a new instance of this object.
     *
     * @param driver The web driver.
     */
    public SystemConfigurationPermissionsPage(WebDriver driver) {
        super(driver, log);
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
