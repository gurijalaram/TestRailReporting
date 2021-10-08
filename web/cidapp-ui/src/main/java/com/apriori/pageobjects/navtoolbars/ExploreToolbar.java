package com.apriori.pageobjects.navtoolbars;

import com.apriori.cidappapi.utils.CidAppTestUtil;
import com.apriori.css.entity.response.Item;
import com.apriori.pageobjects.pages.compare.ComparePage;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.explore.FileUploadPage;
import com.apriori.utils.PageUtils;
import com.apriori.utils.properties.PropertiesContext;
import com.apriori.utils.users.UserCredentials;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.io.File;

/**
 * @author cfrith
 */

@Slf4j
public class ExploreToolbar extends MainNavBar {

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

    @FindBy(id = "qa-action-bar-action-update-cad-file")
    private WebElement cadFileButton;

    private PageUtils pageUtils;
    private WebDriver driver;

    public ExploreToolbar(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.pageUtils = new PageUtils(driver);
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        pageUtils.waitForElementToAppear(newButton);
        pageUtils.waitForElementToAppear(deleteButton);
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
     *
     * @param componentName - the component name
     * @param scenarioName  - the scenario name
     * @param resourceFile  - the file
     * @return new page object
     */
    public EvaluatePage uploadComponentAndOpen(String componentName, String scenarioName, File resourceFile, UserCredentials userCredentials) {
        Item component = new CidAppTestUtil().postCssComponents(componentName, scenarioName, resourceFile, userCredentials);
        return navigateToScenario(component);
    }

    /**
     * Uploads a component through the API
     *
     * @param componentName   - the component name
     * @param scenarioName    - the scenario name
     * @param resourceFile    - the file
     * @param userCredentials - the user credentials
     * @return response object
     */
    public Item uploadComponent(String componentName, String scenarioName, File resourceFile, UserCredentials userCredentials) {
        return new CidAppTestUtil().postCssComponents(componentName, scenarioName, resourceFile, userCredentials);
    }


    /**
     * Navigates to the scenario via url
     *
     * @param cssComponent - the CSS Component
     * @return a new page object
     */
    public EvaluatePage navigateToScenario(Item cssComponent) {
        driver.navigate().to(PropertiesContext.get("${env}.cidapp.ui_url").concat(String.format("components/%s/scenarios/%s", cssComponent.getComponentIdentity(), cssComponent.getScenarioIdentity())));
        return new EvaluatePage(driver);
    }

    /**
     * Navigates to the scenario via url
     *
     * @param scenarioUrl - url for the scenario
     * @return new page object
     */
    public EvaluatePage navigateToScenario(String scenarioUrl) {
        driver.navigate().to(scenarioUrl);
        return new EvaluatePage(driver);
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

    /**
     * Uploads a cad file and select submit
     *
     * @param filePath - location of the file
     * @param klass-   the class name
     * @param <T>      - generic type
     * @return generic page object
     */
    public <T> T updateCadFile(File filePath, Class<T> klass) {
        pageUtils.waitForElementAndClick(actionsButton);
        pageUtils.waitForElementAndClick(cadFileButton);
        return new FileUploadPage(driver).enterFilePath(filePath).submit(klass);
    }
}
