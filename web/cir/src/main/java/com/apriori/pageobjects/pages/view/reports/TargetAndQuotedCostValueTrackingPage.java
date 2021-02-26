package com.apriori.pageobjects.pages.view.reports;

import com.apriori.utils.PageUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TargetAndQuotedCostValueTrackingPage extends GenericReportPage {

    private final Logger LOGGER = LoggerFactory.getLogger(TargetQuotedCostTrendReportPage.class);

    @FindBy(xpath = "//div[@id='projectRollup']//a")
    private WebElement projectRollupDropdown;

    @FindBy(xpath = "//div[@id='exportDate']//a")
    private WebElement exportDateDropdown;

    @FindBy(xpath = "//div[@class='jr-mSingleselect-dropdownContainer jr'][2]//ul")
    private WebElement exportDateOptionList;

    @FindBy(xpath = "(//span[contains(text(), 'Export Date:')])[1]/../following-sibling::td[2]/span")
    private WebElement exportDateOnReport;

    @FindBy(xpath = "(//*[contains(text(), 'IROBOT_18874')])[2]")
    private WebElement partNumberDetailsReport;

    private PageUtils pageUtils;
    private WebDriver driver;

    public TargetAndQuotedCostValueTrackingPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        LOGGER.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    /**
     * Gets final cost
     * @return String
     */
    public String getFinalCost() {
        By locator = By.xpath("//table[@class='jrPage']//tr[30]/td[23]/span");
        pageUtils.waitForElementToAppear(locator);
        return driver.findElement(locator).getText();
    }

    /**
     * Clicks project name link
     * @param index - index of link to click
     * @return current page object instance
     */
    public TargetAndQuotedCostValueTrackingPage clickProjectLink(String index) {
        By locator = By.xpath(String.format("//span[contains(text(), 'PROJECT %s')]/../..", index));
        pageUtils.scrollWithJavaScript(driver.findElement(locator), true);
        pageUtils.waitForElementAndClick(locator);
        pageUtils.waitForElementAndClick(locator);
        return this;
    }

    /**
     * Waits for correct project name to appear
     * @param index project name index
     * @return current page object instance
     */
    public TargetAndQuotedCostValueTrackingPage waitForCorrectProjectNameToAppear(String index) {
        By locator = By.xpath(String.format("//span[contains(text(), 'PROJECT %s')]/../..", index));
        pageUtils.waitForElementToAppear(locator);
        return this;
    }

    /**
     * Expand rollup drop-down
     *
     * @return current page object
     */
    public <T> T selectProjectRollup(Class<T> className, String rollupName) {
        if (!projectRollupDropdown.getAttribute("title").equals(rollupName)) {
            pageUtils.waitForElementAndClick(projectRollupDropdown);
            By rollupLocator = By.xpath(String.format("//li[@title='%s']/div/a", rollupName));
            pageUtils.waitForElementAndClick(rollupLocator);
        }
        return PageFactory.initElements(driver, className);
    }

    /**
     * Gets milestone name
     * @return String
     */
    public String getProjectName() {
        By locator = By.xpath("//span[contains(text(), 'Project Name:')]/../following-sibling::td[2]/span");
        return driver.findElement(locator).getText();
    }

    /**
     * Gets export date option count
     * @return String
     */
    public String getExportDateOptionCount() {
        pageUtils.waitForElementToAppear(exportDateDropdown);
        return exportDateOptionList.getAttribute("childElementCount");
    }

    /**
     * Gets currently selected export date
     * @return String
     */
    public String getSelectedExportDate() {
        pageUtils.waitForElementToAppear(exportDateDropdown);
        return exportDateDropdown.getAttribute("title");
    }

    /**
     * Gets export date from report
     * @return String
     */
    public String getExportDateOnReport() {
        return exportDateOnReport.getText();
    }

    /**
     * Gets part number from details report
     * @return String
     */
    public String getPartNumberFromDetailsReport() {
        pageUtils.waitForElementToAppear(partNumberDetailsReport);
        return partNumberDetailsReport.getText();
    }
}
