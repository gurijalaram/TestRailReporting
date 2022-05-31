package com.apriori.pageobjects.pages.userguides;

import com.apriori.pageobjects.header.AdminPageHeader;
import com.apriori.utils.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

@Slf4j
public class CiaUserGuide extends AdminPageHeader {

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
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
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
