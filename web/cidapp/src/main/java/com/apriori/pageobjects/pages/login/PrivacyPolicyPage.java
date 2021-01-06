package com.apriori.pageobjects.pages.login;

import com.apriori.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author cfrith
 */

public class PrivacyPolicyPage extends LoadableComponent<PrivacyPolicyPage> {

    private final Logger LOGGER = LoggerFactory.getLogger(PrivacyPolicyPage.class);

    @FindBy(id = "menu-main-menu")
    private WebElement mainMenu;

    @FindBy(css = "section[id='services_title_section'] > div > h1")
    private WebElement heading;

    @FindBy(css = "a[href='https://www.apriori.com']")
    private WebElement logo;

    private WebDriver driver;
    private PageUtils pageUtils;

    public PrivacyPolicyPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        LOGGER.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        isPageLogoDisplayed();
    }

    /**
     * Gets page heading
     *
     * @return - string
     */
    public String getPageHeading() {
        pageUtils.windowHandler(1);
        return heading.getText();
    }

    /**
     * Gets page logo
     *
     * @return - webelement
     */
    public WebElement isPageLogoDisplayed() {
        pageUtils.windowHandler(1);
        return pageUtils.waitForElementAppear(logo);
    }

    /**
     * Gets window url
     *
     * @return - string
     */
    public String getChildWindowURL() {
        return pageUtils.getTabTwoUrl();
    }

    /**
     * Gets count of open tabs
     *
     * @return int - open tab count
     */
    public int getTabCount() {
        return pageUtils.getCountOfOpenTabs();
    }
}