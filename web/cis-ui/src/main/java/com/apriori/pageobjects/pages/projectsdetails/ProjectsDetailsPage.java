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

    @FindBy(id = "invite-user-btn")
    private WebElement btnInviteUsers;

    @FindBy(xpath = "//div[@data-testid='modal-paper-comp-invite-project-users-modal']")
    private WebElement inviteUsersModal;

    @FindBy(id = "project-user-share-chip-dropdown")
    private WebElement usersDropdownOption;

    @FindBy(id = "invite-button")
    private WebElement btnInvite;

    @FindBy(xpath = "//div[contains(@id,'project-user-share-chip-dropdown')]")
    private WebElement selectedProjectUserToAdd;

    @FindBy(xpath = "//button[contains(@id,'bulk-action-btn')]")
    private WebElement btnRemoveFromProject;

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
    public String isProjectDetailsDisplayed(String section) {
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
     * @return current page object
     */
    public ProjectsDetailsPage clickModalCancelProject() {
        getPageUtils().waitForElementAndClick(btnModalCancel);
        return this;
    }

    /**
     * clicks on modal delete button
     *
     * @return new page object
     */
    public ProjectsPage clickModalDeleteProject() {
        getPageUtils().javaScriptClick(btnModalDelete);
        return new ProjectsPage(driver);
    }

    /**
     * Checks if invite users button displayed
     *
     * @return true/false
     */
    public boolean isInviteUsersOptionDisplayed() {
        return getPageUtils().isElementDisplayed(btnInviteUsers);
    }

    /**
     * clicks on invite users button
     *
     * @return current page object
     */
    public ProjectsDetailsPage clickInviteUsersOption() {
        getPageUtils().waitForElementAndClick(btnInviteUsers);
        return this;
    }

    /**
     * Checks if invite users modal displayed
     *
     * @return true/false
     */
    public boolean isInviteUsersModalDisplayed() {
        getPageUtils().waitForElementsToNotAppear(By.xpath("//div[@data-testid='loader']"),5);
        return getPageUtils().isElementDisplayed(inviteUsersModal);
    }

    /**
     * Checks if users drop down is displayed
     *
     * @return true/false
     */
    public boolean isUsersDropDownDisplayed() {
        return getPageUtils().waitForElementAppear(usersDropdownOption).isDisplayed();
    }

    /**
     * Checks if invite button is displayed
     *
     * @return true/false
     */
    public boolean isInviteButtonDisplayed() {
        return getPageUtils().waitForElementAppear(btnInvite).isDisplayed();
    }

    /**
     * select a user from drop-down
     *
     * @return current page object
     */
    public ProjectsDetailsPage selectAUser(String user) {
        getPageUtils().waitForElementAndClick(usersDropdownOption);
        getPageUtils().waitForElementToAppear(usersDropdownOption).sendKeys(user);
        getPageUtils().waitForElementAndClick(By.xpath("//span[contains(text(),'" + user + "')]"));
        return this;
    }

    /**
     * get selected user name
     *
     * @return a String
     */
    public String getSelectedProjectUserName() {
        return getPageUtils().waitForElementToAppear(selectedProjectUserToAdd).getAttribute("innerText");
    }

    /**
     * clicks on Invite button
     *
     * @return current page object
     */
    public ProjectsDetailsPage clickOnInvite() {
        getPageUtils().waitForElementToAppear(btnInvite);
        getPageUtils().moveAndClick(btnInvite);
        return this;
    }

    /**
     * Checks if newly added user is displayed
     *
     * @return true/false
     */
    public boolean isAddedProjectUserDisplayed(String addedUserEmail) {
        getPageUtils().waitForElementsToNotAppear(By.xpath("//div[@data-testid='loader']"),5);
        getPageUtils().waitForElementsToAppear(tableRow);
        return getPageUtils().isElementDisplayed(By.xpath("//h4[@data-testid='user-full-name']//..//..//following-sibling::div//span[text()='" + addedUserEmail + "']"));
    }

    /**
     * clicks on project user checkbox
     *
     * @return current page object
     */
    public ProjectsDetailsPage selectAProjectUser(String projectUser) {
        getPageUtils().waitForElementAndClick(By.xpath("//h4[@data-testid='user-full-name']//..//..//following-sibling::div//span[text()='" + projectUser + "']/ancestor::div[@role='row']//div[@role='cell']"));
        return this;
    }

    /**
     * Checks if remove from project button is displayed
     *
     * @return true/false
     */
    public boolean isRemoveFromProjectOptionDisplayed() {
        return getPageUtils().waitForElementAppear(btnRemoveFromProject).isDisplayed();
    }

    /**
     * clicks on remove from project button
     *
     * @return current page object
     */
    public ProjectsDetailsPage clickOnRemoveFromProjectOption() {
        getPageUtils().waitForElementAndClick(btnRemoveFromProject);
        return this;
    }

    /**
     * get user remove option status
     *
     * @return a String
     */
    public String getRemoveUserFromProjectOptionStatus() {
        return getPageUtils().waitForElementToAppear(btnRemoveFromProject).getAttribute("class");
    }
}