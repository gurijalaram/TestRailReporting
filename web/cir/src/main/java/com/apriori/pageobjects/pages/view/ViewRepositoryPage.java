package com.apriori.pageobjects.pages.view;

import com.apriori.pageobjects.header.ReportsPageHeader;
import com.apriori.pageobjects.pages.view.reports.GenericReportPage;
import com.apriori.utils.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.Constants;

import java.util.HashMap;
import java.util.Map;

public class ViewRepositoryPage extends ReportsPageHeader {

    private static final Logger logger = LoggerFactory.getLogger(ViewRepositoryPage.class);
    private Map<String, String[]> navigationMap = new HashMap<>();

    @FindBy(xpath = "//div[contains(text(), 'Repository')]")
    private WebElement repositoryPageTitle;

    @FindBy(css = "ul[id='resultsList']")
    private WebElement generalReportsList;

    private PageUtils pageUtils;
    private WebDriver driver;

    public ViewRepositoryPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        initialiseNavigationMap();
    }

    /**
     * Navigates to specified report folder
     * @param afterReportsFolder - String
     * @param lastFolder - String
     * @return Instance of current page object
     */
    public GenericReportPage navigateToReportFolder(String afterReportsFolder, String lastFolder) {
        navigateToFolder("Organization");
        navigateToFolder("aPriori");
        navigateToFolder("Reports");

        if (!afterReportsFolder.equals(Constants.DTC_METRICS_FOLDER)) {
            navigateToFolder(afterReportsFolder);
        } else {
            navigateToFolder(navigationMap.get(lastFolder)[0]);
            navigateToFolder(navigationMap.get(lastFolder)[1]);
        }

        return new GenericReportPage(driver);
    }

    /**
     * Get page title text
     *
     * @return String - page title text
     */
    public String getRepositoryTitleText() {
        pageUtils.waitForElementToAppear(repositoryPageTitle);
        return repositoryPageTitle.getText();
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
     * Generic folder navigation method
     *
     * @param folder to navigate to
     */
    private void navigateToFolder(String folder) {
        By locator = By.xpath(String.format("//p[contains(text(), '%s')]/b", folder));
        pageUtils.waitForElementAndClick(locator);
    }

    /**
     * Initialises Navigation Hash Map
     */
    private void initialiseNavigationMap() {
        navigationMap.put("Assembly Details", new String[]{Constants.GENERAL_FOLDER, "Assembly Details"});
        navigationMap.put("Assembly Cost (A4)", new String[]{Constants.GENERAL_FOLDER, "Assembly Cost (A4)"});
        navigationMap.put("Assembly Cost (Letter)", new String[]{Constants.GENERAL_FOLDER, "Assembly Cost (Letter)"});
        navigationMap.put("Scenario Comparison", new String[]{Constants.GENERAL_FOLDER, "Scenario Comparison"});

        navigationMap.put("Casting DTC", new String[]{Constants.DTC_METRICS_FOLDER, "Casting"});
        navigationMap.put("Casting DTC Details", new String[]{Constants.DTC_METRICS_FOLDER, "Casting"});
        navigationMap.put("Casting DTC Comparison", new String[]{Constants.DTC_METRICS_FOLDER, "Casting"});

        navigationMap.put("Machining DTC", new String[]{Constants.DTC_METRICS_FOLDER, "Machining"});
        navigationMap.put("Machining DTC Details", new String[]{Constants.DTC_METRICS_FOLDER, "Machining"});
        navigationMap.put("Machining DTC Comparison", new String[]{Constants.DTC_METRICS_FOLDER, "Machining"});

        navigationMap.put("Plastic DTC", new String[]{Constants.DTC_METRICS_FOLDER, "Plastic"});
        navigationMap.put("Plastic DTC Details", new String[]{Constants.DTC_METRICS_FOLDER, "Plastic"});
        navigationMap.put("Plastic DTC Comparison", new String[]{Constants.DTC_METRICS_FOLDER, "Plastic"});

        navigationMap.put("Sheet Metal DTC", new String[]{Constants.DTC_METRICS_FOLDER, "Sheet Metal"});
        navigationMap.put("Sheet Metal DTC Details", new String[]{Constants.DTC_METRICS_FOLDER, "Sheet Metal"});
        navigationMap.put("Sheet Metal DTC Comparison", new String[]{Constants.DTC_METRICS_FOLDER, "Sheet Metal"});

        navigationMap.put("Cycle Time", new String[]{"Design To Cost", "Cycle Time"});
        navigationMap.put("Cycle Time Value Tracking", new String[]{"Design To Cost", "Cycle Time"});
        navigationMap.put("Cycle Time Value Tracking Details", new String[]{"Design To Cost", "Cycle Time"});
        navigationMap.put("Target and Quoted Cost", new String[]{"Design To Cost", "Target And Quoted Cost"});
    }
}
