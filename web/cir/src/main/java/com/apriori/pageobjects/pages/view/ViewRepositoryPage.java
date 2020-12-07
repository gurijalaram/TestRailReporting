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

    private final Logger logger = LoggerFactory.getLogger(ViewRepositoryPage.class);

    private Map<String, WebElement> folderElementMap = new HashMap<>();
    private Map<String, WebElement> reportElementMap = new HashMap<>();

    @FindBy(css = "div[id='results'] > div > div:nth-child(1) > div")
    private WebElement repositoryPageTitle;

    @FindBy(css = "ul[id='node1sub'] > li:nth-child(1) > p > b")
    private WebElement organizationFolder;

    @FindBy(xpath = "//ul[@id='node1sub']/li[1]/ul/li[2]/p/b")
    private WebElement aprioriSubFolder;

    @FindBy(xpath = "//ul[@id='node1sub']/li[1]/ul/li[2]/ul/li[5]/p/b")
    private WebElement reportsFolder;

    @FindBy(xpath = "//ul[@id='node1sub']/li[1]/ul/li[2]/ul/li[5]/ul/li[1]/p/b")
    private WebElement deploymentLeaderFolder;

    @FindBy(xpath = "//ul[@id='node1sub']/li[1]/ul/li[2]/ul/li[5]/ul/li[2]/p/b")
    private WebElement dtcMetricsFolder;

    @FindBy(xpath = "//ul[@id='node1sub']/li[1]/ul/li[2]/ul/li[5]/ul/li[3]/p/b")
    private WebElement generalFolder;

    @FindBy(xpath = "//ul[@id='node1sub']/li[1]/ul/li[2]/ul/li[5]/ul/li[4]/p/b")
    private WebElement solutionsFolder;

    @FindBy(xpath = "//ul[@id='node1sub']/li[1]/ul/li[2]/ul/li[5]/ul/li[5]/p/b")
    private WebElement upgradeProcessFolder;

    @FindBy(css = "ul[id='resultsList']")
    private WebElement generalReportsList;

    @FindBy(xpath = "//a[contains(text(), 'Assembly Cost (A4)')]")
    private WebElement assemblyCostA4Report;

    @FindBy(xpath = "//a[contains(text(), 'Assembly Cost (Letter)')]")
    private WebElement assemblyCostLetterReport;

    @FindBy(xpath = "//a[contains(text(), 'Assembly Details')]")
    private WebElement assemblyDetailsReport;

    @FindBy(xpath = "//a[contains(text(), 'Component Cost')]")
    private WebElement componentCostReport;

    @FindBy(xpath = "//a[contains(text(), 'Scenario Comparison')]")
    private WebElement scenarioComparisonReport;

    @FindBy(xpath = "//p[contains(text(), 'Machining')]/..")
    private WebElement castingDtcFolder;

    @FindBy(xpath = "//p[contains(text(), 'Machining')]/..")
    private WebElement machiningDtcFolder;

    @FindBy(xpath = "//p[contains(text(), 'Plastic')]/..")
    private WebElement plasticDtcFolder;

    @FindBy(xpath = "//p[contains(text(), 'Sheet')]/..")
    private WebElement sheetMetalDtcFolder;

    private PageUtils pageUtils;
    private WebDriver driver;

    public ViewRepositoryPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        initialiseFolderMap();
        initialiseReportMap();
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
        navigateToFolder(afterReportsFolder);
        if (!afterReportsFolder.equals(Constants.GENERAL_FOLDER)) {
            By locator = By.xpath(String.format("//p[contains(text(), '%s')]/..", lastFolder.split(" ")[0]));
            pageUtils.waitForElementAndClick(locator);
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
     * Get count of General Reports
     *
     * @return Integer
     */
    public Integer getCountOfGeneralReports() {
        pageUtils.waitForElementToAppear(generalReportsList);
        return Integer.parseInt(generalReportsList.getAttribute("childElementCount"));
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
     * Gets expected report names values
     * @return String array
     */
    public String[] getReportNamesValues() {
        return new String[] {
                ReportNamesEnum.PLASTIC_DTC.getReportName(),
                ReportNamesEnum.PLASTIC_DTC_COMPARISON.getReportName(),
                ReportNamesEnum.PLASTIC_DTC_DETAILS.getReportName()
        };
    }

    /**
     * Gets all actual report names from UI
     * @return String array
     */
    public String[] getActualReportNames() {
        String[] actualReportNames = new String[getReportNamesValues().length];

        for (int i = 0; i < getReportNamesValues().length; i++) {
            actualReportNames[i] = getReportName(getReportNamesValues()[i]);
        }

        return  actualReportNames;
    }


    /**
     * Generic folder navigation method
     *
     * @param folder to navigate to
     */
    private void navigateToFolder(String folder) {
        pageUtils.waitForElementToAppear(folderElementMap.get(folder))
            .click();
    }

    /**
     * Initialises Report hash map
     */
    private void initialiseReportMap() {
        reportElementMap.put(ReportNamesEnum.ASSEMBLY_COST_A4.getReportName(), assemblyCostA4Report);
        reportElementMap.put(ReportNamesEnum.ASSEMBLY_COST_LETTER.getReportName(), assemblyCostLetterReport);
        reportElementMap.put(ReportNamesEnum.ASSEMBLY_DETAILS.getReportName(), assemblyDetailsReport);
        reportElementMap.put(ReportNamesEnum.COMPONENT_COST.getReportName(), componentCostReport);
        reportElementMap.put(ReportNamesEnum.SCENARIO_COMPARISON.getReportName(), scenarioComparisonReport);
    }

    /**
     * Initialises Bottom Folder hash map
     */
    private void initialiseFolderMap() {
        folderElementMap.put("Deployment Leader", deploymentLeaderFolder);
        folderElementMap.put(Constants.DTC_METRICS_FOLDER, dtcMetricsFolder);
        folderElementMap.put(Constants.GENERAL_FOLDER, generalFolder);
        folderElementMap.put("Solutions", solutionsFolder);
        folderElementMap.put("Upgrade Process", upgradeProcessFolder);
        folderElementMap.put(ReportNamesEnum.CYCLE_TIME_VALUE_TRACKING.getReportName(), plasticDtcFolder);
        folderElementMap.put(ReportNamesEnum.CYCLE_TIME_VALUE_TRACKING_DETAILS.getReportName(), plasticDtcFolder);

        folderElementMap.put("Organization", organizationFolder);
        folderElementMap.put("aPriori", aprioriSubFolder);
        folderElementMap.put("Reports", reportsFolder);
    }

}
