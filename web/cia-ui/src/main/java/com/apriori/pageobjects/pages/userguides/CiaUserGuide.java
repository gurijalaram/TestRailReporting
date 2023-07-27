package com.apriori.pageobjects.pages.userguides;

import com.apriori.PageUtils;
import com.apriori.pageobjects.header.AdminPageHeader;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

@Slf4j
public class CiaUserGuide extends AdminPageHeader {

    @FindBy(xpath = "//*[contains(text(), 'Cost Insight Admin Guide')])")
    private WebElement adminUserGuidePageTitle;

    @FindBy(xpath = "//*[contains(text(), 'Cost Insight Admin Guide')]")
    private WebElement scenarioExportChapterPageTitle;

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
    public String getAdminOrScenarioChapterUserGuidePageHeading(boolean getScenarioChapterTitle) {
        By locatorToUse = getScenarioChapterTitle ? By.xpath("//div[@id='mc-main-content']")
            : By.xpath("//*[contains(text(), 'Cost Insight Admin Guide')]");
        pageUtils.waitForElementToAppear(locatorToUse);
        return driver.findElement(locatorToUse).getAttribute("innerText");
    }
}
