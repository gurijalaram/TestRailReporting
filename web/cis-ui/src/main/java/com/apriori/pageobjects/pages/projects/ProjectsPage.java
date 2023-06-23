package com.apriori.pageobjects.pages.projects;

import com.apriori.pageobjects.pages.createnewproject.CreateNewProjectsPage;
import com.apriori.pageobjects.pages.projectsdetails.ProjectsDetailsPage;
import com.apriori.utils.PageUtils;
import com.apriori.utils.web.components.EagerPageComponent;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;

@Slf4j
public class ProjectsPage extends EagerPageComponent<ProjectsPage> {

    @FindBy(xpath = "//h3[@data-testid='title']")
    private WebElement title;

    @FindBy(id = "create-new-button")
    private WebElement btnCreateNewProject;

    @FindBy(xpath = "//div[@data-testid='loader']")
    private WebElement spinner;

    @FindBy(xpath = "//div[contains(@class,'MuiCalendarPicker')]")
    private WebElement dueDateDatePicker;

    @FindBy(xpath = "//button[@title='Previous month']")
    private WebElement previousMonthSelector;

    @FindBy(xpath = "//button[@data-testid='toolbar-control-button-active']//p[@data-testid='toolbar-Unread']")
    private WebElement btnUnread;

    @FindBy(xpath = "//h3[@data-testid='displayName']")
    private WebElement projectName;

    @FindBy(xpath = "(//span[@data-testid='data-label-Due Date']//following::span)[1]")
    private WebElement dueDate;

    @FindBy(xpath = "(//span[@data-testid='data-label-Owner']//following::span)[1]")
    private WebElement owner;

    @FindBy(xpath = "//button[@data-testid='toolbar-control-button-inactive']//p[@data-testid='toolbar-Search']")
    private WebElement btnSearch;

    @FindBy(xpath = "//div[@data-testid='search-control-input']//input[@placeholder='Search by name...']")
    private WebElement searchField;

    @FindBy(xpath = "//div[@data-testid='search-control-input']//button//*[local-name()='svg']")
    private WebElement searchRemoveIcon;

    @FindBy(xpath = "(//span[@data-testid='data-label-Organization']//following::span)[1]")
    private WebElement organization;

    @FindBy(xpath = "(//div[starts-with(@class,'MuiAvatarGroup-root')])[1]")
    private WebElement participants;

    @FindBy(id = "filter-control-projects")
    private WebElement projectFilterOption;

    @FindBy(id = "add-condition-button-filter-control-projects")
    private WebElement addCondition;

    @FindBy(xpath = "//div[contains(@id,'filter-field-select')]")
    private WebElement filterField;

    @FindBy(xpath = "//div[contains(@id,'filter-condition-type')]")
    private WebElement filterConditionType;

    @FindBy(xpath = "//input[contains(@id,'filter-value')]")
    private WebElement filterValue;

    @FindBy(xpath = "//li[@data-value='displayName']")
    private WebElement projectNameFilterFiled;

    @FindBy(xpath = "//li[@data-value='[CN]']")
    private WebElement containsFilterType;

    @FindBy(xpath = "popover-filter-control-projects")
    private WebElement filterModal;

    @FindBy(xpath = "//li[@data-value='status']")
    private WebElement projectStatusFilterFiled;

    @FindBy(xpath = "//li[@data-value='[IN]']")
    private WebElement isAnyOfFilterType;

    @FindBy(xpath = "//li[@data-value='dueAt']")
    private WebElement projectDueDateFilterFiled;

    @FindBy(xpath = "//li[@data-value='[LT]']")
    private WebElement isBeforeFilterType;

    @FindBy(xpath = "//div[@aria-live='polite']")
    private WebElement btnYear;

    @FindBy(xpath = "//button[@data-testid='toolbar-control-button-active']//p[@data-testid='toolbar-Filter (1)']")
    private WebElement addedFilterOption;

    @FindBy(xpath = "//div[contains(@data-testid,'filter-value')]")
    private WebElement datePickerFilterValue;

    @FindBy(xpath = "//li[contains(@id,'filter-value')]/div/span/span")
    private WebElement inNegotiationOption;

    @FindBy(xpath = "//span[@data-testid='data-label-Status']/following-sibling::span")
    private WebElement projectStatus;

    @FindBy(xpath = "//button[@data-testid='toolbar-control-button-inactive']//p[@data-testid='toolbar-Unread']")
    private WebElement btnRead;

    private PageUtils pageUtils;

    public ProjectsPage(WebDriver driver) {
        this(driver, log);
    }

    private WebDriver driver;

    public ProjectsPage(WebDriver driver, Logger logger) {
        super(driver, logger);
        this.driver = driver;
        this.waitForProjectsPageLoad();
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
     * Get the page title
     *
     * @return a string
     */
    public String getPageTitle() {
        return getPageUtils().waitForElementAppear(title).getText();
    }

    /**
     * Checks if create new project button displayed
     *
     * @return true/false
     */
    public boolean isCreateNewProjectsOptionDisplayed() {
        return getPageUtils().isElementDisplayed(btnCreateNewProject);
    }

    /**
     * clicks on create new project
     *
     * @return current page object
     */
    public CreateNewProjectsPage clickOnCreateNewProject() {
        getPageUtils().waitForElementAndClick(btnCreateNewProject);
        return new CreateNewProjectsPage(getDriver());
    }

    /**
     * clicks on unread filter
     *
     * @return current page object
     */
    public ProjectsPage clickOnUnread() {
        getPageUtils().waitForElementAndClick(btnUnread);
        getPageUtils().waitForElementsToNotAppear(By.xpath("//div[@data-testid='loader']"),5);
        return this;
    }

    /**
     * Get created project name
     *
     * @return current page object
     */
    public String getProjectName() {
        return getPageUtils().waitForElementToAppear(projectName).getText();

    }

    /**
     * Get project Details
     *
     * @return current page object
     */
    public String getProjectDetails(String projectName) {
        return getPageUtils().waitForElementToAppear(By.xpath("//h3[contains(text(),'" + projectName + "')]//..//..//parent::div")).getAttribute("innerText");

    }

    /**
     * Get project due date
     *
     * @return current page object
     */
    public String getDueDate() {
        return getPageUtils().waitForElementToAppear(dueDate).getText();

    }

    /**
     * Get project owner
     *
     * @return current page object
     */
    public String getProjectOwner() {
        return getPageUtils().waitForElementToAppear(owner).getText();
    }

    /**
     * clicks on created project name
     *
     * @return new page object
     */
    public ProjectsDetailsPage clickOnCreatedProject() {
        getPageUtils().javaScriptClick(projectName);
        return new ProjectsDetailsPage(driver);
    }

    /**
     * Get project organization
     *
     * @return a string
     */
    public String getProjectOrganization() {
        return getPageUtils().waitForElementToAppear(organization).getText();
    }

    /**
     * Get project participants
     *
     * @return a string
     */
    public String getProjectParticipants() {
        return getPageUtils().waitForElementToAppear(participants).getText();
    }

    /**
     * Checks if search project button displayed
     *
     * @return true/false
     */
    public boolean isSearchProjectButtonDisplayed() {
        return getPageUtils().isElementDisplayed(btnSearch);
    }

    /**
     * Search project by name
     *
     * @return current page object
     */
    public ProjectsPage searchProject(String projectName) {
        getPageUtils().waitForElementAndClick(btnSearch);
        getPageUtils().waitForElementToAppear(searchField).sendKeys(projectName);
        getPageUtils().waitForElementsToNotAppear(By.xpath("//div[@data-testid='loader']"),5);
        return this;
    }

    /**
     * Remove search
     *
     * @return current page object
     */
    public ProjectsPage removeSearch() {
        getPageUtils().waitForElementAndClick(searchRemoveIcon);
        getPageUtils().waitForElementsToNotAppear(By.xpath("//div[@data-testid='loader']"),5);
        return this;
    }


    /**
     * Checks if project filter option displayed
     *
     * @return true/false
     */
    public boolean isProjectFilterOptionDisplayed() {
        return getPageUtils().isElementDisplayed(projectFilterOption);
    }

    /**
     * clicks on filter option
     *
     * @return true/false
     */
    public ProjectsPage clickOnFilterOption() {
        getPageUtils().waitForElementAndClick(projectFilterOption);
        return this;
    }

    /**
     * Checks if project filter add condition displayed
     *
     * @return true/false
     */
    public boolean isFilterAddConditionDisplayed() {
        return getPageUtils().isElementDisplayed(addCondition);
    }

    /**
     * clicks on add condition link
     *
     * @return true/false
     */
    public ProjectsPage clickOnAddCondition() {
        getPageUtils().waitForElementAndClick(addCondition);
        return this;
    }

    /**
     * Checks if filter field selector displayed
     *
     * @return true/false
     */
    public boolean isFilterFiledDisplayed() {
        return getPageUtils().waitForElementAppear(filterField).isDisplayed();
    }

    /**
     * Checks if filter type selector displayed
     *
     * @return true/false
     */
    public boolean isFilterConditionTypeDisplayed() {
        return getPageUtils().waitForElementAppear(filterConditionType).isDisplayed();
    }

    /**
     * Checks if filter value field displayed
     *
     * @return true/false
     */
    public boolean isFilterValueDisplayed() {
        return getPageUtils().waitForElementAppear(filterValue).isDisplayed();
    }

    /**
     * add project name to filter
     *
     * @return current page object
     */
    public ProjectsPage addProjectNameToFilter(String projectDisplayName) {
        getPageUtils().waitForElementAndClick(filterField);
        getPageUtils().waitForElementAndClick(projectNameFilterFiled);
        getPageUtils().waitForElementAndClick(filterConditionType);
        getPageUtils().waitForElementAndClick(containsFilterType);
        getPageUtils().waitForElementAndClick(filterValue);
        getPageUtils().waitForElementToAppear(filterValue).sendKeys(projectDisplayName);
        getPageUtils().waitForElementToAppear(addedFilterOption);
        getPageUtils().waitForElementToAppear(projectName);
        return this;
    }

    /**
     * Checks if the component is present on the page by size == 0 or > 0
     *
     * @return size of the element as int
     */
    public int getListOfProject(String projectName) {
        return getPageUtils().waitForElementsToAppear(By.xpath("//h3[contains(text(),'" + projectName + "')]")).size();
    }

    /**
     * select status to filter
     *
     * @return current page object
     */
    public ProjectsPage selectProjectStatus(String status) {
        getPageUtils().waitForElementAndClick(filterField);
        getPageUtils().waitForElementAndClick(projectStatusFilterFiled);
        getPageUtils().waitForElementAndClick(filterConditionType);
        getPageUtils().waitForElementAndClick(isAnyOfFilterType);
        getPageUtils().waitForElementAndClick(filterValue);
        getPageUtils().waitForElementToAppear(filterValue).sendKeys(status);
        getPageUtils().waitForElementAndClick(inNegotiationOption);
        return this;
    }

    /**
     * select a due-date to filter
     *
     * @return current page object
     */
    public ProjectsPage selectProjectDueDate(String year, String date) {
        getPageUtils().waitForElementAndClick(filterField);
        getPageUtils().waitForElementAndClick(projectDueDateFilterFiled);
        getPageUtils().waitForElementAndClick(filterConditionType);
        getPageUtils().waitForElementAndClick(isBeforeFilterType);
        getPageUtils().waitForElementAndClick(datePickerFilterValue);
        getPageUtils().waitForElementAndClick(btnYear);
        getPageUtils().waitForElementAndClick(By.xpath("//button[contains(text(),'" + year + "')]"));
        getPageUtils().waitForElementAndClick(By.xpath("//button[contains(text(),'" + date + "')]"));
        return this;
    }

    /**
     * Get filtered project due date
     *
     * @return a String
     */
    public String getFilteredDueDate(String projectName) {
        return getPageUtils().waitForElementToAppear(By.xpath("//h3[contains(text(),'" + projectName + "')]//following::span[contains(@data-testid,'data-label-Due Date')]//following::span")).getText();
    }

    /**
     * Get project status
     *
     * @return a String
     */
    public String getProjectStatus() {
        return getPageUtils().waitForElementToAppear(projectStatus).getText();
    }

    /**
     * clicks on read filter
     *
     * @return current page object
     */
    public ProjectsPage clickOnRead() {
        getPageUtils().waitForElementAndClick(btnRead);
        getPageUtils().waitForElementsToNotAppear(By.xpath("//div[@data-testid='loader']"),5);
        return this;
    }

    /**
     * Navigate and search project
     *
     * @return new page object
     */
    public ProjectsPage navigateAndSearchProject(String createdProjectName) {
        getPageUtils().waitForElementAndClick(btnUnread);
        getPageUtils().waitForElementsToNotAppear(By.xpath("//div[@data-testid='loader']"),5);
        getPageUtils().waitForElementAndClick(btnSearch);
        getPageUtils().waitForElementToAppear(searchField).sendKeys(createdProjectName);
        getPageUtils().waitForElementsToNotAppear(By.xpath("//div[@data-testid='loader']"),5);
        return new ProjectsPage(driver);
    }
}