package com.apriori.pageobjects.navtoolbars;

import com.apriori.cidapp.entity.response.css.Item;
import com.apriori.utils.CidAppTestUtil;
import com.apriori.pageobjects.pages.compare.ComparePage;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.explore.FileUploadPage;
import com.apriori.utils.PageUtils;
import com.apriori.utils.users.UserCredentials;

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

    @FindBy(css = "[id='qa-action-bar-actions-dropdown']")
    private WebElement actionsButton;

    @FindBy(css = "[id='qa-sub-header-edit-button']")
    private WebElement editButton;

    @FindBy(css = "[id='qa-sub-header-new-scenario']")
    private WebElement scenarioButton;

    @FindBy(id = "qa-action-bar-action-info")
    private WebElement infoButton;

    @FindBy(id = "qa-sub-header-action-lock")
    private WebElement lockButton;

    @FindBy(id = "qa-sub-header-action-unlock")
    private WebElement unlockButton;

    @FindBy(id = "qa-sub-header-new-comparison")
    private WebElement comparisonButton;

    @FindBy(id = "qa-action-bar-action-assign")
    private WebElement assignButton;

    private PageUtils pageUtils;
    private WebDriver driver;

    public ExploreToolbar(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        logger.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        pageUtils.waitForElementAppear(newButton);
        pageUtils.waitForElementAppear(deleteButton);
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
     * Uploads a component through the API and opens it via url
     * @param componentName - the component name
     * @param scenarioName - the scenario name
     * @param resourceFile - the file
     * @return new page object
     */
    public EvaluatePage uploadComponentAndOpen(String componentName, String scenarioName, File resourceFile, UserCredentials userCredentials) {
        Item component = new CidAppTestUtil().postComponents(componentName, scenarioName, resourceFile, userCredentials);
        return new ExplorePage(driver).navigateToScenario(component.getComponentIdentity(), component.getScenarioIdentity());
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
     * Opens the scenario
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
     * Opens the scenario page
     *
     * @return new page object
     */
    public ScenarioPage createScenario() {
        pageUtils.waitForElementAndClick(newButton);
        pageUtils.waitForElementAndClick(scenarioButton);
        return new ScenarioPage(driver);
    }

    /**
     * Opens scenario info page
     *
     * @return new page object
     */
    public InfoPage info() {
        pageUtils.waitForElementAndClick(actionsButton);
        pageUtils.waitForElementAndClick(infoButton);
        return new InfoPage(driver);
    }

    /**
     * Lock the scenario
     *
     * @return generic page object
     */
    public <T> T lock(Class<T> klass) {
        pageUtils.waitForElementAndClick(actionsButton);
        pageUtils.waitForElementAndClick(lockButton);
        return PageFactory.initElements(driver, klass);
    }

    /**
     * Unlock the scenario
     *
     * @return generic page object
     */
    public <T> T unlock(Class<T> klass) {
        pageUtils.waitForElementAndClick(actionsButton);
        pageUtils.waitForElementAndClick(unlockButton);
        return PageFactory.initElements(driver, klass);
    }

    /**
     * Opens the scenario page
     *
     * @return new page object
     */
    public DeletePage delete() {
        pageUtils.waitForElementAndClick(deleteButton);
        return new DeletePage(driver);
    }

    /**
     * Opens the assignee page
     *
     * @return new page object
     */
    public AssignPage assign() {
        pageUtils.waitForElementAndClick(actionsButton);
        pageUtils.waitForElementAndClick(assignButton);
        return new AssignPage(driver);
    }

    /**
     * Opens the comparison page
     *
     * @return new page object
     */
    public ComparePage createComparison() {
        pageUtils.waitForElementAndClick(newButton);
        pageUtils.waitForElementAndClick(comparisonButton);
        return new ComparePage(driver);
    }
}
