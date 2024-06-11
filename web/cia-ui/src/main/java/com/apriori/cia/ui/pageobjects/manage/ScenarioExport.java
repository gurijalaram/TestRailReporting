package com.apriori.cia.ui.pageobjects.manage;

import com.apriori.cia.ui.pageobjects.header.AdminHeader;
import com.apriori.web.app.util.PageUtils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.time.Duration;

@Slf4j
public class ScenarioExport extends AdminHeader {

    @FindBy(css = "h1")
    private WebElement manageScenarioExportTitle;

    @FindBy(css = "[id='exportscheduleslist']")
    private WebElement exportTable;

    @FindBy(xpath = "//a[text()='View History']")
    private WebElement viewHistoryTab;

    @FindBy(xpath = "//a[@aria-controls='exporthistorieslist']")
    private WebElement refreshButton;

    @FindBy(xpath = "//input[@id='hist-export-name']")
    private WebElement exportSetNameContainsInput;

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


    /**
     * Click view history
     *
     * @return ScenarioExport popup
     */
    public ScenarioExport clickViewHistoryTab() {
        pageUtils.waitForElementAndClick(viewHistoryTab);
        return this;
    }

    /**
     * Validate that the status is success for created export set
     * @param exportSetName
     * @return
     */
    public ScenarioExport validateStatusIsSuccessForExportSet(final String exportSetName) {
        pageUtils.waitForElementToBeClickable(exportSetNameContainsInput);
        exportSetNameContainsInput.sendKeys(exportSetName);
        pageUtils.waitForElementAndClick(By.xpath("//button[@class='btn btn-primary']"));

        waitForElementInTable(By.xpath(String.format("//td[@class=' truncated' and text()='%s']", exportSetName)));

        waitForElementInTable(By.xpath("//span[text()='Success']"));

        return this;
    }

    private void waitForElementInTable(By elementToCheck) {
        Duration webDriverWait = Duration.ofMinutes(1);
        int retries = 0;
        int maxRetries = 4;

        while (retries < maxRetries) {
            try {
                retries++;
                pageUtils.waitForElementToAppear(elementToCheck, webDriverWait);
                return;

            } catch (TimeoutException e) {
                log.info("Refreshing tab to get last exportSet");
                log.debug(e.getMessage());
                pageUtils.waitForElementAndClick(refreshButton);
            }
        }

        throw new RuntimeException("Refreshing ExportSet table");
    }
}
