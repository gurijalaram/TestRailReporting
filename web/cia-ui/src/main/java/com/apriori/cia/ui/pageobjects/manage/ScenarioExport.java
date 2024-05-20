package com.apriori.cia.ui.pageobjects.manage;

import com.apriori.cia.ui.pageobjects.header.AdminHeader;
import com.apriori.web.app.util.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
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

    public ScenarioExport filterByExportSetName(final String scenarioName) {
        WebElement filterField = pageUtils.waitForElementAppear(driver.findElement(By.xpath("//input[ @aria-controls='exportscheduleslist']")));
        filterField.sendKeys(scenarioName);
        return clickRefreshButton();
    }

    public ScenarioExport clickRefreshButton() {
        pageUtils.waitForElementAndClick(By.xpath("//a[@id='ToolTables_exportscheduleslist_3']"));
        return this;
    }

    public ScenarioExport clickExportButton() {
        pageUtils.waitForElementAndClick(By.xpath("(//a[@class='btn btn-default btn-xs'])[1]"));
        return this;
    }

    public ScenarioExport validateExportNowPopupAndCloseIt() {
        pageUtils.waitForElementToAppear(By.xpath("//div[contains(text(), 'Export initiated for export set')]"));
        pageUtils.waitForElementAndClick(By.xpath("//div[@class='modal error-dialog fade out in']//button[@class='btn btn-primary btn-sm']"));
        return this;
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
