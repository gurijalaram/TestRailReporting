package com.apriori.pageobjects.pages.view.reports;

import com.apriori.utils.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

public class TargetQuotedCostTrendReportPage extends GenericReportPage {

    private final Logger LOGGER = LoggerFactory.getLogger(TargetQuotedCostTrendReportPage.class);

    @FindBy(xpath = "//div[@id='projectRollup']//a")
    private WebElement projectRollupDropdown;

    @FindBy(xpath = "(//div[@class='jr-mSingleselect-dropdownContainer jr'])[1]//ul/li[1]")
    private WebElement projectRollupDropdownFirstOption;

    @FindBy(xpath = "//div[@id='projectName']//a")
    private WebElement projectNameDropdown;

    @FindBy(xpath = "(//div[@class='jr-mSingleselect-dropdownContainer jr'])[2]//ul")
    private WebElement projectNameDropdownOptionList;

    @FindBy(xpath = "//div[@class='highcharts_parent_container']/div")
    private WebElement chartDiv;

    @FindBy(xpath = "//span[contains(text(), 'Project Name:')]/../following-sibling::td[2]/span")
    private WebElement projectNameAboveChart;

    @FindBy(xpath = "//div[@id='exportDate']//a")
    private WebElement exportDateDropdown;

    @FindBy(xpath = "(//div[@class='jr-mSingleselect-dropdownContainer jr'])[3]//ul")
    private WebElement exportDateDropdownOptionlist;

    @FindBy(xpath = "(//div[@class='jr-mSingleselect-dropdownContainer jr'])[3]//ul/li")
    private WebElement exportDateDropdownFirstOption;

    @FindBy(xpath = "(//span[contains(text(), 'Export Date')])[1]/../following-sibling::td[2]/span")
    private WebElement exportDateAboveChart;

    private PageUtils pageUtils;
    private WebDriver driver;

    public TargetQuotedCostTrendReportPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        LOGGER.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    /**
     * Gets project rollup dropdown first option text
     * @return String
     */
    public String getProjectRollupDropdownOptionText() {
        return projectRollupDropdownFirstOption.getAttribute("title");
    }

    /**
     * Gets project name dropdown item count
     * @return String
     */
    public String getProjectNameDropdownItemCount() {
        pageUtils.waitForElementToAppear(projectNameDropdown);
        return projectNameDropdownOptionList.getAttribute("childElementCount");
    }

    /**
     * Gets project name dropdown option text
     * @param optionIndex index of item text to get
     * @return String
     */
    public String getProjectNameDropdownOptionText(String optionIndex) {
        By locator = By.xpath(
                String.format("(//div[@class='jr-mSingleselect-dropdownContainer jr'])[2]//ul/li[%s]", optionIndex));
        return driver.findElement(locator).getAttribute("title");
    }

    /**
     * Checks if chart is displayed and enabled
     * @return boolean
     */
    public boolean isChartDisplayedAndEnabled() {
        pageUtils.waitForElementToAppear(chartDiv);
        return chartDiv.isDisplayed() && chartDiv.isEnabled();
    }

    /**
     * Selects project from dropdown
     * @param projectToSelect String
     */
    public void selectProject(String projectToSelect) {
        By locator = By.xpath(String.format("//li[@title='%s']/div/a", projectToSelect));
        if (!projectNameDropdown.getAttribute("title").equals(projectToSelect)) {
            projectNameDropdown.click();
            pageUtils.waitForElementToAppear(locator);
            driver.findElement(locator).click();
        }
    }

    /**
     * Gets project name from above chart
     * @return String
     */
    public String getProjectNameAboveChart() {
        pageUtils.waitForElementToAppear(projectNameAboveChart);
        return projectNameAboveChart.getText();
    }

    /**
     * Gets count of export date dropdown options
     * @return String
     */
    public String getCountOfExportDateOptions() {
        return exportDateDropdownOptionlist.getAttribute("childElementCount");
    }

    /**
     * Gets currently selected export date
     * @return String
     */
    public String getCurrentExportDate() {
        pageUtils.waitForElementToAppear(exportDateDropdown);
        return exportDateDropdown.getAttribute("title");
    }

    /**
     * Checks if export date is this year or last year
     * @return boolean
     */
    public boolean isExportDateRecent() {
        String currentExportDate = getCurrentExportDate();
        int year = LocalDateTime.now().getYear();
        int lastYear = LocalDateTime.now().minusYears(1).getYear();
        boolean recent = false;
        if (currentExportDate.contains(String.valueOf(year)) || currentExportDate.contains(String.valueOf(lastYear))) {
            recent = true;
        }
        return recent;
    }

    /**
     * Gets export date from above chart
     * @return String
     */
    public String getExportDateFromAboveChart() {
        pageUtils.waitForElementToAppear(exportDateAboveChart);
        return exportDateAboveChart.getText().substring(0, 19);
    }

    /**
     * Expand rollup drop-down
     *
     * @return current page object
     */
    public GenericReportPage selectProjectRollup(String rollupName) {
        if (!projectRollupDropdown.getAttribute("title").equals(rollupName)) {
            pageUtils.waitForElementAndClick(projectRollupDropdown);
            By rollupLocator = By.xpath(String.format("//li[@title='%s']/div/a", rollupName));
            pageUtils.waitForElementAndClick(rollupLocator);
        }
        return this;
    }
}
