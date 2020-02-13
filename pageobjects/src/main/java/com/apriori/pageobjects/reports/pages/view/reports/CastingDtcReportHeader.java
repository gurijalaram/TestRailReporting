package com.apriori.pageobjects.reports.pages.view.reports;

import com.apriori.pageobjects.utils.PageUtils;

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
     * @return String name of displayed rollup
     */
    public String getDisplayedRollup() {
        pageUtils.waitForElementNotDisplayed(loadingPopup, 1);
        pageUtils.waitForElementToAppear(headerDisplayedRollup);
        return headerDisplayedRollup.getText();
    }
}
