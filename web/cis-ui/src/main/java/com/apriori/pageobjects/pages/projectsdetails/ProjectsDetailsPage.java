package com.apriori.pageobjects.pages.projectsdetails;

import com.apriori.pageobjects.common.PartsAndAssemblyTableController;
import com.apriori.pageobjects.common.ProjectsPartsAndAssemblyTableController;
import com.apriori.pageobjects.pages.projects.ProjectsPage;
import com.apriori.utils.PageUtils;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.web.components.EagerPageComponent;

import com.utils.CisColumnsEnum;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.SoftAssertions;
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

    @FindBy(xpath = "//button[@data-testid='toolbar-control-button-inactive']//p[@data-testid='toolbar-Show/Hide Fields']")
    private WebElement btnShowHideOption;

    @FindBy(xpath = "//div[@id='simple-popper']//input[@data-testid='show-hide-input']")
    private WebElement showHideSearchField;

    @FindBy(xpath = "//span[@data-testid='switch']")
    private WebElement btnSwitchToggle;

    @FindBy(id = "show-button")
    private WebElement btnHideAll;

    @FindBy(css = "div.MuiDataGrid-columnHeaders")
    private WebElement tblHeaders;

    @FindBy(id = "hide-button")
    private WebElement btnShowAll;

    @FindBy(xpath = "//h4[@data-testid='user-full-name']")
    private WebElement ownerName;

    @FindBy(xpath = "//h4[@data-testid='user-full-name']//..//div")
    private WebElement ownerLabel;

    @FindBy(id = "project-status")
    private WebElement projectStatusDropDown;

    @FindBy(id = "delete-btn-project")
    private WebElement btnProjectDelete;

    @FindBy(xpath = "//div[@data-testid='modal-paper-comp-delete-project-modal']")
    private WebElement deleteModal;

    @FindBy(id = "primary-delete-project-modal")
    private WebElement btnModalDelete;

    @FindBy(id = "secondary-delete-project-modal")
    private WebElement btnModalCancel;

    private PageUtils pageUtils;

    public ProjectsDetailsPage(WebDriver driver) {
        this(driver, log);
    }

    private WebDriver driver;
    private ProjectsPartsAndAssemblyTableController projectsPartsAndAssemblyTableController;

    public ProjectsDetailsPage(WebDriver driver, Logger logger) {
        super(driver, logger);
        this.driver = driver;
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

    /**
     * Checks if show/hide option displayed
     *
     * @return true/false
     */
    public boolean isDetailsShowHideOptionDisplayed() {
        return getPageUtils().isElementDisplayed(btnShowHideOption);
    }

    /**
     * Hide user details
     *
     * @return current page object
     */
    public ProjectsDetailsPage hideProjectUserDetails(String userDetails) {
        getPageUtils().waitForElementAndClick(btnShowHideOption);
        getPageUtils().waitForElementAppear(showHideSearchField).sendKeys(userDetails);
        getPageUtils().waitForElementAndClick(btnSwitchToggle);
        return this;
    }

    /**
     * clicks on show all
     *
     * @return current page object
     */
    public ProjectsDetailsPage clickShowAll() {
        getPageUtils().waitForElementAndClick(btnShowAll);
        return this;
    }

    /**
     * clicks on hide all
     *
     * @return current page object
     */
    public ProjectsDetailsPage clickHideAll() {
        getPageUtils().waitForElementAndClick(btnHideAll);
        return this;
    }

    /**
     * Get save button status
     *
     * @return a String
     */
    public String getUserTableHeaders() {
        return getPageUtils().waitForElementAppear(tblHeaders).getAttribute("innerText");
    }

    /**
     * Checks if project owner's email is displayed
     *
     * @return true/false
     */
    public boolean isOwnerEmailDisplayed(String ownerEmail) {
        getPageUtils().waitForElementsToAppear(tableRow);
        return getPageUtils().isElementDisplayed(By.xpath("//h4[@data-testid='user-full-name']//..//..//following-sibling::div//span[text()='" + ownerEmail + "']"));
    }

    /**
     * Get project owner's name
     *
     * @return a String
     */
    public String getProjectOwnerName(String ownerEmail) {
        return getPageUtils().waitForElementToAppear(By.xpath("//span[text()='" + ownerEmail + "']//..//..//h4[@data-testid='user-full-name']")).getText();
    }

    /**
     * Checks if project owner label displayed
     *
     * @return true/false
     */
    public boolean isOwnerLabelDisplayed() {
        return getPageUtils().isElementDisplayed(ownerLabel);
    }

    /**
     * Checks if project status dropdown displayed
     *
     * @return true/false
     */
    public boolean isProjectStatusDroDownDisplayed() {
        return getPageUtils().isElementDisplayed(projectStatusDropDown);
    }

    /**
     * Change project status
     *
     * @return current page object
     */
    public ProjectsDetailsPage changeProjectStatus(String projectStatus) {
        getPageUtils().waitForElementAndClick(projectStatusDropDown);
        getPageUtils().waitForElementToAppear(By.xpath("//span[text()='" + projectStatus + "']")).click();
        getPageUtils().waitForElementsToNotAppear(By.xpath("//div[@data-testid='loader']"),5);
        return this;
    }

    /**
     * Navigate to project page
     *
     * @return new page object
     */
    public ProjectsPage navigateToAllProjects() {
        getPageUtils().waitForElementAndClick(btnAllProjects);
        return new ProjectsPage(driver);
    }

    /**
     * project page validations
     *
     * @return current page object
     */
    public ProjectsDetailsPage projectDetailsValidations(String dateTime) {
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(this.getProjectDetailsPageTitle()).contains("Automation Project " + dateTime);
        softAssertions.assertThat(this.isAllProjectsNavigationDisplayed()).isEqualTo(true);
        softAssertions.assertThat(this.isProjectDetailsPageTabsDisplayed("Details")).isEqualTo(true);
        softAssertions.assertThat(this.isProjectDetailsPageTabsDisplayed("Parts & Assemblies")).isEqualTo(true);
        softAssertions.assertThat(this.isProjectDetailsPageTabsDisplayed("Users")).isEqualTo(true);
        softAssertions.assertAll();
        return this;
    }

    /**
     * project details tab validations
     *
     * @return current page object
     */
    public ProjectsDetailsPage projectDetailsTabValidations(String dateTime, UserCredentials currentUser) {
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(this.isProjectDetailsDisplays("Owner")).isNotEmpty();
        softAssertions.assertThat(this.isProjectDetailsDisplays("Due Date")).isNotEmpty();
        softAssertions.assertThat(this.getProjectDetailsTabTitle()).contains("Details");
        softAssertions.assertThat(this.isProjectDetailsDisplays("Name")).contains("Automation Project " + dateTime);
        softAssertions.assertThat(this.isProjectDetailsDisplays("Description")).contains("This Project is created by Automation User " + currentUser.getEmail());
        softAssertions.assertAll();
        return this;
    }

    /**
     * project parts and assemblies tab validations
     *
     * @return current page object
     */
    public ProjectsDetailsPage projectPartsAndAssembliesTabValidations(String componentName, String scenarioName) {
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(this.isShowHideOptionDisplayed()).isEqualTo(true);
        softAssertions.assertThat(this.isSearchOptionDisplayed()).isEqualTo(true);
        softAssertions.assertThat(this.isFilterOptionDisplayed()).isEqualTo(true);
        softAssertions.assertThat(this.getTableHeaders()).contains(CisColumnsEnum.COMPONENT_NAME.getColumns(),CisColumnsEnum.SCENARIO_NAME.getColumns(),
                CisColumnsEnum.COMPONENT_TYPE.getColumns(), CisColumnsEnum.STATE.getColumns(), CisColumnsEnum.PROCESS_GROUP.getColumns(), CisColumnsEnum.DIGITAL_FACTORY.getColumns(), CisColumnsEnum.CREATED_AT.getColumns(),
                CisColumnsEnum.CREATED_BY.getColumns(), CisColumnsEnum.ANNUAL_VOLUME.getColumns(), CisColumnsEnum.BATCH_SIZE.getColumns());

        softAssertions.assertThat(this.getListOfScenarios(componentName, scenarioName)).isEqualTo(1);
        softAssertions.assertThat(this.getPinnedTableHeaders()).contains(CisColumnsEnum.COMPONENT_NAME.getColumns(),CisColumnsEnum.SCENARIO_NAME.getColumns());
        softAssertions.assertAll();
        return this;
    }

    /**
     * project users tab validations
     *
     * @return current page object
     */
    public ProjectsDetailsPage projectUserTabValidations(UserCredentials currentUser) {
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(this.isDetailsShowHideOptionDisplayed()).isEqualTo(true);
        softAssertions.assertThat(this.getUserTableHeaders()).contains("Full Name","Job title");
        softAssertions.assertThat(this.isOwnerEmailDisplayed(currentUser.getEmail())).isEqualTo(true);
        softAssertions.assertThat(this.getProjectOwnerName(currentUser.getEmail())).contains("QA Automation Account");
        softAssertions.assertThat(this.isOwnerLabelDisplayed()).isEqualTo(true);
        softAssertions.assertAll();
        return this;
    }

    /**
     * project details tab validations after edit
     *
     * @return current page object
     */
    public ProjectsDetailsPage projectDetailsTabValidationsAfterEdit(String dateTime, UserCredentials currentUser) {
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(this.isProjectDetailsDisplays("Owner")).isNotEmpty();
        softAssertions.assertThat(this.isProjectDetailsDisplays("Due Date")).isNotEmpty();
        softAssertions.assertThat(this.getProjectDetailsTabTitle()).contains("Details");
        softAssertions.assertThat(this.isProjectDetailsDisplays("Name")).contains("Automation Project " + dateTime);
        softAssertions.assertThat(this.isProjectDetailsDisplays("Description")).contains("This Project is edited by Automation User " + currentUser.getEmail());
        softAssertions.assertAll();
        return this;
    }

    /**
     * Checks if project delete button displayed
     *
     * @return true/false
     */
    public boolean isDeleteButtonDisplayed() {
        return getPageUtils().isElementDisplayed(btnProjectDelete);
    }

    /**
     * clicks on delete button
     *
     * @return current page object
     */
    public ProjectsDetailsPage clickDeleteProject() {
        getPageUtils().waitForElementAndClick(btnProjectDelete);
        return this;
    }

    /**
     * Checks if delete modal displayed
     *
     * @return true/false
     */
    public boolean isDeleteModalDisplayed() {
        return getPageUtils().isElementDisplayed(deleteModal);
    }

    /**
     * get delete confirmation text
     *
     * @return a String
     */
    public String getDeleteConfirmation() {
        return getPageUtils().waitForElementAppear(deleteModal).getText();
    }

    /**
     * Checks if modal delete button displayed
     *
     * @return true/false
     */
    public boolean isModalDeleteButtonDisplayed() {
        return getPageUtils().isElementDisplayed(btnModalDelete);
    }

    /**
     * Checks if modal cancel button displayed
     *
     * @return true/false
     */
    public boolean isModalCancelButtonDisplayed() {
        return getPageUtils().isElementDisplayed(btnModalCancel);
    }

    /**
     * clicks on modal cancel button
     *
     * @return new page object
     */
    public ProjectsPage clickModalCancelProject() {
        getPageUtils().waitForElementAndClick(btnModalCancel);
        return new ProjectsPage(driver);
    }

    /**
     * clicks on modal delete button
     *
     * @return new page object
     */
    public ProjectsPage clickModalDeleteProject() {
        getPageUtils().waitForElementAndClick(btnModalDelete);
        return new ProjectsPage(driver);
    }

    /**
     * project delete modal validations
     *
     * @return current page object
     */
    public ProjectsDetailsPage projectDeleteModalValidations() {
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(this.isDeleteModalDisplayed()).isEqualTo(true);
        softAssertions.assertThat(this.getDeleteConfirmation()).contains("This will permanently delete this Project and all data.");
        softAssertions.assertThat(this.isModalDeleteButtonDisplayed()).isEqualTo(true);
        softAssertions.assertThat(this.isModalCancelButtonDisplayed()).isEqualTo(true);
        softAssertions.assertAll();
        return this;
    }
}