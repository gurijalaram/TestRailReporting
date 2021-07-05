package com.apriori.pageobjects.pages.view.reports;

import com.apriori.utils.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class CycleTimeValueTrackingPage extends GenericReportPage {

    private static final Logger logger = LoggerFactory.getLogger(CycleTimeValueTrackingPage.class);

    @FindBy(xpath = "//div[contains(@class, 'dropdownContainer')][1]//ul/li[1]")
    private WebElement projectRollupDropdownFirstElement;

    @FindBy(xpath = "//span[contains(text(), 'Project Tracking Rollup:')]/../following-sibling::td[2]")
    private WebElement projectTrackingRollupAboveChart;

    @FindBy(xpath = "(//td[@colspan='4'])[24]//span")
    private WebElement projectTrackingRollupInChart;

    @FindBy(xpath = "//label[@title='Projects Rollup']//a")
    private WebElement projectRollupDropdown;

    @FindBy(xpath = "//span[contains(text(), 'Project Name:')]/../following-sibling::td[2]")
    private WebElement projectName;

    @FindBy(xpath = "//*[local-name()='svg']")
    private WebElement chartSvg;

    @FindBy(xpath = "//table[@class='jrPage']/tbody/tr[3]/td[2]/span")
    private WebElement reportTitle;

    @FindBy(xpath = "//table[contains(@class, 'jrPage')]/tbody/tr[13]/td[3]/span/span")
    private WebElement firstRowPartNumber;

    @FindBy(xpath = "//span[contains(text(), 'Steel')]")
    private WebElement cycleTimeValueTrackingDetailsMaterialComposition;

    private final String genericRollupListLocator = "//div[contains(@class, 'dropdownContainer')][1]//ul/li[%s]";
    private final Map<String, String> valueIndexes = new HashMap<String, String>();

    private final PageUtils pageUtils;
    private final WebDriver driver;

    public CycleTimeValueTrackingPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        intialiseValueIndexesMap();
    }

    /**
     * Gets count of items in a dropdown on either report
     *
     * @return String
     */
    public String getCountOfDropdownItems(String index) {
        By locator = By.xpath(String.format("//div[contains(@class, 'dropdownContainer')][%s]//ul", index));
        return driver.findElement(locator).getAttribute("childElementCount");
    }

    /**
     * Gets name of first rollup
     *
     * @return String
     */
    public String getFirstRollupName() {
        By locator = By.xpath(String.format(genericRollupListLocator, "1"));
        return driver.findElement(locator).getAttribute("title");
    }

    /**
     * Gets rollup in use as displayed above chart
     *
     * @return String
     */
    public String getRollupInUseAboveChart() {
        pageUtils.waitForElementToAppear(projectTrackingRollupAboveChart);
        return projectTrackingRollupAboveChart.getText();
    }

    /**
     * Gets rollup in use as displayed in bottom chart
     *
     * @return String
     */
    public String getRollupInUseInChart() {
        return projectTrackingRollupInChart.getText();
    }

    /**
     * Selects project rollup
     *
     * @return instance of current page object
     */
    public CycleTimeValueTrackingPage selectProjectRollup() {
        pageUtils.waitForElementAndClick(projectRollupDropdown);
        By locator = By.xpath(String.format(genericRollupListLocator, "2"));
        pageUtils.waitForElementAndClick(locator);
        By locator2 = By.xpath("//a[@title='PROJECT 1']");
        pageUtils.waitForElementToAppear(locator2);
        return this;
    }

    /**
     * Gets project name
     *
     * @return String
     */
    public String getProjectName() {
        pageUtils.waitForElementToAppear(projectName);
        return projectName.getText();
    }

    /**
     * Gets Cycle Time Value Tracking or Details report title
     *
     * @return String
     */
    public String getCycleTimeReportTitle() {
        return reportTitle.getText();
    }

    /**
     * Clicks specified project name
     *
     * @param name - String
     */
    public <T> T clickHyperlink(String name, Class<T> className) {
        pageUtils.waitForElementToAppear(chartSvg);
        By locator = By.xpath(String.format("//span[contains(text(), '%s')]", name));
        pageUtils.scrollWithJavaScript(driver.findElement(locator), true);
        pageUtils.waitForSteadinessOfElement(locator);
        pageUtils.waitForElementAndClick(locator);
        return PageFactory.initElements(driver, className);
    }

    /**
     * Gets Part Number
     *
     * @return String
     */
    public String getPartNumber() {
        pageUtils.waitForElementToAppear(firstRowPartNumber);
        return firstRowPartNumber.getText();
    }

    /**
     * Gets specified value from the Report
     *
     * @param valueToGet - String
     * @return String
     */
    public String getReportsValue(String valueToGet) {
        By locator = By.xpath(
                String.format(
                        "//table[contains(@class, 'jrPage')]/tbody/tr[13]/td[%s]/span",
                        valueIndexes.get(valueToGet))
        );
        pageUtils.waitForElementToAppear(locator);
        return driver.findElement(locator).getAttribute("textContent");
    }

    /**
     * Waits for tab switch to occur from Cycle Time Value Tracking to Details or Component Cost Reports
     */
    public void waitForNewTabSwitchCycleTimeToDetailsOrComponentCost() {
        pageUtils.waitForElementToAppear(cycleTimeValueTrackingDetailsMaterialComposition);
    }

    private void intialiseValueIndexesMap() {
        valueIndexes.put("Scenario Name", "7");
        valueIndexes.put("Finish Mass", "13");
        valueIndexes.put("Process Group", "17");
        valueIndexes.put("Material Composition", "20");
        valueIndexes.put("Annual Volume", "25");
        valueIndexes.put("Final Cycle Time", "29");
    }
}
