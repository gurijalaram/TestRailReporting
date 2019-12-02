package com.apriori.pageobjects.reports.pages.search;

import com.apriori.pageobjects.reports.header.ReportsHeader;
import com.apriori.pageobjects.reports.pages.homepage.HomePage;
import com.apriori.pageobjects.utils.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SearchResults extends ReportsHeader {

    private final Logger logger = LoggerFactory.getLogger(HomePage.class);

    private PageUtils pageUtils;
    private WebDriver driver;

    public SearchResults(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
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
     * Gets Assembly Cost (A4) report name
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
