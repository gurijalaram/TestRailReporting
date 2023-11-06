package com.apriori.cir.ui.pageobjects.view.reports;

import com.apriori.web.app.util.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TargetAndQuotedCostValueTrackingPage extends GenericReportPage {

    private static final Logger logger = LoggerFactory.getLogger(TargetQuotedCostTrendReportPage.class);

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

    private final PageUtils pageUtils;
    private final WebDriver driver;

    public TargetAndQuotedCostValueTrackingPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    /**
     * Gets final cost
     *
     * @return String
     */
    public String getFinalCost() {
        pageUtils.waitForElementToAppear(By.xpath("//tr[@style='height:3px']//span[contains(text(), 'PROJECT 2')]/ancestor::tr"));
        By locator = By.xpath("//table[@class='jrPage']//tr[29]/td[23]/span");
        pageUtils.waitForElementToAppear(locator);
        return driver.findElement(locator).getText();
    }

    /**
     * Clicks project name link
     *
     * @param index - index of link to click
     * @return current page object instance
     */
    public TargetAndQuotedCostValueTrackingPage clickProjectLink(String index) {
        By locator = By.xpath(String.format("//span[.='PROJECT %s']/span", index));
        pageUtils.isPageLoaded(driver.findElement(By.xpath("//body")));
        pageUtils.waitForElementNotDisplayed(loadingPopup, 1);
        pageUtils.scrollWithJavaScript(driver.findElement(locator), true);
        pageUtils.waitForSteadinessOfElement(locator);
        if (pageUtils.isElementEnabled(driver.findElement(locator))) {
            pageUtils.waitForElementAndClick(locator);
        }
        return this;
    }

    /**
     * Waits for correct project name to appear
     *
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
     * @param rollupName String
     * @return current page object
     */
    public TargetAndQuotedCostValueTrackingPage selectProjectRollup(String rollupName) {
        if (!projectRollupDropdown.getAttribute("title").equals(rollupName)) {
            pageUtils.waitForElementAndClick(projectRollupDropdown);
            By rollupLocator = By.xpath(String.format("//li[@title='%s']/div/a", rollupName));
            pageUtils.waitForElementAndClick(rollupLocator);
        }
        return this;
    }

    /**
     * Gets milestone name
     *
     * @return String
     */
    public String getProjectName() {
        By locator = By.xpath("//span[contains(text(), 'Project Name:')]/../following-sibling::td[2]/span");
        pageUtils.waitForElementToAppear(locator);
        return driver.findElement(locator).getText();
    }

    /**
     * Gets export date option count
     *
     * @return String
     */
    public String getExportDateOptionCount() {
        pageUtils.waitForElementToAppear(exportDateDropdown);
        return exportDateOptionList.getAttribute("childElementCount");
    }

    /**
     * Gets currently selected export date
     *
     * @return String
     */
    public String getSelectedExportDate() {
        pageUtils.waitForElementToAppear(exportDateDropdown);
        return exportDateDropdown.getAttribute("title");
    }

    /**
     * Gets export date from report
     *
     * @return String
     */
    public String getExportDateOnReport() {
        return exportDateOnReport.getText();
    }

    /**
     * Gets part number from details report
     *
     * @return String
     */
    public String getPartNumberFromDetailsReport() {
        pageUtils.waitForElementToAppear(partNumberDetailsReport);
        return partNumberDetailsReport.getText();
    }
}
