package com.apriori.pageobjects.reports.pages.library;

import com.apriori.pageobjects.reports.header.ReportsPageHeader;
import com.apriori.pageobjects.reports.pages.view.reports.AssemblyDetailsReportPage;
import com.apriori.pageobjects.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LibraryPage extends ReportsPageHeader {

    private final Logger logger = LoggerFactory.getLogger(LibraryPage.class);

    private PageUtils pageUtils;
    private WebDriver driver;

    @FindBy(css = "div[id='results'] > div > div:nth-child(1) > div")
    private WebElement libraryPageTitle;

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
        return pageUtils.getReportNameText(reportName);
    }

    /**
     * Navigate to a particular report
     *
     * @param reportName
     * @return new page object
     */
    public AssemblyDetailsReportPage navigateToReport(String reportName) {
        WebElement reportLinkElement = pageUtils.getReportLinkElement(reportName);
        pageUtils.waitForElementToAppear(reportLinkElement);
        reportLinkElement.click();
        return new AssemblyDetailsReportPage(driver);
    }
}
