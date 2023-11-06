package com.apriori.cir.ui.pageobjects.view.reports;

import com.apriori.web.app.util.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class CostOutlierIdentificationReportPage extends GenericReportPage {

    private static final Logger logger = LoggerFactory.getLogger(CostOutlierIdentificationReportPage.class);
    private final Map<String, WebElement> costOutlierValueElementMap = new HashMap<>();

    @FindBy(xpath = "(//span[contains(text(), 'Annualized Potential Savings')])[2]")
    private WebElement costOutlierAnnualisedPotentialSavings;

    @FindBy(xpath = "(//span[contains(text(), 'Percent Difference')])[2]")
    private WebElement costOutlierPercentDifference;

    @FindBy(xpath = "//div[@id='reportContainer']//tr[16]//td[34]/span")
    private WebElement costOutlierPercentDifferenceValueInChartPercentSet;

    @FindBy(xpath = "//div[@id='reportContainer']//tr[18]//td[14]/span")
    private WebElement costOutlierTotalAnnualisedValuePercentSet;

    @FindBy(xpath = "//div[@id='reportContainer']//tr[16]//td[33]/span")
    private WebElement costOutlierPercentValueInChartAnnualisedSet;

    @FindBy(xpath = "//div[@id='reportContainer']//tr[19]//td[14]/span")
    private WebElement costOutlierTotalAnnualisedValueAnnualisedSet;

    private final PageUtils pageUtils;
    private final WebDriver driver;

    public CostOutlierIdentificationReportPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        initialiseCostOutlierValueElementMap();
    }

    /**
     * Checks if Cost Outlier Identification report SVG is displayed and enabled
     *
     * @param index - String of index to use
     * @return boolean of is svg displayed
     */
    public boolean isCostOutlierSvgDisplayedAndEnabled(String index) {
        By locator = By.xpath(String.format("(//div[@id='reportContainer']//*[local-name()='svg'])[%s]", index));
        pageUtils.waitForElementToAppear(locator);
        WebElement element = driver.findElement(locator);
        return element.isDisplayed() && element.isEnabled();
    }

    /**
     * Checks if Cost Outlier Identification Report Title is displayed
     *
     * @param useAnnualisedPotentialSavings - boolean to determine which WebElement to use
     * @return boolean of is title displayed
     */
    public boolean isCostOutlierTableTitleDisplayed(boolean useAnnualisedPotentialSavings) {
        WebElement elementToUse =
                useAnnualisedPotentialSavings ? costOutlierAnnualisedPotentialSavings : costOutlierPercentDifference;
        return elementToUse.isDisplayed() && elementToUse.isEnabled();
    }

    /**
     * Checks if Cost Outlier Identification Details table is displayed
     *
     * @param name String of name to use in locator
     * @return boolean of is element displayed and enabled
     */
    public boolean isCostOutlierDetailsTableTitleDisplayed(String name) {
        By titleLocator = By.xpath("//span[contains(text(), 'Cost Outlier Identification Details')]");
        pageUtils.waitForElementToAppear(titleLocator);

        By tableTitleLocator = By.xpath((String.format("(//span[contains(text(), '%s ')])[2]", name)));
        WebElement element = driver.findElement(tableTitleLocator);
        return pageUtils.isElementDisplayed(tableTitleLocator) && element.isEnabled();
    }

    /**
     * Gets count of bar charts on specified chart
     *
     * @param chartName - String of
     * @return int of bar chart bar count
     */
    public int getCostOutlierBarChartBarCount(String chartName) {
        String chartIndex = chartName.equals("Annualized") ? "1" : "7";
        return driver.findElements(By.xpath(
                String.format(
                        "(//*[@class='highcharts-series-group']//*[local-name() = 'g'])[%s]//*[local-name()='rect']",
                        chartIndex
                )
        )).size();
    }

    /**
     * Checks if cost outlier report bar is enabled and displayed
     *
     * @param chartName String of chart name
     * @return boolean of is bar displayed and enabled
     */
    public boolean isCostOutlierBarDisplayedAndEnabled(String chartName) {
        String chartIndex = chartName.equals("Annualized") ? "1" : "7";
        WebElement elementToUse = driver.findElement(
                By.xpath(String.format(
                        "(//*[@class='highcharts-series-group']//*[local-name() = 'g'])[%s]//*[local-name()='rect']",
                        chartIndex))
        );
        return pageUtils.isElementDisplayed(elementToUse) && pageUtils.isElementEnabled(elementToUse);
    }

    /**
     * Gets Annualised or Percent value from chart on Cost Outlier Identification Details Report
     *
     * @param index String of index get element
     * @return String of total of annualised or percent value
     */
    public String getTotalAnnualisedOrPercentValue(String index) {
        return costOutlierValueElementMap.get(index).getText();
    }

    /**
     * Gets first FBC on Cost Outlier Details report
     *
     * @return BigDecimal of first fully burdened cost
     */
    public BigDecimal getFirstFbcCostOutlierDetailsReport() {
        By locator = By.xpath("//div[@id='reportContainer']//table//tr[16]/td[27]/span");
        pageUtils.waitForElementToAppear(locator);
        return new BigDecimal(driver.findElement(locator).getText());
    }

    /**
     * Initialise cost outlier value element map
     */
    private void initialiseCostOutlierValueElementMap() {
        costOutlierValueElementMap.put("Percent Value Percent Set", costOutlierPercentDifferenceValueInChartPercentSet);
        costOutlierValueElementMap.put("Annualised Value Percent Set", costOutlierTotalAnnualisedValuePercentSet);
        costOutlierValueElementMap.put("Percent Value Annualised Set", costOutlierPercentValueInChartAnnualisedSet);
        costOutlierValueElementMap.put("Annualised Value Annualised Set", costOutlierTotalAnnualisedValueAnnualisedSet);
    }
}
