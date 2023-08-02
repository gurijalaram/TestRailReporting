package com.apriori.navtoolbars;

import static com.apriori.entity.enums.CssSearch.COMPONENT_NAME_EQ;
import static com.apriori.entity.enums.CssSearch.SCENARIO_NAME_EQ;
import static com.apriori.entity.enums.CssSearch.SCENARIO_STATE_EQ;
import static com.apriori.enums.ScenarioStateEnum.COST_COMPLETE;
import static com.apriori.enums.ScenarioStateEnum.NOT_COSTED;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.entity.response.CostingTemplate;
import com.apriori.cidappapi.utils.AssemblyUtils;
import com.apriori.cidappapi.utils.ComponentsUtil;
import com.apriori.cidappapi.utils.ScenariosUtil;
import com.apriori.common.LetNavigationBarController;
import com.apriori.enums.DigitalFactoryEnum;
import com.apriori.enums.ProcessGroupEnum;
import com.apriori.pageobjects.messages.MessagesPage;
import com.apriori.pageobjects.myuser.MyUserPage;
import com.apriori.pageobjects.partsandassemblies.PartsAndAssembliesPage;
import com.apriori.pageobjects.projects.ProjectsPage;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.utils.CssComponent;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;

import java.io.File;
import java.util.List;

@Slf4j
public class LeftHandNavigationBar extends CisHeaderBar {

    @FindBy(xpath = "//button[@data-testid='non-collapsed']")
    private WebElement hamburgerIcon;

    @FindBy(xpath = "//div[@data-testid='list-subitem-text-left-menu.subTitle.parts-and-assemblies']")
    private WebElement btnPartsAndAssemblies;

    @FindBy(xpath = "//div[@data-testid='avatar']")
    private WebElement userIcon;

    @FindBy(xpath = "//div[@data-testid='list-subitem-left-menu.subTitle.messages']")
    private WebElement btnMessage;

    @FindBy(xpath = "//img[@data-testid='main-logo']")
    private WebElement aprioriLogo;

    @FindBy(xpath = "//img[@data-testid='collapsed-logo']")
    private WebElement collapsedAprioriLogo;

    @FindBy(xpath = "//div[starts-with(@Class,'MuiDataGrid-cellCheckbox')]")
    private List<WebElement> tableRow;

    @FindBy(xpath = "//div[@data-testid='loader']")
    private WebElement spinner;

    @FindBy(xpath = "//div[@data-testid='list-subitem-text-left-menu.subTitle.projects']")
    private WebElement btnProjects;

    private LetNavigationBarController letNavigationBarController;
    private final ScenariosUtil scenariosUtil = new ScenariosUtil();
    private final ComponentsUtil componentsUtil = new ComponentsUtil();
    private AssemblyUtils assemblyUtils = new AssemblyUtils();
    private CssComponent cssComponent = new CssComponent();

    public LeftHandNavigationBar(WebDriver driver) {
        this(driver, log);
    }

    public LeftHandNavigationBar(WebDriver driver, Logger logger) {
        super(driver, logger);
        this.letNavigationBarController = new LetNavigationBarController(driver);
    }

    @Override
    protected void isLoaded() throws Error {
    }

    /**
     * Gets the navigation panel state
     *
     * @return String
     */
    public String getNavigationPanelDefaultState() {
        return getPageUtils().waitForElementToAppear(hamburgerIcon).getAttribute("data-testid");
    }

    /**
     * Collapse the navigation panel
     *
     * @return current page object
     */

    public LeftHandNavigationBar collapseNavigationPanel() {
        getPageUtils().waitForElementToAppear(hamburgerIcon).click();
        return this;
    }

    /**
     * Navigate to parts and assemblies page
     *
     * @return new page object
     */

    public PartsAndAssembliesPage clickPartsAndAssemblies() {
        getPageUtils().waitForElementToAppear(btnPartsAndAssemblies).click();
        getPageUtils().waitForElementsToAppear(tableRow);
        return new PartsAndAssembliesPage(getDriver());
    }

    /**
     * Click the user icon
     *
     * @return current page object
     */

    public MyUserPage clickUserIcon() {
        getPageUtils().waitForElementToAppear(spinner);
        getPageUtils().waitForElementsToNotAppear(By.xpath("//div[@data-testid='loader']"),5);
        getPageUtils().waitForElementAndClick(userIcon);
        return new MyUserPage(getDriver());
    }

    /**
     * Navigate to dashboard page
     *
     * @return new page object
     */
    public LeftHandNavigationBar clickDashBoard() {
        getPageUtils().waitForElementAndClick(btnMessage);
        return this;
    }

    /**
     * Checks if aPriori logo displayed
     *
     * @return true/false
     */
    public boolean isaPrioriLogoDisplayed() {
        getPageUtils().waitForElementToAppear(aprioriLogo);
        return getPageUtils().isElementDisplayed(aprioriLogo);
    }

    /**
     * Checks if dashboard navbar item displayed
     *
     * @return true/false
     */
    public boolean isDashboardBtnDisplayed() {
        getPageUtils().waitForElementToAppear(btnMessage);
        return getPageUtils().isElementDisplayed(btnMessage);
    }

    /**
     * Get Message state as default selected
     *
     * @return String
     */
    public String getMessageBtnDefaultState() {
        return getPageUtils().waitForElementToAppear(btnMessage).getAttribute("class");
    }

    /**
     * Checks if  collapsed aPriori logo displayed
     *
     * @return true/false
     */
    public boolean isCollapsedAprioriLogoDisplayed() {
        return getPageUtils().isElementDisplayed(collapsedAprioriLogo);
    }

    /**
     * Get Nav bar items on each section
     *
     * @return list of string
     */
    public List<String> getItemsOfSections(String section) {
        return letNavigationBarController.getItemsOfSections(section);
    }

    /**
     * Upload, Cost and Publish a component via API
     *
     * @return current page object
     */
    public LeftHandNavigationBar uploadAndCostScenario(String componentName, String scenarioName, File resourceFile, UserCredentials userCredentials, ProcessGroupEnum processGroupEnum, DigitalFactoryEnum digitalFactoryEnum) {
        ComponentInfoBuilder scenarioItem = componentsUtil.postComponentQueryCSSUncosted(ComponentInfoBuilder.builder()
            .componentName(componentName)
            .scenarioName(scenarioName)
            .resourceFile(resourceFile)
            .user(userCredentials)
            .build());
        scenariosUtil.postCostScenario(
            ComponentInfoBuilder.builder()
                .componentName(componentName)
                .scenarioName(scenarioName)
                .componentIdentity(scenarioItem.getComponentIdentity())
                .scenarioIdentity(scenarioItem.getScenarioIdentity())
                .costingTemplate(CostingTemplate.builder()
                    .processGroupName(processGroupEnum.getProcessGroup())
                    .vpeName(digitalFactoryEnum.getDigitalFactory())
                    .build())
                .user(userCredentials)
                .build());
        cssComponent.getComponentParts(userCredentials, COMPONENT_NAME_EQ.getKey() + componentName, SCENARIO_NAME_EQ.getKey() + scenarioName, SCENARIO_STATE_EQ.getKey() + COST_COMPLETE);
        scenariosUtil.postPublishScenario(scenarioItem);
        return this;
    }

    /**
     * Upload and cost and publish assembly
     *
     * @return current page object
     */
    public LeftHandNavigationBar uploadAndCostAssembly(String assemblyName,
                                                       String assemblyExtension,
                                                       ProcessGroupEnum assemblyProcessGroup,
                                                       List<String> subComponentNames,
                                                       String subComponentExtension,
                                                       ProcessGroupEnum subComponentProcessGroup,
                                                       String scenarioName,
                                                       UserCredentials currentUser) {
        ComponentInfoBuilder componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(assemblyName,
            assemblyExtension,
            assemblyProcessGroup,
            subComponentNames,
            subComponentExtension,
            subComponentProcessGroup,
            scenarioName,
            currentUser);

        assemblyUtils.uploadSubComponents(componentAssembly)
            .uploadAssembly(componentAssembly);

        assemblyUtils.costAssembly(componentAssembly);
        assemblyUtils.publishSubComponents(componentAssembly)
            .publishAssembly(componentAssembly);

        return this;
    }

    /**
     * Checks if Messages navbar item displayed
     *
     * @return true/false
     */
    public boolean isMessagesLinkDisplayed() {
        return getPageUtils().waitForElementAppear(btnMessage).isDisplayed();
    }

    /**
     * Click on Messages link
     *
     * @return new page object
     */
    public MessagesPage clickMessages() {
        getPageUtils().waitForElementAndClick(btnMessage);
        return new MessagesPage(getDriver());
    }

    /**
     * Upload a component and publish via API
     *
     * @return current page object
     */
    public LeftHandNavigationBar uploadComponent(String componentName, String scenarioName, File resourceFile, UserCredentials userCredentials) {
        ComponentInfoBuilder myComponent = componentsUtil.postComponentQueryCSSUncosted(ComponentInfoBuilder.builder()
            .componentName(componentName)
            .scenarioName(scenarioName)
            .resourceFile(resourceFile)
            .user(userCredentials)
            .build());

        cssComponent.getComponentParts(userCredentials, COMPONENT_NAME_EQ.getKey() + componentName, SCENARIO_NAME_EQ.getKey() + scenarioName, SCENARIO_STATE_EQ.getKey() + NOT_COSTED);
        scenariosUtil.postPublishScenario(myComponent);
        return this;
    }

    /**
     * Checks if projects button displayed
     *
     * @return true/false
     */
    public boolean isProjectsBtnDisplayed() {
        return getPageUtils().waitForElementToAppear(btnProjects).isDisplayed();
    }

    /**
     * Navigate to projects page
     *
     * @return new page object
     */

    public ProjectsPage clickProjects() {
        getPageUtils().waitForElementAndClick(btnProjects);
        return new ProjectsPage(getDriver());
    }
}
