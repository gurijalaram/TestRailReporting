package com.apriori.pageobjects.pages.login;

import static org.junit.Assert.assertTrue;

import com.apriori.cisapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cisapi.utils.AssemblyUtils;
import com.apriori.cisapi.utils.ComponentsUtil;
import com.apriori.cisapi.utils.ScenariosUtil;
import com.apriori.pageobjects.navtoolbars.LeftHandNavigationBar;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.utils.CssComponent;
import com.apriori.utils.PageUtils;
import com.apriori.utils.enums.DigitalFactoryEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.login.AprioriLoginPage;
import com.apriori.utils.reader.file.user.UserCredentials;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

import java.io.File;
import java.util.List;

@Slf4j
public class CisLoginPage extends LoadableComponent<CisLoginPage> {

    @FindBy(css = ".auth0-lock-header-logo")
    private WebElement aprioriLogo;

    private WebDriver driver;
    private PageUtils pageUtils;
    private AprioriLoginPage aprioriLoginPage;
    private final ScenariosUtil scenariosUtil = new ScenariosUtil();
    private final ComponentsUtil componentsUtil = new ComponentsUtil();
    private AssemblyUtils assemblyUtils = new AssemblyUtils();
    private CssComponent cssComponent = new CssComponent();

    public CisLoginPage(WebDriver driver) {
        this.driver = driver;
        pageUtils = new PageUtils(driver);
        this.aprioriLoginPage = new AprioriLoginPage(driver, "cis");
        log.debug(pageUtils.currentlyOnPage(this.getClass().getSimpleName()));
        PageFactory.initElements(driver, this);
        this.get();
    }

    @Override
    protected void load() {

    }

    @Override
    protected void isLoaded() throws Error {
        assertTrue("CIS login page was not displayed", aprioriLoginPage.getPageTitle().contains("Cost Insight Source"));
    }

    /**
     * Login to CIS
     *
     * @param userCredentials - object with users credentials and access level
     * @return new page object
     */
    public ExplorePage login(UserCredentials userCredentials) {
        return aprioriLoginPage.login(userCredentials, ExplorePage.class);
    }

    /**
     * Login to CIS New Application
     *
     * @param userCredentials - object with users credentials and access level
     * @return new page object
     */
    public LeftHandNavigationBar cisLogin(UserCredentials userCredentials) {
        return aprioriLoginPage.login(userCredentials, LeftHandNavigationBar.class);
    }

    /**
     * Checks if apriori logo displayed
     *
     * @return true/false
     */
    public boolean isLogoDisplayed() {
        return pageUtils.waitForElementToAppear(aprioriLogo).isDisplayed();
    }

    /**
     * Upload, Cost and Publish a component via API
     *
     * @return current page object
     */
    public CisLoginPage uploadAndCostScenario(String componentName, String scenarioName, File resourceFile, UserCredentials userCredentials, ProcessGroupEnum processGroupEnum, DigitalFactoryEnum digitalFactoryEnum) {
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
        cssComponent.getCssComponentQueryParams(componentName, scenarioName, userCredentials, "scenarioState, COST_COMPLETE");
        scenariosUtil.postPublishScenario(scenarioItem);
        return this;
    }

    /**
     * Upload a component via API
     *
     * @return current page object
     */
    public CisLoginPage uploadComponent(String componentName, String scenarioName, File resourceFile, UserCredentials userCredentials) {
        ComponentInfoBuilder myComponent = componentsUtil.postComponentQueryCSS(ComponentInfoBuilder.builder()
                .componentName(componentName)
                .scenarioName(scenarioName)
                .resourceFile(resourceFile)
                .user(userCredentials)
                .build());

        cssComponent.getCssComponentQueryParams(componentName, scenarioName, userCredentials, "scenarioState, NOT_COSTED");
        scenariosUtil.postPublishScenario(myComponent);
        return this;
    }

    /**
     * Upload and cost assembly
     *
     * @return current page object
     */
    public CisLoginPage uploadAndCostAssembly(String assemblyName,
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
}