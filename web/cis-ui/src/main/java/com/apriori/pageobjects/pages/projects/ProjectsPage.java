package com.apriori.pageobjects.pages.projects;

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

    @FindBy(id = "create-project-name-input")
    private WebElement projectNameField;

    @FindBy(id = "create-project-description-input")
    private WebElement projectDescriptionField;

    @FindBy(id = "create-project-add-part-and-assembly-btn")
    private WebElement btnAddPartsAndAssemblies;

    @FindBy(xpath = "//span[contains(text(),'Make Selection')]")
    private WebElement inviteTeammatesField;

    @FindBy(xpath = "//div[@data-testid='create-project-due-date']")
    private WebElement dueDateField;

    @FindBy(id = "create-project-submit-btn")
    private WebElement btnProjectSubmit;

    @FindBy(id = "create-project-cancel-btn")
    private WebElement btnCancelProject;

    @FindBy(xpath = "//div[contains(@class,'MuiCalendarPicker')]")
    private WebElement dueDateDatePicker;

    @FindBy(xpath = "//button[@title='Previous month']")
    private WebElement previousMonthSelector;

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
    public ProjectsPage clickOnCreateNewProject() {
        getPageUtils().waitForElementAndClick(btnCreateNewProject);
        return this;
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
    public ProjectsPage clickOnDueDatePicker() {
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
     * @return current page object
     */
    public ProjectsPage clickOnCancelProject() {
        getPageUtils().waitForElementAndClick(btnCancelProject);
        return this;
    }
}