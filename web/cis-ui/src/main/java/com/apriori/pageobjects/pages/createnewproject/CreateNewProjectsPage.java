package com.apriori.pageobjects.pages.createnewproject;

import com.apriori.pageobjects.pages.partsandassembliesdetails.PartsAndAssembliesDetailsPage;
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


    private PageUtils pageUtils;

    public CreateNewProjectsPage(WebDriver driver) {
        this(driver, log);
    }

    private WebDriver driver;

    public CreateNewProjectsPage(WebDriver driver, Logger logger) {
        super(driver, logger);
        this.driver = driver;
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
}