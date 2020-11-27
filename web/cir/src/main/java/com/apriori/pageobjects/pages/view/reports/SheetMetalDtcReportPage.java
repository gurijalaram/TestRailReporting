package com.apriori.pageobjects.pages.view.reports;

import com.apriori.utils.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SheetMetalDtcReportPage extends GenericReportPage {

    private final Logger logger = LoggerFactory.getLogger(SheetMetalDtcReportPage.class);

    @FindBy(xpath = "//span[contains(text(), 'Export Set:')]/../following-sibling::td[2]")
    private WebElement exportSetOnReport;

    @FindBy(xpath = "(//div[@title='Single export set selection.']//input)[1]")
    private WebElement exportSetSearchInput;

    private PageUtils pageUtils;
    private WebDriver driver;

    public SheetMetalDtcReportPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    /**
     * Gets displayed Export Set
     * @return String
     */
    public String getDisplayedExportSet() {
        pageUtils.waitForElementToAppear(exportSetOnReport);
        return exportSetOnReport.getText();
    }

    /**
     * Searches Export Set List
     * @param exportSetName String
     */
    public void searchForExportSet(String exportSetName) {
        pageUtils.waitForElementAndClick(exportSetSearchInput);
        exportSetSearchInput.clear();
        exportSetSearchInput.sendKeys(exportSetName);
        By locator = By.xpath(
                String.format("(//div[@title='Single export set selection.']//ul)[1]/li[@title='%s']", exportSetName));
        pageUtils.waitForElementToAppear(locator);
    }

    /**
     * Gets first Export Set List name
     * @return String
     */
    public String getFirstExportSetName() {
        By locator = By.xpath("(//div[@title='Single export set selection.']//ul)[1]/li[1]");
        pageUtils.waitForElementToAppear(locator);
        return driver.findElement(locator).getText();
    }
}
