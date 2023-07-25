package com.apriori.pageobjects.pages.view.reports;

import com.apriori.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AssemblyCostReportPage extends GenericReportPage {

    private static final Logger logger = LoggerFactory.getLogger(AssemblyCostReportPage.class);

    @FindBy(xpath = "//label[@title='Single export set selection.']/div/div/div/a")
    private WebElement exportSetDropdown;

    @FindBy(xpath = "//label[@title='Single [assembly] part number selection.']/div/div/div/a/div")
    private WebElement assemblyPartNumberDropdown;

    @FindBy(xpath = "//label[@title='Single scenario name selection.']/div/div/div/a")
    private WebElement scenarioNameDropdown;

    @FindBy(xpath = "//li[@title='TOP-LEVEL']/..")
    private WebElement assemblyPartNumberDropdownItemList;

    @FindBy(xpath = "//li[@title='Initial']/..")
    private WebElement scenarioNameItemList;

    private final String generalCostInfoValueLocator = "//span[contains(text(), '%s')]/../following-sibling::td[%s]/span";
    private final String dropdownOptionLocator = "li[title='%s'] > div > a";

    private final PageUtils pageUtils;
    private final WebDriver driver;

    public AssemblyCostReportPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    /**
     * Selects Export Set from Dropdown
     *
     * @param exportSet String
     * @return instance of class
     */
    public AssemblyCostReportPage selectExportSetDropdown(String exportSet) {
        if (!exportSetDropdown.getAttribute("title").equals(exportSet)) {
            pageUtils.waitForElementAndClick(exportSetDropdown);
            By locator = By.cssSelector(String.format(dropdownOptionLocator, exportSet));
            pageUtils.waitForElementAndClick(locator);
        }
        return this;
    }

    /**
     * Selects Assembly from Dropdown
     *
     * @param assemblyName String
     * @return instance of class
     */
    public AssemblyCostReportPage selectAssemblySetDropdown(String assemblyName) {
        if (!getCurrentAssemblyPartNumber().equals(assemblyName)) {
            pageUtils.waitForElementAndClick(assemblyPartNumberDropdown);
            By locatorToUse = By.cssSelector(String.format(dropdownOptionLocator, assemblyName));
            pageUtils.waitForElementAndClick(locatorToUse);
        }
        return this;
    }

    /**
     * Waits for correct assembly part number
     *
     * @param assemblyName String - assembly to wait for
     */
    public void waitForCorrectAssemblyPartNumber(String assemblyName) {
        pageUtils.waitForElementToAppear(
                By.xpath(String.format("//div[@id='partNumber']//a[@title='%s']", assemblyName)));
    }

    /**
     * Selects Assembly from Dropdown
     *
     * @param scenarioName String
     * @return instance of class
     */
    public AssemblyCostReportPage selectScenarioNameDropdown(String scenarioName) {
        if (!scenarioNameDropdown.getAttribute("title").equals(scenarioName)) {
            pageUtils.waitForElementAndClick(scenarioNameDropdown);
            By locator = By.cssSelector(String.format(dropdownOptionLocator, scenarioName));
            pageUtils.waitForElementAndClick(locator);
        }
        return this;
    }

    /**
     * Waits for Assembly Part Number filter to occur
     *
     * @return instance of current page object
     */
    public AssemblyCostReportPage waitForAssemblyPartNumberFilter(String assemblyName) {
        By locator = By.xpath("//a[@title='TOP-LEVEL']");
        pageUtils.waitForElementToAppear(locator);
        return this;
    }

    /**
     * Gets count of Assembly Part Numbers available
     *
     * @return String
     */
    public String getAssemblyPartNumberFilterItemCount() {
        return assemblyPartNumberDropdownItemList.getAttribute("childElementCount");
    }

    /**
     * Gets count of Scenario Names available
     *
     * @return String
     */
    public String getScenarioNameCount() {
        return scenarioNameItemList.getAttribute("childElementCount");
    }

    /**
     * Checks if a particular Assembly Part Number option is enabled
     *
     * @param itemName String - item to check
     * @return boolean
     */
    public boolean isAssemblyPartNumberItemEnabled(String itemName) {
        return driver.findElement(By.cssSelector(String.format(dropdownOptionLocator, itemName))).isEnabled();
    }

    /**
     * Gets currently selected Assembly Part Number
     *
     * @return String
     */
    public String getCurrentAssemblyPartNumber() {
        By locator = By.xpath("//label[@title='Single [assembly] part number selection.']/div/div/div/a");
        return driver.findElement(locator).getAttribute("title");
    }

    /**
     * Checks if a particular Scenario Name is enabled
     *
     * @param scenarioName String - Scenario Name to check
     * @return boolean
     */
    public boolean isScenarioNameEnabled(String scenarioName) {
        return driver.findElement(By.cssSelector(String.format(dropdownOptionLocator, scenarioName))).isEnabled();
    }

    /**
     * Gets General Cost Info value
     *
     * @param valueName String - value to get
     * @return String
     */
    public String getGeneralCostInfoValue(String valueName, boolean isGeneralCostValue) {
        String tdIndex = isGeneralCostValue ? "2" : "1";
        By locator = By.xpath(String.format(generalCostInfoValueLocator, valueName, tdIndex));
        return driver.findElement(locator).getText();
    }
}
