package com.apriori.cir.ui.pageobjects.view.reports;

import com.apriori.web.app.util.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

public class TargetQuotedCostTrendReportPage extends GenericReportPage {

    private static final Logger logger = LoggerFactory.getLogger(TargetQuotedCostTrendReportPage.class);

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

    @FindBy(xpath = "//table[@class='jrPage']//tr[16]/td[17]/span")
    private WebElement costAvoidedFinal;

    @FindBy(xpath = "//table[contains(@class, 'jrPage')]//tr[16]/td[17]/span")
    private WebElement finalAprioriCost;

    @FindBy(xpath = "(//div[@class='highcharts_parent_container']/div//*[local-name() = 'tspan'])[3]")
    private WebElement milestoneName;

    @FindBy(xpath = "(//span[@class='_jrHyperLink ReportExecution'])[1]/span")
    private WebElement firstProject;

    @FindBy(xpath = "//table[contains(@class, 'jrPage')]/tbody/tr[17]/td[29]/span")
    private WebElement costDifferenceFromApriori;

    @FindBy(xpath = "//table[contains(@class, 'jrPage')]/tbody/tr[5]/td[6]/span")
    private WebElement currentAprioriCost;

    @FindBy(xpath = "//table[contains(@class, 'jrPage')]/tbody/tr[22]/td[26]/span")
    private WebElement annualizedCurrentAprioriCost;

    private final PageUtils pageUtils;
    private final WebDriver driver;

    public TargetQuotedCostTrendReportPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    /**
     * Gets project rollup dropdown first option text
     *
     * @return String
     */
    public String getProjectRollupDropdownOptionText() {
        return projectRollupDropdownFirstOption.getAttribute("title");
    }

    /**
     * Gets project name dropdown item count
     *
     * @return String
     */
    public String getProjectNameDropdownItemCount() {
        pageUtils.waitForElementToAppear(projectNameDropdown);
        return projectNameDropdownOptionList.getAttribute("childElementCount");
    }

    /**
     * Gets project name dropdown option text
     *
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
     *
     * @return boolean
     */
    public boolean isChartDisplayedAndEnabled() {
        pageUtils.waitForElementToAppear(chartDiv);
        return chartDiv.isDisplayed() && chartDiv.isEnabled();
    }

    /**
     * Selects project from dropdown
     *
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
     *
     * @return String
     */
    public String getProjectNameAboveChart() {
        pageUtils.waitForElementToAppear(projectNameAboveChart);
        return projectNameAboveChart.getText();
    }

    /**
     * Gets count of export date dropdown options
     *
     * @return String
     */
    public String getCountOfExportDateOptions() {
        return exportDateDropdownOptionlist.getAttribute("childElementCount");
    }

    /**
     * Gets currently selected export date
     *
     * @return String
     */
    public String getCurrentExportDate() {
        pageUtils.waitForElementToAppear(exportDateDropdown);
        return exportDateDropdown.getAttribute("title");
    }

    /**
     * Checks if export date is this year or last year
     *
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
     *
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

    /**
     * Gets cost avoided final value as a string
     *
     * @return String
     */
    public String getCostAvoidedFinal() {
        pageUtils.waitForElementToAppear(costAvoidedFinal);
        return costAvoidedFinal.getText();
    }

    /**
     * Gets final apriori cost value as a string
     *
     * @return String
     */
    public String getFinalAprioriCost() {
        pageUtils.waitForElementToAppear(finalAprioriCost);
        return finalAprioriCost.getText();
    }

    /**
     * Clicks the specified milestone link
     *
     * @param milestoneName String
     * @return instance of page object
     */
    public TargetQuotedCostTrendReportPage clickMilestoneLink(String milestoneName) {
        By locatorToClick = By.xpath(String.format("//span[contains(text(), '%s')]", milestoneName));
        By locatorToWaitFor = By.xpath("(//div[@class='highcharts_parent_container']/div//*[local-name()='rect'])[5]");
        pageUtils.waitForElementToAppear(locatorToWaitFor);
        pageUtils.waitForElementAndClick(locatorToClick);
        return this;
    }

    /**
     * Gets milestone name
     *
     * @return String
     */
    public String getMilestoneName() {
        pageUtils.waitForElementToAppear(milestoneName);
        return milestoneName.getAttribute("textContent");
    }

    /**
     * Gets part name
     *
     * @param index - String index of value to get
     * @return String
     */
    public String getPartName(String index) {
        By locator = By.xpath(String.format("(//span[@class='_jrHyperLink ReportExecution'])[%s]/span", index));
        pageUtils.waitForElementToAppear(locator);
        return driver.findElement(locator).getText();
    }

    /**
     * Gets first project name from Value Tracking Reports
     *
     * @return String
     */
    public String getFirstProject() {
        pageUtils.waitForElementToAppear(firstProject);
        return firstProject.getText();
    }

    /**
     * Get quoted cost difference from apriori cost in Value Tracking reports
     *
     * @return String
     */
    public String getQuotedCostDifferenceFromApCost() {
        pageUtils.waitForElementToAppear(costDifferenceFromApriori);
        return costDifferenceFromApriori.getText();
    }

    /**
     * Get current Apriori cost from Value Tracking Details Report
     *
     * @return String
     */
    public String getCurrentAprioriCost() {
        pageUtils.waitForElementToAppear(currentAprioriCost);
        return currentAprioriCost.getText();
    }

    /**
     * Get annualized current Apriori cost from Value Tracking Details Report
     *
     * @return String
     */
    public String getAnnualizedCost() {
        pageUtils.waitForElementToAppear(annualizedCurrentAprioriCost);
        return annualizedCurrentAprioriCost.getText();
    }
}
