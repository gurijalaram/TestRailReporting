package main.java.header;

import main.java.utils.PageUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author kpatel
 */

public class GenericHeader extends LoadableComponent<GenericHeader> {

    private static Logger logger = LoggerFactory.getLogger(GenericHeader.class);

    @FindBy(css = "a.dropdown-toggle.text-center span.glyphicon-file")
    private WebElement newFileDropdown;

    @FindBy(css = "button[data-ap-comp='publishScenarioButton']")
    private WebElement publishButton;

    @FindBy(css = "button[data-ap-comp='revertScenarioButton']")
    private WebElement revertButton;

    @FindBy(css = "span.delete-button")
    private WebElement deleteButton;

    @FindBy(css = "span.glyphicons-settings")
    private WebElement actionsDropdown;

    @FindBy(css = "button[data-ap-comp='editScenarioButton']")
    private WebElement editButton;

    @FindBy(css = "button[data-ap-comp='newComponentButton']")
    private WebElement componentButton;

    @FindBy(css = "button[data-ap-comp='saveAsButton']")
    private WebElement scenarioButton;

    @FindBy(css = "button[data-ap-comp='newComparisonButton']")
    private WebElement comparisonButton;

    @FindBy(css = "button[data-ap-comp='toggleLockButton']")
    private WebElement lockButton;

    @FindBy(css = "button[data-ap-comp='reloadButton']")
    private WebElement cadModelButton;

    @FindBy(css = "button[data-ap-comp='assignScenarioButton']")
    private WebElement assignButton;

    @FindBy(css = "button[data-ap-comp='updateAdminInfoButton']")
    private WebElement scenarioNotesButton;

    @FindBy(css = "button[data-ap-comp='partCostReportButton']")
    private WebElement partCostButton;

    @FindBy(css = "button[data-ap-comp='costComparisonReportButton']")
    private WebElement comparisonReportButton;

    private WebDriver driver;
    private PageUtils pageUtils;

    public GenericHeader(WebDriver driver) {
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
        pageUtils.waitForElementToAppear(deleteButton);
    }

    /**
     * Selects new file dropdown
     *
     * @return current page object
     */
    GenericHeader selectNewFileDropdown() {
        pageUtils.waitForElementToAppear(newFileDropdown).click();
        return this;
    }

    /**
     * Selects component button
     *
     * @return current page object
     */
    GenericHeader selectComponentButton() {
        pageUtils.waitForElementToAppear(componentButton).click();
        return this;
    }

    /**
     * Selects scenario button
     *
     * @return current page object
     */
    GenericHeader selectScenarioButton() {
        pageUtils.waitForElementToAppear(scenarioButton).click();
        return this;
    }

    /**
     * Selects comparison button
     *
     * @return current page object
     */
    GenericHeader selectComparisonButton() {
        pageUtils.waitForElementToAppear(comparisonButton).click();
        return this;
    }

    /**
     * Selects actions button
     *
     * @return current page object
     */
    GenericHeader selectActionsButton() {
        pageUtils.waitForElementToAppear(actionsDropdown).click();
        return this;
    }

    /**
     * Selects edit button
     *
     * @return current page object
     */
    GenericHeader selectEditButton() {
        pageUtils.waitForElementToAppear(editButton).click();
        return this;
    }

    /**
     * Selects lock button
     *
     * @return current page object
     */
    GenericHeader selectLockButton() {
        pageUtils.waitForElementToAppear(lockButton).click();
        return this;
    }

    /**
     * Selects assign button
     *
     * @return current page object
     */
    GenericHeader selectAssignButton() {
        pageUtils.waitForElementToAppear(assignButton).click();
        return this;
    }

    /**
     * Selects scenario notes button
     *
     * @return current page object
     */
    GenericHeader selectScenarioNotesButton() {
        pageUtils.waitForElementToAppear(scenarioButton).click();
        return this;
    }

    /**
     * Selects publish button
     *
     * @return current page object
     */
    GenericHeader selectPublishButton() {
        pageUtils.waitForElementToAppear(publishButton).click();
        return this;
    }

    /**
     * Checks delete button is displayed
     *
     * @return visibility of button
     */
    boolean getDeleteButton() {
        return deleteButton.isDisplayed();
    }
}