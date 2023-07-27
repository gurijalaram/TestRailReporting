package com.apriori.pageobjects.pages.manage;

import com.apriori.PageUtils;
import com.apriori.pageobjects.header.AdminHeader;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

@Slf4j
public class ScenarioExport extends AdminHeader {

    @FindBy(css = "h1")
    private WebElement manageScenarioExportTitle;

    @FindBy(css = "[id='exportscheduleslist']")
    private WebElement exportTable;

    private WebDriver driver;
    private PageUtils pageUtils;

    public ScenarioExport(WebDriver driver) {
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
        pageUtils.waitForElementToAppear(exportTable);
    }

    /**
     * Checks if header is displayed
     *
     * @return boolean - is element displayed
     */
    public boolean isHeaderDisplayed() {
        pageUtils.waitForElementToAppear(manageScenarioExportTitle);
        return pageUtils.isElementDisplayed(manageScenarioExportTitle);
    }

    /**
     * Checks if header is enabled
     *
     * @return boolean - is element enabled
     */
    public boolean isHeaderEnabled() {
        pageUtils.waitForElementToAppear(manageScenarioExportTitle);
        return pageUtils.isElementEnabled(manageScenarioExportTitle);
    }
}
