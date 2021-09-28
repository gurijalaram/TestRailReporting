package com.apriori.pageobjects.navtoolbars.help;

import com.apriori.utils.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

@Slf4j
public class AboutUsPage extends LoadableComponent<AboutUsPage> {

    @FindBy(id = "about_us_section")
    private WebElement aboutUs;

    @FindBy(css = ".red-btn")
    private WebElement termsButton;

    private final WebDriver driver;
    private final PageUtils pageUtils;

    public AboutUsPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementToAppear(aboutUs);
    }

    /**
     * Clicks on the Yes I Agree button
     *
     * @return page object
     */
    public AboutUsPage agreeTermsAndCondition() {
        pageUtils.waitForElementAndClick(termsButton);
        return this;
    }

    /**
     * Gets the page url
     *
     * @return String
     */
    public String getAboutUsPageUrl() {
        return pageUtils.windowHandler(1).getCurrentUrl();
    }

    /**
     * Switches from parent to child tab
     *
     * @return page object
     */
    public AboutUsPage switchTab() {
        pageUtils.windowHandler(1);
        return new AboutUsPage(driver);
    }

    /**
     * Gets About Us meta tag
     *
     * @return String
     */
    public String getAboutUsMetaTag() {
        return pageUtils.waitForElementToAppear(aboutUs).getAttribute("textContent");
    }
}
