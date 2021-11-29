package com.apriori.customer.users;

import com.apriori.utils.web.components.EagerPageComponent;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;

/**
 * Represents the page for aPriori Staff under the users tab.
 */
@Slf4j
public final class StaffPage extends EagerPageComponent<StaffPage> {
    /**
     * Initializes a new instance of this object.
     *
     * @param driver The web driver that the page exists on.
     */
    public StaffPage(WebDriver driver) {
        super(driver, log);
    }

    @Override
    protected void isLoaded() throws Error {
        // TODO
    }
}
