package com.apriori.pageobjects.pages.login;

import static org.junit.Assert.assertTrue;

import com.apriori.PageUtils;

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

    private static final Logger logger = LoggerFactory.getLogger(PrivacyPolicyPage.class);

    @FindBy(id = "menu-main-menu")
    private WebElement mainMenu;

    @FindBy(css = ".privacy-page h1")
    private WebElement aprioriHeading;

    @FindBy(css = "a img[alt='aPriori']")
    private WebElement aprioriLogo;

    private WebDriver driver;
    private PageUtils pageUtils;

    public PrivacyPolicyPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        assertTrue("aPriori logo is not displayed", isPageLogoDisplayed());
    }

    /**
     * Gets page heading
     *
     * @return - string
     */
    public String getPageHeading() {
        pageUtils.windowHandler(1);
        return aprioriHeading.getText();
    }

    /**
     * Gets page logo
     *
     * @return - webelement
     */
    public boolean isPageLogoDisplayed() {
        pageUtils.windowHandler(1);
        return pageUtils.waitForElementToAppear(aprioriLogo).isDisplayed();
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
