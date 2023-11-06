package com.apriori.cia.ui.pageobjects.cirpages;

import com.apriori.web.app.util.PageUtils;

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

    @FindBy(css = ".DocumentationCoverPageTitle")
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
        return reportsUserGuideTitle.getAttribute("textContent");
    }

    /**
     * Ensures page is loaded before continuing
     */
    public CirUserGuidePage ensurePageIsLoaded() {
        pageUtils.waitForElementToAppear(heading);
        pageUtils.waitForElementToBeClickable(heading);
        return this;
    }

    /**
     * Gets page heading
     *
     * @return - string
     */
    public String getPageHeading() {
        return heading.getText();
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
     * Switches to iframe within a page by its "id" value
     *
     * @param iframeId - iframe id attribute
     * @return new CirUserGuide page object
     */
    public CirUserGuidePage switchToIFrameUserGuide(String iframeId) throws Exception {
        pageUtils.isElementEnabled(pageTitle);

        if (pageBody.getAttribute("className").startsWith("error404")) {
            throw new Exception("Link broken. Wrong page was opened - iframe wasn't found as a result");
        } else {
            driver.switchTo().frame(iframeId);
        }

        return new CirUserGuidePage(driver);
    }

    /**
     * Switches tab using window handler
     *
     * @return new CirUserGuide page object
     */
    public CirUserGuidePage switchTab() {
        pageUtils.switchToWindow(1);
        return new CirUserGuidePage(driver);
    }
}
