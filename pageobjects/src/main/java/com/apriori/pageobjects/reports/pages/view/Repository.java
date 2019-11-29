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
    private Map<String, WebElement> folderElementMap = new HashMap<String, WebElement>();

    private PageUtils pageUtils;
    private WebDriver driver;

    @FindBy(css = "div[id='results'] > div > div:nth-child(1) > div")
    private WebElement repositoryPageTitle;

    @FindBy(css = "li[id='node2'] > p")
    private WebElement organizationFolder;

    @FindBy(css = "li[id='node4'] > p")
    private WebElement aprioriSubFolder;

    @FindBy(css = "li[id='node9'] > p")
    private WebElement reportsFolder;

    @FindBy(css = "li[id='node10'] > p")
    private  WebElement deploymentLeaderFolder;

    @FindBy(css = "li[id='node11'] > p")
    private WebElement dtcMetricsFolder;

    @FindBy(css = "li[id='node12'] > p")
    private WebElement generalFolder;

    @FindBy(css = "li[id='node13'] > p")
    private WebElement solutionsFolder;

    @FindBy(css = "li[id='node14'] > p")
    private WebElement upgradProcessFolder;

    @FindBy(css = "ul[id='resultsList']")
    private WebElement generalReportsList;

    @FindBy(css = "a:contains('Assembly Cost (A4)')")
    private WebElement assemblyCostA4Report;

    @FindBy(css = "a:contains('Assembly Cost (Letter)')")
    private WebElement assemblyCostLetterReport;

    @FindBy(css = "a:contains('Assembly Details')")
    private WebElement assemblyDetailsReport;

    @FindBy(css = "a:contains('Component Cost')")
    private WebElement componentCostReport;

    @FindBy(css = "a:contains('Scenario Comparison')")
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
    }

    public Repository navigateToDeploymentLeaderFolder() {
        navigateToFolder("Deployment Leader");
        return this;
    }

    public Repository navigateToDtcMetricsFolder() {
        navigateToFolder("DTC Metrics");
        return this;
    }

    public Repository navigateToGeneralFolder() {
        navigateToFolder("General");
        return this;
    }

    public Repository navigateToSolutionsFolder() {
        navigateToFolder("Solutions");
        return this;
    }

    public Repository navigateToUpgradeProcessFolder() {
        navigateToFolder("Upgrade Process");
        return this;
    }

    /**
     * Generic method to allow for navigation to a folder
     * @param reportsSubFolderName
     */
    public void navigateToFolder(String reportsSubFolderName) {
        pageUtils.waitForElementToAppear(folderElementMap.get(reportsSubFolderName)).click();
    }

    /**
     * Get page title text
     * @return String - page title text
     */
    public String getRepositoryTitleText() {
        pageUtils.waitForElementToAppear(repositoryPageTitle);
        return repositoryPageTitle.getText();
    }

    public String getCountOfGeneralReports() {
        String retVal = generalReportsList.getAttribute("childElementCount");
        return retVal;
    }

    /**
     * Initialises Folder Hash map
     */
    private void initialiseFolderMap() {
        folderElementMap.put("Deployment Leader", deploymentLeaderFolder);
        folderElementMap.put("DTC Metrics", dtcMetricsFolder);
        folderElementMap.put("General", generalFolder);
        folderElementMap.put("Solutions", solutionsFolder);
        folderElementMap.put("Upgrade Process", upgradProcessFolder);
    }

}
