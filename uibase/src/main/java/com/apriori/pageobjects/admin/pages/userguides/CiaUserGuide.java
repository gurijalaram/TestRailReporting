package com.apriori.pageobjects.admin.pages.userguides;

import com.apriori.pageobjects.admin.header.PageHeader;
import com.apriori.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CiaUserGuide extends PageHeader {
    private Logger logger = LoggerFactory.getLogger(CiaUserGuide.class);

    @FindBy(xpath = "//*[contains(text(), 'Cost Insight Report:User Guide')]")
    private WebElement pageTitle;

    @FindBy(css = "iframe[id='page_iframe']")
    private WebElement mainContentIframe;

    @FindBy(css = ".aPriori_Cover_Page_Title")
    private WebElement adminUserGuideTitle;

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
     *
     * @return String - page title
     */
    public String getAdminUserGuidePageHeading() {
        pageUtils.windowHandler();
        pageUtils.waitForElementToAppear(mainContentIframe);
        driver.switchTo().frame(mainContentIframe);
        pageUtils.waitForElementAppear(adminUserGuideTitle);
        String retVal = adminUserGuideTitle.getAttribute("textContent");
        return retVal;
    }
}
