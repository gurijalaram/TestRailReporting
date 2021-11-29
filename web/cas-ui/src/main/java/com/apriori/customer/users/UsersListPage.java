package com.apriori.customer.users;

import com.apriori.utils.properties.PropertiesContext;
import com.apriori.utils.web.components.EagerPageComponent;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;

/**
 * Represents the page under the users tab that contains the users list.
 */
@Slf4j
public class UsersListPage extends EagerPageComponent<UsersListPage> {
    /**
     * @inheritDoc
     */
    public UsersListPage(WebDriver driver) {
        super(driver, log);
    }

    /**
     * inheritDoc
     */
    @Override
    protected void isLoaded() throws Error {
        // TODO: This page needs a refactor as the structure has changed
    }

    /**
     * Retrieves UsersListPage for customer via URL and returns Page object.
     *
     * @param driver - WebDriver
     * @param customer - Customer ID
     * @return UsersListPage
     */
    public static UsersListPage getViaURL(WebDriver driver, String customer) {
        String url = PropertiesContext.get("${env}.cas.ui_url") + "customers/%s/users/customer-staff";
        driver.navigate().to(String.format(url, customer));
        return new UsersListPage(driver);
    }
}
