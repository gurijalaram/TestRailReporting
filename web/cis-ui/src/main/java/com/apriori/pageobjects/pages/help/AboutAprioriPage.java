package com.apriori.pageobjects.pages.help;

import com.apriori.utils.web.components.EagerPageComponent;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Slf4j
public class AboutAprioriPage extends EagerPageComponent<AboutAprioriPage> {

    @FindBy(id = "about_us_section")
    private WebElement aboutUs;

    @FindBy(css = ".red-btn")
    private WebElement termsButton;

    public AboutAprioriPage(WebDriver driver) {
        super(driver, log);
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        getPageUtils().waitForElementToAppear(aboutUs);
    }

    /**
     * Clicks on the Yes I Agree button
     *
     * @return page object
     */
    public AboutAprioriPage agreeTermsAndCondition() {
        getPageUtils().waitForElementAndClick(termsButton);
        return this;
    }

    /**
     * Switches from parent to child tab
     *
     * @return page object
     */
    public AboutAprioriPage switchTab() {
        getPageUtils().windowHandler(1);
        return this;
    }
}
