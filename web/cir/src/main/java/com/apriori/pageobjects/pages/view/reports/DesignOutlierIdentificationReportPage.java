package com.apriori.pageobjects.pages.view.reports;

import com.apriori.utils.PageUtils;

import org.openqa.selenium.By;
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

    private PageUtils pageUtils;
    private WebDriver driver;

    public DesignOutlierIdentificationReportPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    /**
     * Gets export date count
     * @return String
     */
    public String getExportDateCount() {
        pageUtils.waitForElementToAppear(exportDateList);
        return exportDateList.getAttribute("childElementCount");
    }

    /**
     * Inputs value into max or min cost or mass fields
     * @param valueToSet - cost or mass input field
     * @param maxMinValue - max or min input field
     * @param valueToInput - value to input into field
     */
    public void inputMaxOrMinCostOrMass(String valueToSet, String maxMinValue, String valueToInput) {
        By locator = By.xpath(String.format("//div[@id='aPriori%s%s']//input", valueToSet, maxMinValue));
        WebElement input = driver.findElement(locator);
        pageUtils.waitForElementToAppear(input);
        input.clear();
        input.click();
        input.sendKeys(valueToInput);
    }

    /**
     * Gets Cost min or max above chart value
     * @param maxOrMin - max or min value to get
     * @return value as string
     */
    public String getCostMinOrMaxAboveChartValue(String maxOrMin) {
        By locator = By.xpath(
                String.format(
                        "//span[contains(text(), 'aPriori Cost %s:')]/ancestor::td[2]/following-sibling::td[1]/span",
                        maxOrMin)
        );
        pageUtils.waitForElementToAppear(locator);
        return driver.findElement(locator).getText();
    }

    /**
     * Gets Mass min or max above chart value
     * @param maxOrMin - max or min value to get
     * @return value as string
     */
    public String getMassMinOrMaxAboveChartValue(String maxOrMin) {
        By locator = By.xpath(
                String.format(
                        "//span[contains(text(), 'aPriori Mass %s:')]/../following-sibling::td[1]/span",
                        maxOrMin
                )
        );
        pageUtils.waitForElementToAppear(locator);
        return driver.findElement(locator).getText();
    }
}
