package com.apriori.pageobjects.pages.help;

import com.apriori.utils.web.components.EagerPageComponent;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Slf4j
public class ZendeskSignInPage extends EagerPageComponent<ZendeskSignInPage> {

    @FindBy(css = "input[name='email']")
    private WebElement email;

    public ZendeskSignInPage(WebDriver driver) {
        super(driver, log);
    }

    @Override
    protected void isLoaded() throws Error {
        getPageUtils().waitForElementToAppear(email);
    }

    /**
     * Switches from parent to child tab
     *
     * @return page object
     */
    public ZendeskSignInPage switchTab() {
        getPageUtils().windowHandler(1);
        return this;
    }
}
