package com.apriori.pageobjects.navtoolbars;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.utils.AssemblyUtils;
import com.apriori.cidappapi.utils.ComponentsUtil;
import com.apriori.css.entity.response.ScenarioItem;
import com.apriori.pageobjects.pages.compare.ComparePage;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.UpdateCadFilePage;
import com.apriori.pageobjects.pages.explore.EditScenarioStatusPage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.explore.ImportCadFilePage;
import com.apriori.utils.PageUtils;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.properties.PropertiesContext;
import com.apriori.utils.reader.file.user.UserCredentials;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.io.File;
import java.util.List;

/**
 * @author cfrith
 */

@Slf4j
public class ExploreToolbar extends MainNavBar {

    @FindBy(css = "[id='qa-sub-header-new-dropdown']")
    private WebElement newButton;

    @FindBy(css = "[id='qa-sub-header-new-component']")
    private WebElement componentButton;

    @FindBy(css = "[id='qa-sub-header-import-dropdown']")
    private WebElement importButton;

    @FindBy(css = "[id='qa-sub-header-import-component']")
    private WebElement cadButton;

    @FindBy(css = "[id='qa-sub-header-publish-button'] button")
    private WebElement publishButton;

    @FindBy(css = "[id='qa-sub-header-revert-button']")
    private WebElement revertButton;

    @FindBy(css = "[id='qa-sub-header-delete-button'] button")
    private WebElement deleteButton;

    @FindBy(css = "[id='qa-action-bar-actions-dropdown'] .btn-secondary")
    private WebElement actionsButton;

    @FindBy(css = "[id='qa-sub-header-edit-button'] button")
    private WebElement editButton;

    @FindBy(css = "[id='qa-sub-header-new-scenario']")
    private WebElement scenarioButton;

    @FindBy(css = "[id='qa-sub-header-refresh-view-button'] button")
    private WebElement refreshButton;

    @FindBy(css = "[id='qa-action-bar-action-info']")
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
     * Uploads a component through the API and opens it via url
     *
     * @param componentName - the component name
     * @param scenarioName  - the scenario name
     * @param resourceFile  - the file
     * @return new page object
     */
    public EvaluatePage uploadComponentAndOpen(String componentName, String scenarioName, File resourceFile, UserCredentials userCredentials) {
        ComponentInfoBuilder component = new ComponentsUtil().postComponentQueryCSS(
            ComponentInfoBuilder.builder()
                .componentName(componentName)
                .scenarioName(scenarioName)
                .resourceFile(resourceFile)
                .user(userCredentials)
                .build());
        return navigateToScenario(component);
    }

    /**
     * uploads an assembly with all subcomponents and publish them all
     *
     * @param subComponentNames  - the subcomponent names
     * @param componentExtension - the subcomponent extension
     * @param processGroupEnum   - the process group enum
     * @param assemblyName       - the assembly name
     * @param assemblyExtension  -  the assembly extension
     * @param scenarioName       - the scenario name
     * @param currentUser        - the current user
     * @return - a new page object
     */
    public EvaluatePage uploadPublishAndOpenAssembly(List<String> subComponentNames,
                                                     String componentExtension,
                                                     ProcessGroupEnum processGroupEnum,
                                                     String assemblyName,
                                                     String assemblyExtension,
                                                     String scenarioName,
                                                     UserCredentials currentUser) {

        ComponentInfoBuilder myAssembly = new AssemblyUtils().uploadAndPublishAssembly(
            subComponentNames,
            componentExtension,
            processGroupEnum,
            assemblyName,
            assemblyExtension,
            scenarioName,
            currentUser);

        return navigateToScenario(myAssembly);
    }

    /**
     * uploads an assembly with all subcomponents, cost and publish them all
     *
     * @param assemblyName             - the assembly name
     * @param assemblyExtension        - the assembly extension
     * @param assemblyProcessGroup     - the assembly process group
     * @param subComponentNames        - the subcomponent names
     * @param subComponentExtension    - the subcomponent extension
     * @param subComponentProcessGroup - the subcomponent process group
     * @param scenarioName             - the scenario name
     * @param mode                     - the mode
     * @param material                 - the material
     * @param currentUser              - the current user
     * @return - a new page object
     */
    public EvaluatePage uploadCostPublishAndOpenAssembly(String assemblyName,
                                                         String assemblyExtension,
                                                         ProcessGroupEnum assemblyProcessGroup,
                                                         List<String> subComponentNames,
                                                         String subComponentExtension,
                                                         ProcessGroupEnum subComponentProcessGroup,
                                                         String scenarioName,
                                                         String mode,
                                                         String material,
                                                         UserCredentials currentUser) {

        ComponentInfoBuilder myAssembly = new AssemblyUtils().uploadCostPublishScenario(
            assemblyName,
            assemblyExtension,
            assemblyProcessGroup,
            subComponentNames,
            subComponentExtension,
            subComponentProcessGroup,
            scenarioName,
            currentUser);

        return navigateToScenario(myAssembly);
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
    public ComponentInfoBuilder uploadComponent(String componentName, String scenarioName, File resourceFile, UserCredentials userCredentials) {
        return new ComponentsUtil().postComponentQueryCSS(ComponentInfoBuilder.builder()
            .componentName(componentName)
            .scenarioName(scenarioName)
            .resourceFile(resourceFile)
            .user(userCredentials)
            .build());
    }

    public ComponentInfoBuilder uploadMultiComponents(List<File> resourceFiles, UserCredentials currentUser) {
        return new ComponentsUtil().postMultiComponentsQueryCss(ComponentInfoBuilder.builder()
            .resourceFiles(resourceFiles)
            .user(currentUser)
            .build());
    }

    /**
     * Selects the file dropdown and enters file details
     *
     * @return new page object
     */
    public ImportCadFilePage importCadFile() {
        pageUtils.waitForElementAndClick(importButton);
        pageUtils.waitForElementAndClick(cadButton);
        return new ImportCadFilePage(driver);
    }

    /**
     * Navigates to the scenario via url
     *
     * @param cssComponent - the CSS Component
     * @return a new page object
     */
    public EvaluatePage navigateToScenario(ScenarioItem cssComponent) {
        driver.navigate().to(PropertiesContext.get("${env}.cidapp.ui_url").concat(String.format("components/%s/scenarios/%s", cssComponent.getComponentIdentity(), cssComponent.getScenarioIdentity())));
        return new EvaluatePage(driver);
    }

    /**
     * Navigates to the scenario via url
     *
     * @param component - the component
     * @return - A new page object
     */
    public EvaluatePage navigateToScenario(ComponentInfoBuilder component) {
        driver.navigate().to(PropertiesContext.get("${env}.cidapp.ui_url").concat(String.format("components/%s/scenarios/%s", component.getComponentIdentity(), component.getScenarioIdentity())));
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
        return importCadFile()
            .inputComponentDetails(scenarioName, filePath)
            .cancel(className);
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
     * Clicks on the publish button to open PublishScenario page with Unpublished Scenario(s)
     *
     * @return new page object
     */
    public PublishScenarioPage publishPrivateScenario() {
        pageUtils.waitForElementAndClick(publishButton);
        return new PublishScenarioPage(driver);
    }

    /**
     * Edit the scenario
     *
     * @return new page object
     */
    public EditScenarioStatusPage editScenario() {
        pageUtils.waitForElementAndClick(editButton);
        return new EditScenarioStatusPage(driver);
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
     * @return new page object
     */
    public EvaluatePage updateCadFile(File filePath) {
        pageUtils.waitForElementAndClick(actionsButton);
        pageUtils.waitForElementAndClick(cadFileButton);
        return new UpdateCadFilePage(driver).enterFilePath(filePath).submit(EvaluatePage.class);
    }

    /**
     * Clicks on the Refresh button
     *
     * @return new page object
     */
    public ExplorePage refresh() {
        pageUtils.waitForElementAndClick(refreshButton);
        return new ExplorePage(driver);
    }
}
