package com.apriori.pageobjects.reports.pages.view.reports;

import com.apriori.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CastingDtcReportHeader extends GenericReportPage {

    private final Logger logger = LoggerFactory.getLogger(CastingDtcReportHeader.class);

    private PageUtils pageUtils;
    private WebDriver driver;

    @FindBy(xpath = "//div[@id='reportContainer']//td[@colspan='4']/span")
    private WebElement headerDisplayedRollup;

    @FindBy(xpath = "//div[@id='reportContainer']//span[contains(text(), 'Rollup:')]/../..//span[contains(text(), 'ALL')]")
    private WebElement headerDisplayedCastingDtcDetails;

    @FindBy(id = "loading")
    private WebElement loadingPopup;

    public CastingDtcReportHeader(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    /**
     * Get roll-up displayed in header
     *
     * @return String name of displayed rollup
     */
    public String getDisplayedRollup(String reportName) {
        pageUtils.waitForElementNotDisplayed(loadingPopup, 1);
        return waitForCorrectHeader(reportName);
    }

    /**
     * Method to wait on correct header, based on report name
     *
     * @param reportName
     * @return String of header text
     */
    private String waitForCorrectHeader(String reportName) {
        String textToReturn;
        if (reportName.equals("Casting DTC Details") || reportName.equals("Casting DTC")) {
            pageUtils.waitForElementToAppear(headerDisplayedCastingDtcDetails);
            textToReturn = headerDisplayedCastingDtcDetails.getText();
        } else {
            pageUtils.waitForElementToAppear(headerDisplayedRollup);
            textToReturn = headerDisplayedRollup.getText();
        }
        return textToReturn;
    }
}
