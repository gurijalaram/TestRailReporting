package main.java.pages.explore;

import main.java.utils.PageUtils;
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

    private final Logger logger = LoggerFactory.getLogger(ScenarioNotesPage.class);

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

    @FindBy(css = "button.btn.btn-default")
    private WebElement cancelButton;

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
        selectStatus(status);
        selectCostMaturity(costMaturity);
        enterDescription(description);
        enterScenarioNotes(notes);
        return this;
    }

    /**
     * Selects the status
     *
     * @param status - the status
     * @return current page object
     */
    private ScenarioNotesPage selectStatus(String status) {
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
     * Get the status dropdown
     * @return option as string
     */
    public boolean getStatus(String text) {
        return pageUtils.checkElementFirstOption(statusDropdown, text);
    }

    /**
     * Gets the cost maturity dropdown
     * @return option as string
     */
    public boolean getCostMaturity(String text) {
        return pageUtils.checkElementFirstOption(maturityDropdown, text);
    }

    /**
     * Get the description
     * @return the description as string
     */
    public String getDescription() {
        return pageUtils.checkElementAttribute(descriptionInput, "value");
    }

    /**
     * Gets the scenario notes
     * @return the scenario notes as string
     */
    public String getScenarioNotes() {
        return pageUtils.checkElementAttribute(scenarioNotesInput, "value");
    }

    /**
     * Removes the description
     * @return current page object
     */
    public ScenarioNotesPage deleteDescription() {
        descriptionInput.clear();
        return this;
    }

    /**
     * Removes the notes
     * @return current page object
     */
    public ScenarioNotesPage deleteNotes() {
        scenarioNotesInput.clear();
        return this;
    }

    /**
     * Selects the apply button
     *
     * @return new page object
     */
    public ExplorePage save() {
        pageUtils.waitForElementAndClick(saveButton);
        return new ExplorePage(driver);
    }

    /**
     * Selects the cancel button
     *
     * @return new page object
     */
    public ExplorePage cancel() {
        cancelButton.click();
        return new ExplorePage(driver);
    }
}