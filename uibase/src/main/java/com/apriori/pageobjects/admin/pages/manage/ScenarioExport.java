package com.apriori.pageobjects.admin.pages.manage;

import com.apriori.pageobjects.admin.header.AdminHeader;
import com.apriori.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScenarioExport extends AdminHeader {

    private final Logger logger = LoggerFactory.getLogger(ScenarioExport.class);

    @FindBy(css = "h1")
    private WebElement manageScenarioExportTitle;

    @FindBy(css = "[id='ToolTables_exportscheduleslist_0']")
    private WebElement newExportButton;

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
        pageUtils.waitForElementToAppear(newExportButton);
        pageUtils.waitForElementToAppear(exportTable);
    }

    public boolean isHeaderDisplayed() {
        pageUtils.waitForElementToAppear(manageScenarioExportTitle);
        return pageUtils.isElementDisplayed(manageScenarioExportTitle);
    }

    public boolean isHeaderEnabled() {
        pageUtils.waitForElementToAppear(manageScenarioExportTitle);
        return pageUtils.isElementEnabled(manageScenarioExportTitle);
    }

    /**
     * Opens the new export set dialog
     *
     * @return new page object
     */
    public NewExportSet clickNew() {
        pageUtils.waitForElementAndClick(newExportButton);
        return new NewExportSet(driver);
    }
}
