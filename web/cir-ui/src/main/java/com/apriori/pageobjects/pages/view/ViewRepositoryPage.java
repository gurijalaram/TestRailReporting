package com.apriori.pageobjects.pages.view;

import com.apriori.pageobjects.header.ReportsPageHeader;
import com.apriori.pageobjects.pages.view.reports.GenericReportPage;
import com.apriori.utils.PageUtils;
import com.apriori.utils.enums.reports.ReportNamesEnum;

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
     *
     * @param lastFolder - String
     * @return Instance of current page object
     */
    public GenericReportPage navigateToReportFolder(String lastFolder) {
        navigateToFolder("Organization");
        navigateToFolder("aPriori");
        navigateToFolder("Reports");

        String[] foldersToGoTo = navigationMap.get(lastFolder);
        navigateToFolder(foldersToGoTo[0]);

        if (foldersToGoTo.length != 1) {
            navigateToFolder(foldersToGoTo[1]);
            if (foldersToGoTo.length == 3) {
                navigateToFolder(foldersToGoTo[2]);
            }
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
        navigationMap.put(
                ReportNamesEnum.ASSEMBLY_DETAILS.getReportName(), new String[]{ Constants.GENERAL_FOLDER });
        navigationMap.put(
                ReportNamesEnum.ASSEMBLY_COST_A4.getReportName(), new String[]{ Constants.GENERAL_FOLDER });
        navigationMap.put(
                ReportNamesEnum.ASSEMBLY_COST_LETTER.getReportName(), new String[]{ Constants.GENERAL_FOLDER });
        navigationMap.put(
                ReportNamesEnum.SCENARIO_COMPARISON.getReportName(), new String[]{ Constants.GENERAL_FOLDER });

        navigationMap.put(
                ReportNamesEnum.CASTING_DTC.getReportName(), new String[]{ Constants.DTC_METRICS_FOLDER });
        navigationMap.put(
                ReportNamesEnum.CASTING_DTC_DETAILS.getReportName(), new String[]{ Constants.DTC_METRICS_FOLDER });
        navigationMap.put(
                ReportNamesEnum.CASTING_DTC_COMPARISON.getReportName(), new String[]{ Constants.DTC_METRICS_FOLDER });

        navigationMap.put(
                ReportNamesEnum.MACHINING_DTC.getReportName(), new String[]{ Constants.DTC_METRICS_FOLDER });
        navigationMap.put(
                ReportNamesEnum.MACHINING_DTC_DETAILS.getReportName(), new String[]{ Constants.DTC_METRICS_FOLDER });
        navigationMap.put(
                ReportNamesEnum.MACHINING_DTC_COMPARISON.getReportName(), new String[]{ Constants.DTC_METRICS_FOLDER });

        navigationMap.put(
                ReportNamesEnum.PLASTIC_DTC.getReportName(), new String[]{ Constants.DTC_METRICS_FOLDER });
        navigationMap.put(
                ReportNamesEnum.PLASTIC_DTC_DETAILS.getReportName(), new String[]{ Constants.DTC_METRICS_FOLDER });
        navigationMap.put(
                ReportNamesEnum.PLASTIC_DTC_COMPARISON.getReportName(), new String[]{ Constants.DTC_METRICS_FOLDER });

        navigationMap.put(
                ReportNamesEnum.SHEET_METAL_DTC.getReportName(), new String[]{ Constants.DTC_METRICS_FOLDER });
        navigationMap.put(
                ReportNamesEnum.SHEET_METAL_DTC_DETAILS.getReportName(), new String[]{ Constants.DTC_METRICS_FOLDER });
        navigationMap.put(
                ReportNamesEnum.SHEET_METAL_DTC_COMPARISON.getReportName(),
                new String[]{ Constants.DTC_METRICS_FOLDER });

        navigationMap.put(ReportNamesEnum.CYCLE_TIME_VALUE_TRACKING.getReportName(),
            new String[]{ Constants.SOLUTIONS_FOLDER, Constants.DESIGN_TO_COST_FOLDER,
                Constants.CYCLE_TIME_FOLDER });
        navigationMap.put(ReportNamesEnum.CYCLE_TIME_VALUE_TRACKING_DETAILS.getReportName(),
            new String[]{ Constants.SOLUTIONS_FOLDER, Constants.DESIGN_TO_COST_FOLDER,
                Constants.CYCLE_TIME_FOLDER });

        navigationMap.put(ReportNamesEnum.TARGET_AND_QUOTED_COST_TREND.getReportName(),
            new String[]{ Constants.SOLUTIONS_FOLDER, Constants.DESIGN_TO_COST_FOLDER,
                Constants.TARGET_AND_QUOTED_COST_FOLDER });
        navigationMap.put(ReportNamesEnum.TARGET_AND_QUOTED_COST_VALUE_TRACKING.getReportName(),
            new String[]{ Constants.SOLUTIONS_FOLDER, Constants.DESIGN_TO_COST_FOLDER,
                Constants.TARGET_AND_QUOTED_COST_FOLDER });
        navigationMap.put(ReportNamesEnum.TARGET_AND_QUOTED_COST_VALUE_TRACKING_DETAILS.getReportName(),
            new String[]{ Constants.SOLUTIONS_FOLDER, Constants.DESIGN_TO_COST_FOLDER,
                Constants.TARGET_AND_QUOTED_COST_FOLDER });

        navigationMap.put(ReportNamesEnum.DESIGN_OUTLIER_IDENTIFICATION.getReportName(),
                new String[]{ Constants.SOLUTIONS_FOLDER, Constants.SOURCING_FOLDER });
        navigationMap.put(ReportNamesEnum.DESIGN_OUTLIER_IDENTIFICATION_DETAILS.getReportName(),
                new String[]{ Constants.SOLUTIONS_FOLDER, Constants.SOURCING_FOLDER });

        navigationMap.put(ReportNamesEnum.COST_OUTLIER_IDENTIFICATION.getReportName(),
                new String[]{ Constants.SOLUTIONS_FOLDER, Constants.SOURCING_FOLDER });
        navigationMap.put(ReportNamesEnum.COST_OUTLIER_IDENTIFICATION_DETAILS.getReportName(),
                new String[]{ Constants.SOLUTIONS_FOLDER, Constants.SOURCING_FOLDER });
    }
}
