package com.apriori.pageobjects.cirpages;

import com.apriori.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CirUserGuidePage {

    private static final Logger logger = LoggerFactory.getLogger(CirUserGuidePage.class);

    @FindBy(xpath = "//title[contains(text(), 'Cost Insight Report:User Guide')]")
    private WebElement pageTitle;

    @FindBy(css = "iframe[id='page_iframe']")
    private WebElement mainContentIframe;

    @FindBy(xpath = "//div[@id='mc-main-content']")
    private WebElement reportsUserGuideTitle;

    @FindBy(css = "body > h1")
    private WebElement heading;

    @FindBy(css = "body")
    private WebElement pageBody;

    private WebDriver driver;
    private PageUtils pageUtils;

    public CirUserGuidePage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
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
     * Gets count of open tabs
     *
     * @return int
     */
    public int getTabCount() {
        return pageUtils.getCountOfOpenTabs();
    }

    /**
     * Gets page heading of Reports Help page
     *
     * @return String - page title
     */
    public String getReportsUserGuidePageHeading() {
        pageUtils.waitForElementToAppear(reportsUserGuideTitle);
        return reportsUserGuideTitle.getAttribute("innerText");
    }

    /**
     * Switches tab using window handler
     *
     * @return new CirUserGuide page object
     */
    public CirUserGuidePage switchTab() {
        pageUtils.windowHandler(1);
        return new CirUserGuidePage(driver);
    }
}
