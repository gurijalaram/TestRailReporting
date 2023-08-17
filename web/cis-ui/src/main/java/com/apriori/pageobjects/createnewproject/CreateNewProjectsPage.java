package com.apriori.pageobjects.createnewproject;

import com.apriori.EagerPageComponent;
import com.apriori.PageUtils;
import com.apriori.common.ProjectPartsAndAssemblyTableController;
import com.apriori.pageobjects.projects.ProjectsPage;
import com.apriori.pageobjects.projectsdetails.ProjectsDetailsPage;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;

import java.util.List;

@Slf4j
public class CreateNewProjectsPage extends EagerPageComponent<CreateNewProjectsPage> {

    @FindBy(id = "create-project-name-input")
    private WebElement projectNameField;

    @FindBy(id = "create-project-description-input")
    private WebElement projectDescriptionField;

    @FindBy(id = "create-project-add-part-and-assembly-btn")
    private WebElement btnAddPartsAndAssemblies;

    @FindBy(xpath = "//div[starts-with(@Class,'MuiDataGrid-cellCheckbox')]")
    private List<WebElement> tableRow;

    @FindBy(id = "add-part-and-assembly-add-btn")
    private WebElement btnAddPartsAndAssembliesToProject;

    @FindBy(xpath = "//span[contains(text(),'Make Selection')]")
    private WebElement inviteTeammatesField;

    @FindBy(xpath = "//input[@placeholder='Search']")
    private WebElement inviteTeammatesSearchField;

    @FindBy(xpath = "//div[@data-testid='create-project-due-date']")
    private WebElement dueDateField;

    @FindBy(id = "create-project-submit-btn")
    private WebElement btnProjectSubmit;

    @FindBy(xpath = "//div[@aria-live='polite']")
    private WebElement btnYear;

    @FindBy(id = "create-project-add-part-and-assembly-modal")
    private WebElement addPartsModal;

    @FindBy(xpath = "//p[@data-testid='toolbar-Show/Hide Fields']")
    private WebElement showHideFieldsOption;

    @FindBy(id = "filter-control")
    private WebElement filterTableOption;

    @FindBy(xpath = "//div[@data-testid='search-control']")
    private WebElement searchTableOption;

    @FindBy(xpath = "//div[@role='grid']")
    private WebElement partsAndAssembliesTable;

    @FindBy(xpath = "//p[@data-testid ='toolbar-Search']")
    private WebElement btnSearch;

    @FindBy(xpath = "//div[@data-testid ='search-control-input']")
    private WebElement fieldSearch;

    @FindBy(xpath = "//div[@data-testid='search-control-input']//input")
    private WebElement searchInputField;

    @FindBy(xpath = "//div[@class='MuiDataGrid-row']")
    private WebElement filterRecords;

    @FindBy(xpath = "//div[@data-testid='search-control-popper']//button//*[local-name()='svg']")
    private WebElement btnClear;

    @FindBy(xpath = "//*[local-name()='svg' and @data-icon='times-circle']")
    private WebElement removePartIcon;

    @FindBy(id = "create-project-delete-team-member")
    private WebElement removeUserIcon;

    @FindBy(id = "create-project-cancel-btn")
    private WebElement btnCancelProject;

    @FindBy(xpath = "//p[@data-testid='input-field-helper-text']")
    private WebElement projectNameFieldValidation;

    @FindBy(xpath = "//ul[@role='listbox']")
    private WebElement memberList;

    @FindBy(xpath = "//div[@data-testid='list-subitem-text-left-menu.subTitle.projects']")
    private WebElement btnProjects;

    @FindBy(id = "create-new-button")
    private WebElement btnCreateNewProject;

    @FindBy(xpath = "//button[@data-testid='toolbar-control-button-active']//p[@data-testid='toolbar-Unread']")
    private WebElement btnUnread;

    @FindBy(xpath = "//h3[@data-testid='name']")
    private WebElement projectNameLink;

    private PageUtils pageUtils;

    public CreateNewProjectsPage(WebDriver driver) {
        this(driver, log);
    }

    private WebDriver driver;
    private ProjectPartsAndAssemblyTableController projectPartsAndAssemblyTableController;

    public CreateNewProjectsPage(WebDriver driver, Logger logger) {
        super(driver, logger);
        this.driver = driver;
        this.projectPartsAndAssemblyTableController = new ProjectPartsAndAssemblyTableController(driver);
        PageFactory.initElements(driver, this);
    }

    @Override
    protected void isLoaded() throws Error {
    }

    /**
     * Type project name
     *
     * @return current page object
     */
    public CreateNewProjectsPage typeProjectName(String projectName) {
        getPageUtils().waitForElementToAppear(projectNameField).sendKeys(projectName);
        return this;
    }

    /**
     * Type project description
     *
     * @return current page object
     */
    public CreateNewProjectsPage typeProjectDescription(String projectDescription) {
        getPageUtils().waitForElementToAppear(projectDescriptionField).sendKeys(projectDescription);
        return this;
    }

    /**
     * clicks on add new parts and assemblies
     *
     * @return current page object
     */
    public CreateNewProjectsPage clickOnAddNewButton() {
        getPageUtils().waitForElementAndClick(btnAddPartsAndAssemblies);
        getPageUtils().waitForElementsToAppear(tableRow);
        return this;
    }

    /**
     * Select a part
     *
     * @return current page object
     */
    public CreateNewProjectsPage selectAPart(String scenarioName, String componentName) {
        getPageUtils().waitForElementAndClick(By.xpath("//div[@data-field='scenarioName']//p[text()='" + scenarioName + "']/ancestor::div[@role='row']//div[@data-field='componentName']//p[text()='" + componentName + "']//..//..//parent::div//span"));
        return this;
    }

    /**
     * Click on add button
     *
     * @return current page object
     */
    public CreateNewProjectsPage clickAdd() {
        getPageUtils().waitForElementAndClick(btnAddPartsAndAssembliesToProject);
        return this;
    }

    /**
     * Select a user
     *
     * @return current page object
     */
    public CreateNewProjectsPage selectAUser(String teamMember) {
        getPageUtils().waitForElementAndClick(inviteTeammatesField);
        getPageUtils().waitForElementToAppear(memberList);
        getPageUtils().waitForElementToAppear(inviteTeammatesSearchField).sendKeys(teamMember);
        getPageUtils().waitForElementAndClick(By.xpath("//span[contains(text(),'" + teamMember + "')]"));
        return this;
    }

    /**
     * set due date
     *
     * @return current page object
     */
    public CreateNewProjectsPage setDueDate(String year, String date) {
        getPageUtils().scrollWithJavaScript(dueDateField,true);
        getPageUtils().waitForElementAndClick(dueDateField);
        getPageUtils().waitForElementAndClick(btnYear);
        getPageUtils().waitForElementAndClick(By.xpath("//button[contains(text(),'" + year + "')]"));
        getPageUtils().waitForElementAndClick(By.xpath("//button[contains(text(),'" + date + "')]"));
        getPageUtils().scrollWithJavaScript(projectNameField,false);
        return this;
    }

    /**
     * Save project
     *
     * @return current page object
     */
    public ProjectsPage saveProject() {
        getPageUtils().waitForElementAndClick(btnProjectSubmit);
        return new ProjectsPage(getDriver());
    }

    /**
     * Checks if add parts modal displayed
     *
     * @return true/false
     */
    public boolean isAddPartsModalDisplayed() {
        return getPageUtils().isElementDisplayed(addPartsModal);
    }

    /**
     * Checks if show/hide fields option displayed
     *
     * @return true/false
     */
    public boolean isShowHideFieldOptionDisplayed() {
        return getPageUtils().isElementDisplayed(showHideFieldsOption);
    }

    /**
     * Checks if filter table option displayed
     *
     * @return true/false
     */
    public boolean isFilterTableOptionDisplayed() {
        return getPageUtils().isElementDisplayed(filterTableOption);
    }

    /**
     * Checks if search table option displayed
     *
     * @return true/false
     */
    public boolean isSearchTableOptionDisplayed() {
        return getPageUtils().isElementDisplayed(searchTableOption);
    }

    /**
     * Checks if parts & assemblies table displayed
     *
     * @return true/false
     */
    public boolean isPartsAndAssembliesTableDisplayed() {
        return getPageUtils().isElementDisplayed(partsAndAssembliesTable);
    }

    /**
     * Click on search button
     *
     * @return current page object
     */
    public CreateNewProjectsPage clickSearch() {
        getPageUtils().waitForElementAndClick(btnSearch);
        return this;
    }

    /**
     * Checks if search field displayed
     *
     * @return true/false
     */
    public boolean isSearchFieldDisplayed() {
        return getPageUtils().isElementDisplayed(fieldSearch);
    }

    /**
     * Click on search box
     *
     * @return current page object
     */
    public CreateNewProjectsPage clickOnSearchField() {
        getPageUtils().waitForElementAndClick(fieldSearch);
        return this;
    }

    /**
     * Enter component name on search field
     *
     * @return current page object
     */
    public CreateNewProjectsPage enterAComponentName(String componentName) {
        getPageUtils().waitForElementToAppear(searchInputField).sendKeys(componentName);
        getPageUtils().waitForElementToAppear(filterRecords);
        return this;
    }

    /**
     * Get the added component Name
     *
     * @return a String
     */
    public String getAddedComponentName() {
        return getPageUtils().waitForElementToAppear(searchInputField).getAttribute("value");
    }

    /**
     * Gets the number of elements present on the page
     *
     * @param componentName - name of the part
     * @param scenarioName  - scenario name
     * @return size of the element as int
     */
    public int getListOfScenarios(String componentName, String scenarioName) {
        return projectPartsAndAssemblyTableController.getListOfScenarios(componentName, scenarioName);
    }

    /**
     * Click on clear icon on search field
     *
     * @return current page object
     */
    public CreateNewProjectsPage clickClearOption() {
        getPageUtils().waitForElementAndClick(btnClear);
        getPageUtils().waitForElementsToNotAppear(By.xpath("//div[@class='MuiDataGrid-row MuiDataGrid-row--lastVisible']"));
        return this;
    }

    /**
     * Checks if component/scenario names displayed
     *
     * @return true/false
     */
    public boolean isAddedPartComponentAndScenarioNamesDisplayed(String partName) {
        return getPageUtils().isElementDisplayed(By.xpath("//p[text()='" + partName + "']"));
    }

    /**
     * Checks if remove part icon displayed
     *
     * @return true/false
     */
    public boolean isRemovePartIconDisplayed() {
        return getPageUtils().isElementDisplayed(removePartIcon);
    }

    /**
     * Click on remove part
     *
     * @return current page object
     */
    public CreateNewProjectsPage clickOnRemovePart() {
        getPageUtils().waitForElementAndClick(removePartIcon);
        return this;
    }

    /**
     * Checks if invite members option displayed
     *
     * @return true/false
     */
    public boolean isInviteMembersOptionDisplayed() {
        return getPageUtils().isElementDisplayed(inviteTeammatesField);
    }

    /**
     * Checks if invite members search field displayed
     *
     * @return true/false
     */
    public boolean isInviteMembersSearchDisplayed() {
        return getPageUtils().isElementDisplayed(inviteTeammatesSearchField);
    }

    /**
     * Checks if selected users displayed
     *
     * @return true/false
     */
    public boolean isAddedUsersDisplayed(String user) {
        return getPageUtils().isElementDisplayed(By.xpath("//p[text()='" + user + "']"));
    }

    /**
     * Click to remove a selected user
     *
     * @return current page object
     */
    public CreateNewProjectsPage clickOnRemoveUser() {
        getPageUtils().waitForElementAndClick(removeUserIcon);
        return this;
    }

    /**
     * create a new project
     *
     * @return a new page object
     */
    public ProjectsPage createANewProject(String projectName, String projectDescription, String scenarioName, String componentName, String teamMember, String year, String date) {
        getPageUtils().waitForElementToAppear(projectNameField).sendKeys(projectName);
        getPageUtils().waitForElementToAppear(projectDescriptionField).sendKeys(projectDescription);
        getPageUtils().waitForElementAndClick(btnAddPartsAndAssemblies);
        getPageUtils().waitForElementsToAppear(tableRow);
        getPageUtils().waitForElementAndClick(By.xpath("//div[@data-field='scenarioName']//p[text()='" + scenarioName + "']/ancestor::div[@role='row']//div[@data-field='componentName']//p[text()='" + componentName + "']//..//..//parent::div//span"));
        getPageUtils().waitForElementAndClick(btnAddPartsAndAssembliesToProject);
        getPageUtils().waitForElementAndClick(inviteTeammatesField);
        getPageUtils().waitForElementToAppear(memberList);
        getPageUtils().waitForElementToAppear(inviteTeammatesSearchField).sendKeys(teamMember);
        getPageUtils().waitForElementAndClick(By.xpath("//span[contains(text(),'" + teamMember + "')]"));
        getPageUtils().scrollWithJavaScript(dueDateField,true);
        getPageUtils().waitForElementAndClick(dueDateField);
        getPageUtils().waitForElementAndClick(btnYear);
        getPageUtils().waitForElementAndClick(By.xpath("//button[contains(text(),'" + year + "')]"));
        getPageUtils().waitForElementAndClick(By.xpath("//button[contains(text(),'" + date + "')]"));
        getPageUtils().scrollWithJavaScript(projectNameField,false);
        getPageUtils().waitForElementAndClick(btnProjectSubmit);
        return new ProjectsPage(getDriver());
    }

    /**
     * Checks if project name field displayed
     *
     * @return true/false
     */
    public boolean isProjectNameFieldDisplayed() {
        return getPageUtils().isElementDisplayed(projectNameField);
    }

    /**
     * Checks if project description field displayed
     *
     * @return true/false
     */
    public boolean isProjectDescriptionFieldDisplayed() {
        return getPageUtils().isElementDisplayed(projectDescriptionField);
    }

    /**
     * Checks if add parts and assemblies displayed
     *
     * @return true/false
     */
    public boolean isAddPartsAndAssembliesOptionDisplayed() {
        return getPageUtils().isElementDisplayed(btnAddPartsAndAssemblies);
    }

    /**
     * Checks if invite members list displayed
     *
     * @return true/false
     */
    public boolean isInviteTeamMembersFieldDisplayed() {
        return getPageUtils().isElementDisplayed(inviteTeammatesField);
    }

    /**
     * Checks if due date field displayed
     *
     * @return true/false
     */
    public boolean isDueDateFieldDisplayed() {
        return getPageUtils().isElementDisplayed(dueDateField);
    }

    /**
     * Checks if create project button displayed
     *
     * @return true/false
     */
    public boolean isCreateProjectButtonDisplayed() {
        return getPageUtils().isElementDisplayed(btnProjectSubmit);
    }

    /**
     * Checks if cancel project button displayed
     *
     * @return true/false
     */
    public boolean isCancelProjectCreationDisplayed() {
        return getPageUtils().isElementDisplayed(btnCancelProject);
    }

    /**
     * clicks on due date field
     *
     * @return current page object
     */
    public CreateNewProjectsPage clickOnDueDatePicker() {
        getPageUtils().waitForElementAndClick(dueDateField);
        return this;
    }

    /**
     * gets previous month status
     *
     * @return a String
     */
    public String getMonthSelectorStatus(String month) {
        return getPageUtils().waitForElementToAppear(By.xpath("//button[@title='" + month + "']")).getAttribute("class");
    }

    /**
     * clicks on cancel project
     *
     * @return new page object
     */
    public ProjectsPage clickOnCancelProject() {
        getPageUtils().waitForElementAndClick(btnCancelProject);
        return new ProjectsPage(driver);
    }

    /**
     * Get create project button status
     *
     * @return a string
     */
    public String getProjectCreateStatus() {
        return getPageUtils().waitForElementToAppear(btnProjectSubmit).getAttribute("class");
    }

    /**
     * Clear project name
     *
     * @return current page object
     */
    public CreateNewProjectsPage clearProjectName() {
        getPageUtils().waitForElementAndClick(projectNameField);
        getPageUtils().clearValueOfElement(projectNameField);
        return this;
    }

    /**
     * Checks if project name validation displayed
     *
     * @return true/false
     */
    public boolean isProjectNameRequiredValidationDisplayed() {
        return getPageUtils().isElementDisplayed(projectNameFieldValidation);
    }

    /**
     * get component checkbox status
     *
     * @return a string
     */
    public String getComponentStatus(String scenarioName, String componentName) {
        return getPageUtils().waitForElementToAppear(By.xpath("//div[@data-field='scenarioName']//p[text()='" + scenarioName + "']/ancestor::div[@role='row']//div[@data-field='componentName']//p[text()='" + componentName + "']//..//..//parent::div//span")).getAttribute("class");
    }

    /**
     * create a new project and open
     *
     * @return a new page object
     */
    public ProjectsDetailsPage createANewProjectAndOpen(String projectName, String projectDescription, String scenarioName, String componentName, String teamMember, String year, String date, String tabName) {
        this.createANewProject(projectName, projectDescription, scenarioName, componentName, teamMember, year, date);
        this.clickOnUnreadButton();
        this.openTheProject(tabName);
        return new ProjectsDetailsPage(getDriver());
    }

    /**
     * clicks on unread button
     *
     * @return new page object
     */
    public ProjectsPage clickOnUnreadButton() {
        getPageUtils().waitForElementAndClick(btnUnread);
        return new ProjectsPage(getDriver());
    }

    /**
     * open the project
     *
     * @return new page object
     */
    public ProjectsDetailsPage openTheProject(String tabName) {
        getPageUtils().javaScriptClick(projectNameLink);
        getPageUtils().waitForElementAndClick(By.xpath("//div[@role='tablist']//button[contains(text(),'" + tabName + "')]"));
        return new ProjectsDetailsPage(getDriver());
    }

    /**
     * Get the table records count
     *
     * @return int
     */
    public int getTableRecordsCount() {
        return getPageUtils().waitForElementsToAppear(tableRow).size();
    }
}