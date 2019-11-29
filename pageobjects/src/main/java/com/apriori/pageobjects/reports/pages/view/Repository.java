package com.apriori.pageobjects.reports.pages.view;

import com.apriori.pageobjects.reports.header.ReportsHeader;
import com.apriori.pageobjects.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class Repository extends ReportsHeader {

    private final Logger logger = LoggerFactory.getLogger(Repository.class);
    private Map<String, WebElement> folderElementMap = new HashMap<>();
    private Map<String, WebElement> reportElementMap = new HashMap<>();

    private PageUtils pageUtils;
    private WebDriver driver;

    @FindBy(css = "div[id='results'] > div > div:nth-child(1) > div")
    private WebElement repositoryPageTitle;

    @FindBy(css = "ul[id='node1sub'] > li:nth-child(1) > p > b")
    private WebElement organizationFolder;

    @FindBy(xpath = "//ul[@id='node1sub']/li[1]/ul/li[2]/p/b")
    private WebElement aprioriSubFolder;

    @FindBy(xpath = "//ul[@id='node1sub']/li[1]/ul/li[2]/ul/li[5]/p/b")
    private WebElement reportsFolder;

    @FindBy(xpath = "//ul[@id='node1sub']/li[1]/ul/li[2]/ul/li[5]/ul/li[1]/p/b")
    private  WebElement deploymentLeaderFolder;

    @FindBy(xpath = "//ul[@id='node1sub']/li[1]/ul/li[2]/ul/li[5]/ul/li[2]/p/b")
    private WebElement dtcMetricsFolder;

    @FindBy(xpath = "//ul[@id='node1sub']/li[1]/ul/li[2]/ul/li[5]/ul/li[3]/p/b")
    private WebElement generalFolder;

    @FindBy(xpath = "//ul[@id='node1sub']/li[1]/ul/li[2]/ul/li[5]/ul/li[4]/p/b")
    private WebElement solutionsFolder;

    @FindBy(xpath = "//ul[@id='node1sub']/li[1]/ul/li[2]/ul/li[5]/ul/li[5]/p/b")
    private WebElement upgradProcessFolder;

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

    @FindBy(id = "apply")
    private WebElement applyButton;

    @FindBy(id = "ok")
    private WebElement okButton;

    @FindBy(id = "reset")
    private WebElement resetButton;

    @FindBy(id = "cancelButton")
    private WebElement cancelButton;

    @FindBy(id = "save")
    private WebElement saveButton;

    public Repository(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        initialiseFolderMap();
        initialiseReportMap();
    }

    public Repository navigateToFolder(String folder) {
        pageUtils.waitForElementToAppear(folderElementMap.get(folder))
                .click();
        return this;
    }

    /**
     * Get page title text
     * @return String - page title text
     */
    public String getRepositoryTitleText() {
        pageUtils.waitForElementToAppear(repositoryPageTitle);
        return repositoryPageTitle.getText();
    }

    /**
     * Get count of General Reports
     * @return String
     */
    public String getCountOfGeneralReports() {
        pageUtils.waitForElementToAppear(generalReportsList);
        return generalReportsList.getAttribute("childElementCount");
    }

    /**
     * Get name of first report
     * @return
     */
    public String getReportNameText(String reportName) {
        WebElement element = reportElementMap.get(reportName);
        String retVal = element.getAttribute("textContent");
        return retVal;
    }

    /**
     * Initialises Report hash map
     */
    private void initialiseReportMap() {
        reportElementMap.put("Assembly Cost (A4)", assemblyCostA4Report);
        reportElementMap.put("Assembly Cost (Letter)", assemblyCostLetterReport);
        reportElementMap.put("Assembly Details", assemblyDetailsReport);
        reportElementMap.put("Component Cost", componentCostReport);
        reportElementMap.put("Scenario Comparison", scenarioComparisonReport);
    }

    /**
     * Initialises Bottom Folder hash map
     */
    private void initialiseFolderMap() {
        folderElementMap.put("Deployment Leader", deploymentLeaderFolder);
        folderElementMap.put("DTC Metrics", dtcMetricsFolder);
        folderElementMap.put("General", generalFolder);
        folderElementMap.put("Solutions", solutionsFolder);
        folderElementMap.put("Upgrade Process", upgradProcessFolder);

        folderElementMap.put("Organization", organizationFolder);
        folderElementMap.put("aPriori", aprioriSubFolder);
        folderElementMap.put("Reports", reportsFolder);
    }
}
