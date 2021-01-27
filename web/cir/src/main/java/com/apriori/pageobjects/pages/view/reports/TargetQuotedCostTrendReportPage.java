package com.apriori.pageobjects.pages.view.reports;

import com.apriori.utils.PageUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TargetQuotedCostTrendReportPage extends GenericReportPage {

    private final Logger LOGGER = LoggerFactory.getLogger(TargetQuotedCostTrendReportPage.class);

    @FindBy(xpath = "//div[@id='projectRollup']//a")
    private WebElement projectRollupDropdown;

    @FindBy(xpath = "(//div[@class='jr-mSingleselect-dropdownContainer jr'])[1]//ul")
    private WebElement projectRollupDropdownOptionList;

    @FindBy(xpath = "//div[@id='projectName']//a")
    private WebElement projectNameDropdown;

    @FindBy(xpath = "(//div[@class='jr-mSingleselect-dropdownContainer jr'])[2]//ul")
    private WebElement projectNameDropdownOptionList;

    @FindBy(xpath = "//div[@class='highcharts_parent_container']/div")
    private WebElement chartDiv;

    @FindBy(xpath = "//span[contains(text(), 'Project Name:')]/../following-sibling::td[2]/span")
    private WebElement projectNameAboveChart;

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
     * Gets project rollup dropdown item count
     * @return String
     */
    public String getProjectRollupDropdownItemCount() {
        pageUtils.waitForElementToAppear(projectRollupDropdown);
        return projectRollupDropdownOptionList.getAttribute("childElementCount");
    }

    /**
     * Gets project rollup dropdown option text
     * @param optionIndex index of item text to get
     * @return String
     */
    public String getProjectRollupDropdownOptionText(String optionIndex) {
        By locator = By.xpath(
                String.format("(//div[@class='jr-mSingleselect-dropdownContainer jr'])[1]//ul/li[%s]", optionIndex));
        return driver.findElement(locator).getAttribute("title");
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
}
