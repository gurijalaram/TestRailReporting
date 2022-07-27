package com.apriori.pageobjects.navtoolbars.help;

import com.apriori.utils.web.components.EagerPageComponent;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Slf4j
public class AboutUsPage extends EagerPageComponent<AboutUsPage> {

    @FindBy(css = ".fade-in-delay")
    private WebElement aboutUs;

    @FindBy(xpath = "//button [@class='cookie-ignore-btn']")
    private WebElement termsButton;

    public AboutUsPage(WebDriver driver) {
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
    public AboutUsPage agreeTermsAndCondition() {
        getPageUtils().waitForElementAndClick(termsButton);
        return this;
    }

    /**
     * Gets the page url
     *
     * @return String
     */
    public String getAboutUsPageUrl() {
        return getPageUtils().windowHandler(1).getCurrentUrl();
    }

    /**
     * Switches from parent to child tab
     *
     * @return page object
     */
    public AboutUsPage switchTab() {
        getPageUtils().windowHandler(1);
        return new AboutUsPage(getDriver());
    }

    /**
     * Gets About Us meta tag
     *
     * @return String
     */
    public String getAboutUsMetaTag() {
        return getPageUtils().waitForElementToAppear(aboutUs).getAttribute("textContent");
    }
}
