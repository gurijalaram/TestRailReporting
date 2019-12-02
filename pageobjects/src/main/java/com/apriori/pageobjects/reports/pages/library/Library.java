package com.apriori.pageobjects.reports.pages.library;

import com.apriori.pageobjects.reports.header.ReportsHeader;
import com.apriori.pageobjects.utils.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Library extends ReportsHeader {

    private final Logger logger = LoggerFactory.getLogger(Library.class);

    private PageUtils pageUtils;
    private WebDriver driver;

    @FindBy(css = "div[id='results'] > div > div:nth-child(1) > div")
    private WebElement libraryPageTitle;

    public Library(WebDriver driver) {
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
     * @return String - page title text
     */
    public String getLibraryTitleText() {
        pageUtils.waitForElementToAppear(libraryPageTitle);
        return libraryPageTitle.getText();
    }

    /**
     * Get name of a report
     * @return String - text of report name
     */
    private String getReportNameText(String reportName) {
        By by = By.xpath(String.format("//a[contains(text(), '%s')]", reportName));
        pageUtils.waitForElementToAppear(by);
        WebElement element = driver.findElement(by);
        return element.getAttribute("textContent");
    }

    /**
     * Gets Assembly Cost (A4) report name
     * @return String - report name
     */
    public String getAssemblyA4ReportName() {
        return getReportNameText("Assembly Cost (A4)");
    }

    /**
     * Gets Assembly Cost (Letter) report name
     * @return String - report name
     */
    public String getAssemblyLetterReportName() {
        return getReportNameText("Assembly Cost (Letter)");
    }

    /**
     * Gets Assembly Details report name
     * @return String - report name
     */
    public String getAssemblyDetailsReportName() {
        return getReportNameText("Assembly Details");
    }

    /**
     * Gets Component Cost report name
     * @return String - report name
     */
    public String getComponentCostReportName() {
        return getReportNameText("Component Cost");
    }

    /**
     * Gets Scenario Comparison report name
     * @return String - report name
     */
    public String getScenarioComparisonReportName() {
        return getReportNameText("Scenario Comparison");
    }
}
