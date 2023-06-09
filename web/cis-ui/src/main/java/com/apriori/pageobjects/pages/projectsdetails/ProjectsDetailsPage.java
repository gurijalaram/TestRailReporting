package com.apriori.pageobjects.pages.projectsdetails;

import com.apriori.pageobjects.common.PartsAndAssemblyTableController;
import com.apriori.pageobjects.common.ProjectsPartsAndAssemblyTableController;
import com.apriori.pageobjects.pages.projects.ProjectsPage;
import com.apriori.utils.PageUtils;
import com.apriori.utils.web.components.EagerPageComponent;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;

import java.util.List;

@Slf4j
public class ProjectsDetailsPage extends EagerPageComponent<ProjectsDetailsPage> {


    @FindBy(xpath = "//div[@data-testid='loader']")
    private WebElement spinner;

    @FindBy(xpath = "//h3[@data-testid='title']")
    private WebElement detailsPageTitle;

    @FindBy(xpath = "//button[@type='button']//*[@data-icon='angle-left']")
    private WebElement btnAllProjects;

    @FindBy(xpath = "//h1[@data-testid='details-title']")
    private WebElement detailsLabel;

    @FindBy(xpath = "//p[@data-testid='toolbar-Show/Hide Fields']")
    private WebElement showHideOption;

    @FindBy(xpath = "//p[@data-testid='toolbar-Filter ']")
    private WebElement filterOption;

    @FindBy(xpath = "//p[@data-testid='toolbar-Search']")
    private WebElement searchOption;

    @FindBy(xpath = "//div[starts-with(@Class,'MuiDataGrid-cellCheckbox')]")
    private List<WebElement> tableRow;

    private PageUtils pageUtils;

    public ProjectsDetailsPage(WebDriver driver) {
        this(driver, log);
    }

    private WebDriver driver;
    private ProjectsPartsAndAssemblyTableController projectsPartsAndAssemblyTableController;

    public ProjectsDetailsPage(WebDriver driver, Logger logger) {
        super(driver, logger);
        this.driver = driver;
        this.waitForProjectsPageLoad();
        this.projectsPartsAndAssemblyTableController = new ProjectsPartsAndAssemblyTableController(driver);
        PageFactory.initElements(driver, this);
    }

    @Override
    protected void isLoaded() throws Error {
    }

    /**
     * Method to wait projects page loads
     */
    public void waitForProjectsPageLoad() {
        getPageUtils().waitForElementToAppear(spinner);
        getPageUtils().waitForElementsToNotAppear(By.xpath("//div[@data-testid='loader']"),5);
    }

    /**
     * Get details page title
     *
     * @return a String
     */
    public String getProjectDetailsPageTitle() {
        return getPageUtils().waitForElementAppear(detailsPageTitle).getText();
    }

    /**
     * Checks if back to all project button displayed
     *
     * @return true/false
     */
    public boolean isAllProjectsNavigationDisplayed() {
        return getPageUtils().isElementDisplayed(btnAllProjects);
    }

    /**
     * Checks if details tab lists displayed
     *
     * @return true/false
     */
    public boolean isProjectDetailsPageTabsDisplayed(String tabName) {
        return getPageUtils().isElementDisplayed(By.xpath("//div[@role='tablist']//button[contains(text(),'" + tabName + "')]"));
    }

    /**
     * clicks on all project
     *
     * @return new page object
     */
    public ProjectsPage clickOnAllProjects() {
        getPageUtils().waitForElementAndClick(btnAllProjects);
        return new ProjectsPage(driver);
    }

    /**
     * clicks on tabs
     *
     * @return current page object
     */
    public ProjectsDetailsPage clickDetailsPageTab(String tabName) {
        getPageUtils().waitForElementAndClick(By.xpath("//div[@role='tablist']//button[contains(text(),'" + tabName + "')]"));
        return this;
    }

    /**
     * Get details tab title
     *
     * @return a String
     */
    public String getProjectDetailsTabTitle() {
        return getPageUtils().waitForElementAppear(detailsLabel).getText();
    }

    /**
     * Get Details tab information
     *
     * @return a String
     */
    public String isProjectDetailsDisplays(String section) {
        return getPageUtils().waitForElementToAppear(By.xpath("//h3[text()='" + section + "']//..")).getText();
    }

    /**
     * Checks if show/hide option displayed
     *
     * @return true/false
     */
    public boolean isShowHideOptionDisplayed() {
        getPageUtils().waitForElementsToAppear(tableRow);
        return getPageUtils().isElementDisplayed(showHideOption);
    }

    /**
     * Checks if search option displayed
     *
     * @return true/false
     */
    public boolean isSearchOptionDisplayed() {
        return getPageUtils().isElementDisplayed(searchOption);
    }

    /**
     * Checks if filter option displayed
     *
     * @return true/false
     */
    public boolean isFilterOptionDisplayed() {
        return getPageUtils().isElementDisplayed(filterOption);
    }

    /**
     * Gets table headers
     *
     * @return list of string
     */
    public List<String> getTableHeaders() {
        return projectsPartsAndAssemblyTableController.getTableHeaders();
    }

    /**
     * Gets the number of elements present on the page
     *
     * @param componentName - name of the part
     * @param scenarioName  - scenario name
     * @return size of the element as int
     */
    public int getListOfScenarios(String componentName, String scenarioName) {
        return projectsPartsAndAssemblyTableController.getListOfScenarios(componentName, scenarioName);
    }

    /**
     * Gets pinned table headers
     *
     * @return list of string
     */
    public  List<String> getPinnedTableHeaders() {
        return projectsPartsAndAssemblyTableController.getPinnedTableHeaders();
    }
}