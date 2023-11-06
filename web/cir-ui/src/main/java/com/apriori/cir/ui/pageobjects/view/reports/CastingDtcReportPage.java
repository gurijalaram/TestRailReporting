package com.apriori.cir.ui.pageobjects.view.reports;

import com.apriori.web.app.util.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CastingDtcReportPage extends GenericReportPage {

    private static final Logger logger = LoggerFactory.getLogger(CastingDtcReportPage.class);

    @FindBy(xpath = "//span[contains(text(), 'Hole Issues')]")
    private WebElement holeIssuesCastingDtcDetailsTitle;

    @FindBy(xpath = "//table[@class='jrPage superfocus']//span[text()='23']")
    private WebElement holeIssuesCastingDtcDetailsValue;

    @FindBy(xpath = "//*[local-name() = 'rect' and @y='180.5']")
    private WebElement partOfCastingChartComparisonReport;

    @FindBy(xpath = "//*[contains(text(), 'Hole Issues')]/following-sibling::*[1]")
    private WebElement holeIssuesChartOneComparisonReport;

    private PageUtils pageUtils;
    private WebDriver driver;

    public CastingDtcReportPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    /**
     * Gets Scenario Name from Casting Dtc Details report
     *
     * @param getRowOneScenarioName boolean to determine which row index to use
     * @return String of scenario name
     */
    public String getScenarioNameCastingDtcDetails(boolean getRowOneScenarioName) {
        String rowIndex = getRowOneScenarioName ? "1" : "2";
        By locator = By.xpath(String.format(
                "(//span[@class='_jrHyperLink ReportExecution'])[%s]/../following-sibling::td[2]/span", rowIndex));
        pageUtils.waitForElementToAppear(locator);
        return driver.findElement(locator).getAttribute("textContent");
    }

    /**
     * Gets Hole Issue number from DTC Casting Details Report
     *
     * @return String - value of hole issues
     */
    public String getHoleIssuesFromDetailsReport() {
        pageUtils.waitForElementAndClick(holeIssuesCastingDtcDetailsTitle);
        return holeIssuesCastingDtcDetailsValue.getText();
    }

    /**
     * Gets Hole Issue number from DTC Casting Comparison Report
     *
     * @return String - value of hole issues
     */
    public String getHoleIssuesFromComparisonReport() {
        pageUtils.waitForElementToAppear(partOfCastingChartComparisonReport);
        Actions builder = new Actions(driver).moveToElement(partOfCastingChartComparisonReport);
        builder.perform();

        pageUtils.waitForElementToAppear(holeIssuesChartOneComparisonReport);

        return holeIssuesChartOneComparisonReport.getText();
    }
}
