package com.apriori.pageobjects.view.reports;

import com.apriori.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DesignOutlierIdentificationReportPage extends GenericReportPage {

    private static final Logger logger = LoggerFactory.getLogger(DesignOutlierIdentificationReportPage.class);

    @FindBy(xpath = "//div[@id='exportDate']//div[@class='jr-mSingleselect jr']")
    private WebElement exportDateList;

    private final PageUtils pageUtils;
    private final WebDriver driver;

    public DesignOutlierIdentificationReportPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    /**
     * Gets export date count
     *
     * @return String - count of export date count
     */
    public String getExportDateCount() {
        pageUtils.waitForElementToAppear(exportDateList);
        return exportDateList.getAttribute("childElementCount");
    }
}
