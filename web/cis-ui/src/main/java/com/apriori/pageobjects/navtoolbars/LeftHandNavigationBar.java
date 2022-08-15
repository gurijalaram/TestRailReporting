package com.apriori.pageobjects.navtoolbars;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.utils.AssemblyUtils;
import com.apriori.cidappapi.utils.ComponentsUtil;
import com.apriori.cidappapi.utils.ScenariosUtil;
import com.apriori.pageobjects.common.LetNavigationBarController;
import com.apriori.pageobjects.pages.messages.MessagesPage;
import com.apriori.pageobjects.pages.myuser.MyUserPage;
import com.apriori.pageobjects.pages.partsandassemblies.PartsAndAssembliesPage;
import com.apriori.utils.enums.DigitalFactoryEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.reader.file.user.UserCredentials;

import lombok.extern.slf4j.Slf4j;
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

    private LetNavigationBarController letNavigationBarController;
    private final ScenariosUtil scenariosUtil = new ScenariosUtil();
    private final ComponentsUtil componentsUtil = new ComponentsUtil();
    private AssemblyUtils assemblyUtils = new AssemblyUtils();

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
        return new PartsAndAssembliesPage(getDriver());
    }

    /**
     * Click the user icon
     *
     * @return current page object
     */

    public MyUserPage clickUserIcon() {
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
     * Upload and cost a component via API
     *
     * @return current page object
     */
    public LeftHandNavigationBar uploadAndCostScenario(String componentName, String scenarioName, File resourceFile, UserCredentials userCredentials,ProcessGroupEnum processGroupEnum,DigitalFactoryEnum digitalFactoryEnum) {
        ComponentInfoBuilder scenarioItem = componentsUtil.postComponentQueryCSS(ComponentInfoBuilder.builder()
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
                        .processGroup(processGroupEnum)
                        .digitalFactory(digitalFactoryEnum)
                        .user(userCredentials)
                        .build());

        return this;
    }

    /**
     * Upload and cost assembly
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
}
