package com.apriori.customer.systemconfiguration;

import com.apriori.utils.web.components.EagerPageComponent;

import org.openqa.selenium.WebDriver;
import org.slf4j.LoggerFactory;

/**
 * Represents the page object model of the system configuration permissions page.
 */
public class SystemConfigurationPermissionsPage extends EagerPageComponent<SystemConfigurationPermissionsPage> {

    /**
     * Initializes a new instance of this object.
     *
     * @param driver The web driver.
     */
    public SystemConfigurationPermissionsPage(WebDriver driver) {
        super(driver, LoggerFactory.getLogger(SystemConfigurationPermissionsPage.class));
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
