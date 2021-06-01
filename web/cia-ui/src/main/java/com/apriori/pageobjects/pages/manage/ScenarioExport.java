package com.apriori.pageobjects.pages.manage;

import com.apriori.pageobjects.header.AdminHeader;
import com.apriori.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pageobjects.pages.manage.NewExportSet;

public class ScenarioExport extends AdminHeader {

    private static final Logger logger = LoggerFactory.getLogger(ScenarioExport.class);

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
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
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
