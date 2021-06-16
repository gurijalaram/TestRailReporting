package com.apriori.pageobjects.pages.library;

import com.apriori.pageobjects.header.ReportsPageHeader;
import com.apriori.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LibraryPage extends ReportsPageHeader {

    private static final Logger logger = LoggerFactory.getLogger(LibraryPage.class);

    @FindBy(css = "div[id='results'] > div > div:nth-child(1) > div")
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
        pageUtils.waitForElementToAppear(libraryPageTitle);
        return libraryPageTitle.getText();
    }

    /**
     * Get name of a report
     *
     * @return String - text of report name
     */
    public String getReportName(String reportName) {
        return pageUtils.getReportElement(reportName).getText();
    }

    /**
     * Navigate to a particular report
     *
     * @param reportName String
     * @return new page object
     */
    public <T> T navigateToReport(String reportName, Class<T> className) {
        WebElement reportLinkElement = pageUtils.getReportElement(reportName);
        pageUtils.waitForElementToAppear(reportLinkElement);
        reportLinkElement.click();
        waitForInputControlsLoad();
        return PageFactory.initElements(driver, className);
    }
}
