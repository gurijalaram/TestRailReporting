package com.apriori.pageobjects.reports.pages.view;

import com.apriori.pageobjects.reports.header.ReportsPageHeader;
import com.apriori.pageobjects.reports.pages.view.enums.AssemblyReportsEnum;
import com.apriori.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class ViewRepositoryPage extends ReportsPageHeader {

    private Logger logger = LoggerFactory.getLogger(ViewRepositoryPage.class);
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

    @FindBy(css = "li[id='node21']")
    private WebElement castingFolder;

    @FindBy(xpath = "//ul[@id='node1sub']/li[1]/ul/li[2]/ul/li[5]/ul/li[2]/ul/li[2]/p/b")
    private WebElement machiningDTCFolder;

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
     * Navigates to general folder
     *
     * @return current page object
     */
    public ViewRepositoryPage navigateToGeneralFolder() {
        navigateToFolder("Organization");
        navigateToFolder("aPriori");
        navigateToFolder("Reports");
        navigateToFolder("General");
        return this;
    }

    /**
     * Navigate to Machining DTC folder
     *
     * @return current page object
     */
    public ViewRepositoryPage navigateToMachiningDTCFolder() {
        navigateToFolder("Organization");
        navigateToFolder("aPriori");
        navigateToFolder("Reports");
        navigateToFolder("DTC Metrics");
        navigateToFolder("Machining DTC");
        return this;
    }

    /**
     * Navigate to Casting folder
     *
     * @return current page object
     */
    public ViewRepositoryPage navigateToCastingFolder() {
        navigateToFolder("Organization");
        navigateToFolder("aPriori");
        navigateToFolder("Reports");
        navigateToFolder("DTC Metrics");
        navigateToFolder("Casting");
        return this;
    }

    /**
     * Waits for Machining DTC reports to appear
     *
     * @return current page object
     */
    public ViewRepositoryPage waitForMachiningDTCReportsToAppear() {
        pageUtils.checkElementAttribute(generalReportsList, "childElementCount", "3");
        return this;
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
        return pageUtils.getReportNameText(reportName);
    }


    /**
     * Generic folder navigation method
     *
     * @param folder to navigate to
     * @return current page object
     */
    private ViewRepositoryPage navigateToFolder(String folder) {
        pageUtils.waitForElementToAppear(folderElementMap.get(folder))
            .click();
        return this;
    }

    /**
     * Initialises Report hash map
     */
    private void initialiseReportMap() {
        reportElementMap.put(AssemblyReportsEnum.ASSEMBLY_COST_A4.getReportName(), assemblyCostA4Report);
        reportElementMap.put(AssemblyReportsEnum.ASSEMBLY_COST_LETTER.getReportName(), assemblyCostLetterReport);
        reportElementMap.put(AssemblyReportsEnum.ASSEMBLY_DETAILS.getReportName(), assemblyDetailsReport);
        reportElementMap.put(AssemblyReportsEnum.COMPONENT_COST.getReportName(), componentCostReport);
        reportElementMap.put(AssemblyReportsEnum.SCENARIO_COMPARISON.getReportName(), scenarioComparisonReport);
    }

    /**
     * Initialises Bottom Folder hash map
     */
    private void initialiseFolderMap() {
        folderElementMap.put("Deployment Leader", deploymentLeaderFolder);
        folderElementMap.put("DTC Metrics", dtcMetricsFolder);
        folderElementMap.put("General", generalFolder);
        folderElementMap.put("Solutions", solutionsFolder);
        folderElementMap.put("Upgrade Process", upgradeProcessFolder);
        folderElementMap.put("Casting", castingFolder);
        folderElementMap.put("Machining DTC", machiningDTCFolder);

        folderElementMap.put("Organization", organizationFolder);
        folderElementMap.put("aPriori", aprioriSubFolder);
        folderElementMap.put("Reports", reportsFolder);
    }

}
