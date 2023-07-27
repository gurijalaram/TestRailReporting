package com.apriori.pageobjects.navtoolbars;

import com.apriori.EagerPageComponent;

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
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        getPageUtils().waitForElementToAppear(email);
    }

    /**
     * Gets the current url
     *
     * @return string
     */
    public String getCurrentUrl() {
        return getPageUtils().getTabTwoUrl();
    }
}
