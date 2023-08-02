package com.apriori.pageobjects.help;

import com.apriori.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;

@Slf4j
public class ZendeskSignInPage extends LoadableComponent<ZendeskSignInPage> {

    @FindBy(css = "input[name='email']")
    private WebElement email;

    @FindBy(xpath = "//div[@class='auth0-lock-header-bg auth0-lock-blur-support']")
    private WebElement aprioriSupportLogo;

    @FindBy(xpath = "//div[@title='Zendesk']")
    private WebElement zendeskLabel;

    private  WebDriver driver;
    private  PageUtils pageUtils;

    public ZendeskSignInPage(WebDriver driver) {
        this(driver, log);
    }

    public ZendeskSignInPage(WebDriver driver, Logger logger) {
        PageFactory.initElements(driver, this);
        this.pageUtils = new PageUtils(driver);
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {
    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.windowHandler(1);
    }

    /**
     * Switches from parent to child tab
     *
     * @return page object
     */
    public ZendeskSignInPage switchTab() {
        pageUtils.windowHandler(1);
        return this;
    }

    /**
     * Checks if zendesk displayed
     *
     * @return true/false
     */
    public boolean isSupportLogoDisplayed() {
        return pageUtils.isElementDisplayed(aprioriSupportLogo);
    }

    /**
     * Gets the current url
     *
     * @return string
     */
    public String getCurrentUrl() {
        return pageUtils.getTabTwoUrl();
    }

    /**
     * Checks if zendesk label displayed
     *
     * @return true/false
     */
    public boolean isZendeskLabelDisplayed() {
        return pageUtils.waitForElementToAppear(zendeskLabel).isDisplayed();
    }
}
