package com.apriori.customer.systemconfiguration;

import com.apriori.utils.web.components.EagerPageComponent;
import com.apriori.utils.web.components.SelectionTreeItem;

import org.openqa.selenium.WebDriver;
import org.slf4j.LoggerFactory;

/**
 * Represents the page object model for the system configuration groups.
 */
public final class SystemConfigurationGroupsPage extends EagerPageComponent<SystemConfigurationGroupsPage> {

    /**
     * Initializes a new instance of this object.
     *
     * @param driver The web driver.
     */
    public SystemConfigurationGroupsPage(WebDriver driver) {
        super(driver, LoggerFactory.getLogger(SystemConfigurationGroupsPage.class));
    }

    public SelectionTreeItem getRootGroup() {
        return null;
    }

    public SelectionTreeItem getSelectedGroup() {
        return null;
    }

    /**
     * Checks to make sure that the selection tree is available.
     *
     * @throws Error If the selection tree cannot be found.
     */
    @Override
    protected void isLoaded() throws Error {
        // TODO
    }
}
