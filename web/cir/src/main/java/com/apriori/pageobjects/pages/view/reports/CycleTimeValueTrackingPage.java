package com.apriori.pageobjects.pages.view.reports;

import com.apriori.utils.PageUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CycleTimeValueTrackingPage extends GenericReportPage {

    private final Logger logger = LoggerFactory.getLogger(CycleTimeValueTrackingPage.class);

    @FindBy(xpath = "//div[contains(@class, 'dropdownContainer')][1]//ul")
    private WebElement projectRollupDropdownList;

    @FindBy(xpath = "//div[contains(@class, 'dropdownContainer')][1]//ul/li[1]")
    private WebElement projectRollupDropdownFirstElement;

    @FindBy(xpath = "//div[contains(@class, 'dropdownContainer')][1]//ul/li[2]")
    private WebElement projectRollupDropdownSecondElement;

    @FindBy(xpath = "//span[contains(text(), 'Project Tracking Rollup:')]/../following-sibling::td[2]")
    private WebElement projectTrackingRollupAboveChart;

    @FindBy(xpath = "(//td[@colspan='4'])[20]//span")
    private WebElement projectTrackingRollupInChart;

    @FindBy(xpath = "//label[@title='Projects Rollup']//a")
    private WebElement projectRollupDropdown;

    @FindBy(xpath = "//span[contains(text(), 'Project Name:')]/../following-sibling::td[2]")
    private WebElement projectName;

    private PageUtils pageUtils;
    private WebDriver driver;

    public CycleTimeValueTrackingPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    /**
     * Gets count of project rollup dropdown items
     * @return String
     */
    public String getCountOfRollupItems() {
        return projectRollupDropdownList.getAttribute("childElementCount");
    }

    /**
     * Gets name of first rollup
     * @return String
     */
    public String getFirstRollupName() {
        return projectRollupDropdownFirstElement.getAttribute("title");
    }

    /**
     * Gets rollup in use as displayed above chart
     * @return String
     */
    public String getRollupInUseAboveChart() {
        pageUtils.waitForElementToAppear(projectTrackingRollupAboveChart);
        return projectTrackingRollupAboveChart.getText();
    }

    /**
     * Gets rollup in use as displayed in bottom chart
     * @return String
     */
    public String getRollupInUseInChart() {
        return projectTrackingRollupInChart.getText();
    }

    /**
     * Selects project rollup
     * @return instance of current page object
     */
    public CycleTimeValueTrackingPage selectProjectRollup(String index) {
        pageUtils.waitForElementAndClick(projectRollupDropdown);
        By locator = By.xpath(String.format("//div[contains(@class, 'dropdownContainer')][1]//ul/li[%s]", index));
        pageUtils.waitForElementAndClick(locator);
        //pageUtils.waitForElementAndClick(projectRollupDropdownSecondElement);
        return this;
    }

    /**
     * Gets project name
     * @return String
     */
    public String getProjectName() {
        pageUtils.waitForElementToAppear(projectName);
        return projectName.getText();
    }
}
