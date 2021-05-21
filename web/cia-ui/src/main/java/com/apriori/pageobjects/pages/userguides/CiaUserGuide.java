package com.apriori.pageobjects.pages.userguides;

import com.apriori.pageobjects.header.PageHeader;
import com.apriori.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CiaUserGuide extends PageHeader {

    private static final Logger logger = LoggerFactory.getLogger(CiaUserGuide.class);

    @FindBy(css = "div[id='page_content'] > div")
    private WebElement pageTitle;

    @FindBy(css = "iframe[id='page_iframe']")
    private WebElement mainContentIframe;

    private WebDriver driver;
    private PageUtils pageUtils;

    public CiaUserGuide(WebDriver driver) {
        super(driver);
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

    }

    /**
     * Gets count of open tabs
     *
     * @return int - open tab count
     */
    public int getTabCount() {
        return pageUtils.getCountOfOpenTabs();
    }

    /**
     * Gets current URL of new tab
     *
     * @return String
     */
    public String getCurrentUrl() {
        return pageUtils.getTabTwoUrl();
    }

    /**
     * Gets page heading of Admin User Guide page
     * @return String - page title
     */
    public String getAdminUserGuidePageHeading() {
        pageUtils.windowHandler(1);
        pageUtils.waitForElementToAppear(mainContentIframe);
        driver.switchTo().frame(mainContentIframe);
        pageUtils.waitForElementAppear(pageTitle);
        return pageTitle.getText();
    }
}
