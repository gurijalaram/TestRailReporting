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

public class ViewRepositoryPage extends ReportsPageHeader {

    private final Logger logger = LoggerFactory.getLogger(ViewRepositoryPage.class);

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
        if (afterReportsFolder.equals(Constants.DTC_METRICS_FOLDER)) {
            By locator = By.xpath(String.format("//p[contains(text(), '%s')]/..", lastFolder.split(" ")[0]));
            pageUtils.waitForElementAndClick(locator);
        } else if (lastFolder.contains("Cycle Time")) {
            navigateToFolder("Design To Cost");
            navigateToFolder("Cycle Time");
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
}
