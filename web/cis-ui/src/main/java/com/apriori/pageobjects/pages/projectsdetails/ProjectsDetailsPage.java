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

    @FindBy(id = "edit-btn-project-details")
    private WebElement btnEdit;

    @FindBy(id = "primary-btn-edit-project-modal")
    private WebElement btnSave;

    @FindBy(id = "secondary-btn-edit-project-modal")
    private WebElement btnCancel;

    @FindBy(xpath = "//p[text()='Edit Project Details']")
    private WebElement editProjectModal;

    @FindBy(id = "edit-project-name-input")
    private WebElement editProjectNameField;

    @FindBy(id = "edit-project-owner-input")
    private WebElement editProjectOwnerField;

    @FindBy(xpath = "//input[@type='tel']")
    private WebElement editProjectDueDateField;

    @FindBy(id = "edit-project-description-input")
    private WebElement editProjectDescriptionField;

    @FindBy(xpath = "//div[@aria-live='polite']")
    private WebElement btnYear;

    @FindBy(xpath = "//li[contains(@id,edit-project-owner-input-option)]//div//span")
    private WebElement projectOwnerEmail;

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

    /**
     * Checks if edit details button displayed
     *
     * @return true/false
     */
    public boolean isEditDetailsDisplayed() {
        return getPageUtils().isElementDisplayed(btnEdit);
    }

    /**
     * clicks on edit
     *
     * @return current page object
     */
    public ProjectsDetailsPage clickEditDetails() {
        getPageUtils().waitForElementAndClick(btnEdit);
        return this;
    }

    /**
     * Checks if edit details modal displayed
     *
     * @return true/false
     */
    public boolean isEditDetailsModalDisplayed() {
        return getPageUtils().isElementDisplayed(editProjectModal);
    }

    /**
     * clicks on Save
     *
     * @return current page object
     */
    public ProjectsDetailsPage clickSave() {
        getPageUtils().waitForElementAndClick(btnSave);
        getPageUtils().waitForElementsToNotAppear(By.xpath("//div[@data-testid='loader']"),5);
        return this;
    }

    /**
     * clicks on Save
     *
     * @return current page object
     */
    public ProjectsDetailsPage clickCancel() {
        getPageUtils().waitForElementAndClick(btnCancel);
        return this;
    }

    /**
     * Edit project name
     *
     * @return current page object
     */
    public ProjectsDetailsPage editProjectName(String projectName) {
        getPageUtils().clearValueOfElement(editProjectNameField);
        getPageUtils().waitForElementToAppear(editProjectNameField).sendKeys(projectName);
        return this;
    }

    /**
     * Edit project owner
     *
     * @return current page object
     */
    public ProjectsDetailsPage editProjectOwner(String projectOwner) {
        getPageUtils().waitForElementAndClick(editProjectOwnerField);
        getPageUtils().clearValueOfElement(editProjectOwnerField);
        getPageUtils().waitForElementAppear(editProjectOwnerField).sendKeys(projectOwner);
        getPageUtils().waitForElementAndClick(projectOwnerEmail);
        return this;
    }

    /**
     * Edit due date
     *
     * @return current page object
     */
    public ProjectsDetailsPage editDueDate(String year, String date) {
        getPageUtils().waitForElementAndClick(editProjectDueDateField);
        getPageUtils().waitForElementAndClick(btnYear);
        getPageUtils().waitForElementAndClick(By.xpath("//button[contains(text(),'" + year + "')]"));
        getPageUtils().waitForElementAndClick(By.xpath("//button[contains(text(),'" + date + "')]"));
        return this;
    }

    /**
     * Edit project description
     *
     * @return current page object
     */
    public ProjectsDetailsPage editProjectDescription(String projectDescription) {
        getPageUtils().clearValueOfElement(editProjectDescriptionField);
        getPageUtils().waitForElementToAppear(editProjectDescriptionField).sendKeys(projectDescription);
        return this;
    }

    /**
     * Get save button status
     *
     * @return a String
     */
    public String getSaveButtonStatus() {
        return getPageUtils().waitForElementAppear(btnSave).getAttribute("class");
    }
}