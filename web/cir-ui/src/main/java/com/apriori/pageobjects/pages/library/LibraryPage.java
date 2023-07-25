package com.apriori.pageobjects.pages.library;

import com.apriori.PageUtils;
import com.apriori.pageobjects.header.ReportsPageHeader;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LibraryPage extends ReportsPageHeader {

    private static final Logger logger = LoggerFactory.getLogger(LibraryPage.class);

    @FindBy(css = "div[class='pageHeader-title-text'")
    private WebElement libraryPageTitle;

    private final PageUtils pageUtils;
    private final WebDriver driver;

    public LibraryPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {

    }

    /**
     * Get page title text
     *
     * @return String - page title text
     */
    public String getLibraryTitleText() {
        return getTitleText();
    }

    /**
     * Get name of a report
     *
     * @return String - text of report name
     */
    public String getReportName(String reportName) {
        return driver.findElement(By.xpath(String.format("//a[text() = '%s']", reportName))).getText();
    }

    /**
     * Navigate to a particular report
     *
     * @param reportName - String
     * @param className - class to instantiate as new page object
     * @return new page object
     */
    public <T> T navigateToReport(String reportName, Class<T> className) {
        WebElement reportLinkElement = driver.findElement(
                By.xpath(String.format("//a[text() = '%s']", reportName)));
        pageUtils.waitForElementAndClick(reportLinkElement);
        reportLinkElement.click();
        waitForInputControlsLoad();
        return PageFactory.initElements(driver, className);
    }
}
