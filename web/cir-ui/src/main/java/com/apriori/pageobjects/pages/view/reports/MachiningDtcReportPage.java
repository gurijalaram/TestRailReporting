package com.apriori.pageobjects.pages.view.reports;

import com.apriori.PageUtils;
import com.apriori.utils.enums.reports.ReportNamesEnum;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MachiningDtcReportPage extends GenericReportPage {

    private static final Logger logger = LoggerFactory.getLogger(MachiningDtcReportPage.class);

    @FindBy(css = ".highcharts_parent_container > div > svg > .highcharts-series-group > g:nth-child(2) > " +
            "path:nth-of-type(38)")
    private WebElement machiningDtcBubbleTwo;

    @FindBy(xpath = "(((//div[@class='highcharts-container '])[2]//*[local-name()='g'])[16]/*[local-name()='text'])" +
            "[13]")
    private WebElement machiningDtcComparisonPartName;

    @FindBy(xpath = "(((//div[@class='highcharts-container '])[2]//*[local-name()='g'])[7]/*[local-name()='rect'])" +
            "[13]")
    private WebElement machiningDtcComparisonBar;

    @FindBy(xpath = "//table/tbody/tr[13]/td[2]")
    private WebElement machiningDtcDetailsPartNameLink;

    @FindBy(xpath = "//span[@class='_jrHyperLink Reference']")
    private WebElement dtcPartSummaryPartName;

    private final PageUtils pageUtils;
    private final WebDriver driver;

    public MachiningDtcReportPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    /**
     * Clicks bubble to get to DTC Part Summary and Switches tab
     */
    public void clickMachiningBubbleAndSwitchTab() {
        pageUtils.actionClick(machiningDtcBubbleTwo);

        switchTab(1);
        pageUtils.waitForElementToAppear(upperTitle);
        pageUtils.waitForElementNotDisplayed(loadingPopup, 1);
        pageUtils.waitForElementToAppear(dtcPartSummaryPartName);
    }

    /**
     * Clicks bar in Machining DTC Comparison Report and switches tab
     *
     * @return String of part name
     */
    public String clickMachiningDtcComparisonBar() {
        pageUtils.waitForElementToAppear(machiningDtcComparisonPartName);
        pageUtils.waitForElementToAppear(machiningDtcComparisonBar);
        setReportName(ReportNamesEnum.MACHINING_DTC_COMPARISON.getReportName());
        String partName = getPartNameDtcReports();

        for (int i = 0; i < 2; i++) {
            Actions builder = new Actions(driver).moveToElement(machiningDtcComparisonBar).click();
            builder.build().perform();
        }

        switchTab(1);
        pageUtils.waitForElementToAppear(upperTitle);
        pageUtils.waitForElementToAppear(dtcPartSummaryPartName);
        return partName;
    }

    /**
     * Clicks part name link in Machining DTC Details report and switches tab
     *
     * @return String of part name
     */
    public String clickMachiningDtcDetailsPartName() {
        pageUtils.waitForElementToAppear(machiningDtcDetailsPartNameLink);

        setReportName(ReportNamesEnum.MACHINING_DTC_DETAILS.getReportName());
        String partName = getPartNameDtcReports();

        pageUtils.waitForElementAndClick(machiningDtcDetailsPartNameLink);

        switchTab(1);
        pageUtils.waitForElementToAppear(upperTitle);
        pageUtils.waitForElementToAppear(dtcPartSummaryPartName);

        return partName;
    }

    /**
     * Gets Part Name value from DTC Part Summary report
     *
     * @return String of dtc part summary part name
     */
    public String getDtcPartSummaryPartNameValue() {
        return dtcPartSummaryPartName.getText();
    }

    /**
     * Gets upper title text from any report
     *
     * @return String of report title
     */
    public String getUpperTitleText() {
        return upperTitle.getText();
    }
}
