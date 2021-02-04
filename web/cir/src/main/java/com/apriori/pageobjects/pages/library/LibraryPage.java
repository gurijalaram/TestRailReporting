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

    private final Logger LOGGER = LoggerFactory.getLogger(LibraryPage.class);

    @FindBy(css = "div[class='pageHeader-title-text'")
    private WebElement libraryPageTitle;

    private PageUtils pageUtils;
    private WebDriver driver;

    public LibraryPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        LOGGER.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
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
     * @param reportName
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
