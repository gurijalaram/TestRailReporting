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

    /**
     *
     * @return
     */
    public boolean isHeaderDisplayed() {
        pageUtils.waitForElementToAppear(manageScenarioExportTitle);
        return pageUtils.isElementDisplayed(manageScenarioExportTitle);
    }

    /**
     *
     * @return
     */
    public boolean isHeaderEnabled() {
        pageUtils.waitForElementToAppear(manageScenarioExportTitle);
        return pageUtils.isElementEnabled(manageScenarioExportTitle);
    }

    /**
     *
     * @return
     */
    public String getHeaderText() {
        pageUtils.waitForElementToAppear(manageScenarioExportTitle);
        return manageScenarioExportTitle.getText();
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
