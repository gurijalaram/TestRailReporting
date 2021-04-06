package com.apriori.pageobjects.navtoolbars;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.explore.FileUploadPage;
import com.apriori.utils.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * @author cfrith
 */

public class ExploreToolbar extends MainNavBar {

    private static final Logger logger = LoggerFactory.getLogger(ExploreToolbar.class);

    @FindBy(css = "[id='qa-sub-header-new-dropdown']")
    private WebElement newButton;

    @FindBy(css = "[id='qa-sub-header-new-component']")
    private WebElement componentButton;

    @FindBy(css = "[id='qa-sub-header-publish-button']")
    private WebElement publishButton;

    @FindBy(css = "[id='qa-sub-header-revert-button']")
    private WebElement revertButton;

    @FindBy(css = "[id='qa-sub-header-delete-button']")
    private WebElement deleteButton;

    @FindBy(css = "[id='qa-sub-header-actions-dropdown']")
    private WebElement actionsButton;

    @FindBy(css = "[id='qa-sub-header-edit-button']")
    private WebElement editButton;

    @FindBy(css = "[id='qa-sub-header-new-scenario']")
    private WebElement scenarioButton;

    @FindBy(css = "[role='menuitem'] svg[data-icon='info-circle']")
    private WebElement infoButton;

    @FindBy(id = "qa-sub-header-action-lock")
    private WebElement lockButton;

    @FindBy(id = "qa-sub-header-action-unlock")
    private WebElement unlockButton;

    private PageUtils pageUtils;
    private WebDriver driver;

    public ExploreToolbar(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        pageUtils.waitForElementAppear(newButton);
        pageUtils.waitForElementAppear(publishButton);
    }

    /**
     * Checks delete button is displayed
     *
     * @return visibility of button
     */
    public boolean isDeleteButtonPresent() {
        return deleteButton.isDisplayed();
    }

    /**
     * Collective method to upload a file then select Submit
     *
     * @param scenarioName - the name of the scenario
     * @param filePath     - location of the file
     * @param klass        - the class name
     * @return new page object
     */
    public <T> T uploadComponentAndSubmit(String scenarioName, File filePath, Class<T> klass) {
        return uploadComponent(scenarioName, filePath).submit(klass);
    }

    /**
     * Collective method to upload a file then select Cancel
     *
     * @param scenarioName - the name of the scenario
     * @param filePath     - location of the file
     * @param className    - the class name
     * @return new page object
     */
    public <T> T uploadComponentAndCancel(String scenarioName, File filePath, Class<T> className) {
        return uploadComponent(scenarioName, filePath).cancel(className);
    }

    /**
     * Selects the file dropdown and enters file details
     *
     * @param scenarioName - the name of the scenario
     * @param filePath     - location of the file
     * @return new page object
     */
    public FileUploadPage uploadComponent(String scenarioName, File filePath) {
        pageUtils.waitForElementAndClick(newButton);
        pageUtils.waitForElementAndClick(componentButton);
        return new FileUploadPage(driver).inputComponentDetails(scenarioName, filePath);
    }

    /**
     * Publish the scenario
     *
     * @return new page object
     */
    public PublishPage publishScenario() {
        pageUtils.waitForElementAndClick(publishButton);
        return new PublishPage(driver);
    }

    /**
     * Edit the scenario
     *
     * @return new page object
     */
    public EvaluatePage editScenario() {
        pageUtils.waitForElementAndClick(editButton);
        return new EvaluatePage(driver);
    }

    /**
     * Create new scenario
     *
     * @return new page object
     */
    public ScenarioPage createScenario() {
        pageUtils.waitForElementAndClick(newButton);
        pageUtils.waitForElementAndClick(scenarioButton);
        return new ScenarioPage(driver);
    }

    /**
     * Add scenario info
     *
     * @return new page object
     */
    public InfoPage addScenarioNotes() {
        pageUtils.waitForElementAndClick(actionsButton);
        pageUtils.waitForElementAndClick(infoButton);
        return new InfoPage(driver);
    }

    /**
     * Lock the scenario
     *
     * @param klass - the class
     * @param <T>   - the return type
     * @return generic page object
     */
    public <T> T lockScenario(Class<T> klass) {
        pageUtils.waitForElementAndClick(actionsButton);
        pageUtils.waitForElementAndClick(lockButton);
        return PageFactory.initElements(driver, klass);
    }

    /**
     * Unlock the scenario
     *
     * @param klass - the class
     * @param <T>   - the return type
     * @return generic page object
     */
    public <T> T unlockScenario(Class<T> klass) {
        pageUtils.waitForElementAndClick(actionsButton);
        pageUtils.waitForElementAndClick(unlockButton);
        return PageFactory.initElements(driver, klass);
    }
}
