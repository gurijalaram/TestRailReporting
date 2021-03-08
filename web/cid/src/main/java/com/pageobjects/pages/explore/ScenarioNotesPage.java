package com.pageobjects.pages.explore;

import com.apriori.utils.PageUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author cfrith
 */

public class ScenarioNotesPage extends LoadableComponent<ScenarioNotesPage> {

    private static final Logger logger = LoggerFactory.getLogger(ScenarioNotesPage.class);

    @FindBy(css = "[data-ap-scope='scenarioSelection'] .modal-content")
    private WebElement modalDialog;

    @FindBy(css = "select[data-ap-field='status']")
    private WebElement statusDropdown;

    @FindBy(css = "select[data-ap-field='costMaturity']")
    private WebElement maturityDropdown;

    @FindBy(css = "textarea[data-ap-field='description']")
    private WebElement descriptionInput;

    @FindBy(css = "textarea[data-ap-field='comments']")
    private WebElement scenarioNotesInput;

    @FindBy(css = "button.btn.btn-primary")
    private WebElement saveButton;

    @FindBy(css = "[class='gwt-Button btn btn-default']")
    private WebElement cancelButton;

    @FindBy(css = "[data-ap-field='assignee']")
    private WebElement assigneeField;

    private WebDriver driver;
    private PageUtils pageUtils;

    public ScenarioNotesPage(WebDriver driver) {
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        pageUtils.waitForElementToAppear(modalDialog);
    }

    /**
     * Collective method to enter scenario information and notes
     *
     * @param status       - the status
     * @param costMaturity - the cost maturity
     * @param description  - the description
     * @param notes        - the notes
     * @return current page object
     */
    public ScenarioNotesPage enterScenarioInfoNotes(String status, String costMaturity, String description, String notes) {
        enterScenarioInfoNotesForComparison(status, costMaturity, description);
        enterScenarioNotes(notes);
        return this;
    }

    /**
     * Enter scenario information and notes in comparison view
     *
     * @param status - the status
     * @param costMaturity - the cost maturity
     * @param description - the description
     * @return current page object
     */
    public ScenarioNotesPage enterScenarioInfoNotesForComparison(String status, String costMaturity, String description) {
        selectStatus(status);
        selectCostMaturity(costMaturity);
        enterDescription(description);
        return this;
    }

    /**
     * Selects the status
     *
     * @param status - the status
     * @return current page object
     */
    private ScenarioNotesPage selectStatus(String status) {
        pageUtils.checkDropdownOptions(statusDropdown, status);
        new Select(statusDropdown).selectByVisibleText(status);
        return this;
    }

    /**
     * Selects cost maturity
     *
     * @param costMaturity - the cost maturity
     * @return current page object
     */
    private ScenarioNotesPage selectCostMaturity(String costMaturity) {
        pageUtils.checkDropdownOptions(maturityDropdown, costMaturity);
        new Select(maturityDropdown).selectByVisibleText(costMaturity);
        return this;
    }

    /**
     * Enter the description
     *
     * @param description - the description
     * @return current page object
     */
    private ScenarioNotesPage enterDescription(String description) {
        pageUtils.clearInput(descriptionInput);
        descriptionInput.sendKeys(description);
        return this;
    }

    /**
     * Enter the scenario notes
     *
     * @param notes - the scenario notes
     * @return current page object
     */
    private ScenarioNotesPage enterScenarioNotes(String notes) {
        pageUtils.clearInput(scenarioNotesInput);
        scenarioNotesInput.sendKeys(notes);
        return this;
    }

    /**
     * Checks the status dropdown
     *
     * @return option as string
     */
    public boolean isStatusSelected(String text) {
        return pageUtils.checkElementFirstOption(statusDropdown, text);
    }

    /**
     * Checks the cost maturity dropdown
     *
     * @return option as string
     */
    public boolean isCostMaturitySelected(String text) {
        return pageUtils.checkElementFirstOption(maturityDropdown, text);
    }

    /**
     * Gets the description
     *
     * @return the description as string
     */
    public String getDescription() {
        By description = By.cssSelector("textarea[data-ap-field='description']");
        pageUtils.waitForElementToAppear(description);
        return driver.findElement(description).getAttribute("value");
    }

    /**
     * Gets the scenario notes
     *
     * @return the scenario notes as string
     */
    public String getScenarioNotes() {
        By scenarioNotes = By.cssSelector("textarea[data-ap-field='comments']");
        pageUtils.waitForElementToAppear(scenarioNotes);
        return driver.findElement(scenarioNotes).getAttribute("value");
    }

    /**
     * Removes the description
     *
     * @return current page object
     */
    public ScenarioNotesPage deleteDescription() {
        descriptionInput.clear();
        return this;
    }

    /**
     * Removes the notes
     *
     * @return current page object
     */
    public ScenarioNotesPage deleteNotes() {
        scenarioNotesInput.clear();
        return this;
    }

    /**
     * Selects the save button
     *
     * @param className - the class the method should return
     * @param <T>       - the return type
     * @return generic page object
     */
    public <T> T save(Class<T> className) {
        pageUtils.waitForElementAndClick(saveButton);
        return PageFactory.initElements(driver, className);
    }

    /**
     * Selects the cancel button
     *
     * @return new page object
     */
    public <T> T cancel(Class<T> className) {
        pageUtils.waitForElementAndClick(cancelButton);
        return PageFactory.initElements(driver, className);
    }

    /**
     * Gets the assignee
     *
     * @return the assignee as string
     */
    public String isAssignee() {
        By assignee = By.cssSelector("[data-ap-field='assignee']");
        pageUtils.waitForElementToAppear(assignee);
        return driver.findElement(assignee).getAttribute("innerText");
    }

    /**
     * Edits the scenario notes
     *
     * @param notes - the scenario notes
     * @return current page object
     */
    public ScenarioNotesPage editNotes(String notes) {
        scenarioNotesInput.sendKeys(Keys.CONTROL + "a");
        scenarioNotesInput.sendKeys(Keys.DELETE);
        scenarioNotesInput.sendKeys(notes);
        return this;
    }
}