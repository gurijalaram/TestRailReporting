package com.evaluate.assemblies;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.utils.AssemblyUtils;
import com.apriori.cidappapi.utils.ScenariosUtil;
import com.apriori.cidappapi.utils.UserPreferencesUtil;
import com.apriori.pageobjects.navtoolbars.PublishPage;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.components.ComponentsTreePage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.PreferencesEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.enums.StatusIconEnum;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import com.utils.ColumnsEnum;
import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.ExtendedRegression;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrivatePublicAssemblyAssociationsTests extends TestBase {

    private AssemblyUtils assemblyUtils = new AssemblyUtils();
    private UserPreferencesUtil userPreferencesUtil = new UserPreferencesUtil();
    private ScenariosUtil scenariosUtil = new ScenariosUtil();
    private CidAppLoginPage loginPage;
    private EvaluatePage evaluatePage;
    private ComponentsTreePage componentsTreePage;
    private UserCredentials currentUser;
    private SoftAssertions softAssertions = new SoftAssertions();
    private ComponentInfoBuilder componentAssembly;
    private ComponentInfoBuilder cidComponentItemA;
    private ComponentInfoBuilder cidComponentItemB;
    private ComponentInfoBuilder cidComponentItemC;
    private ComponentInfoBuilder cidComponentItemD;
    private ComponentInfoBuilder cidComponentItemE;
    private ComponentInfoBuilder cidComponentItemF;
    private ComponentInfoBuilder cidComponentItemG;
    private List<ComponentInfoBuilder> listOfSubcomponentsA;
    private List<ComponentInfoBuilder> listOfSubcomponentsB;

    private String bigRing;
    private String pin;
    private String smallRing;
    private String hingeAssembly;
    private ProcessGroupEnum assemblyProcessGroup;
    private ProcessGroupEnum subComponentProcessGroup;
    private String assemblyExtension;
    private String subComponentExtension;
    private String scenarioName;
    private String newScenarioName;
    private String newScenarioName2;
    private String newScenarioName3;
    private String newScenarioName4;
    private List<String> subComponentNames;
    private File resourceFile;
    private File resourceFile2;

    @Before
    public void testSetup() {
        hingeAssembly = "auto hinge assembly";
        assemblyProcessGroup = ProcessGroupEnum.ASSEMBLY;
        assemblyExtension = ".SLDASM";
        bigRing = "auto big ring";
        pin = "auto pin";
        smallRing = "auto small ring";
        subComponentExtension = ".SLDPRT";
        subComponentNames = Arrays.asList(bigRing, pin, smallRing);
        subComponentProcessGroup = ProcessGroupEnum.FORGING;
        resourceFile = FileResourceUtil.getCloudFile(assemblyProcessGroup, hingeAssembly + assemblyExtension);
        resourceFile2 = FileResourceUtil.getCloudFile(subComponentProcessGroup, bigRing + subComponentExtension);

        currentUser = UserUtil.getUser();

        scenarioName = new GenerateStringUtil().generateScenarioName();
        newScenarioName = new GenerateStringUtil().generateScenarioName();
        newScenarioName2 = new GenerateStringUtil().generateScenarioName();
        newScenarioName3 = new GenerateStringUtil().generateScenarioName();
        newScenarioName4 = new GenerateStringUtil().generateScenarioName();

        componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(
            hingeAssembly,
            assemblyExtension,
            assemblyProcessGroup,
            subComponentNames,
            subComponentExtension,
            subComponentProcessGroup,
            scenarioName,
            currentUser);

        assemblyUtils.uploadSubComponents(componentAssembly).uploadAssembly(componentAssembly);
    }

    @After
    public void deleteScenarios() {
        if (currentUser != null) {
            userPreferencesUtil.resetSettings(currentUser);
        }

        listOfSubcomponentsA = Arrays.asList(cidComponentItemA, cidComponentItemC, cidComponentItemE,
            cidComponentItemG);

        listOfSubcomponentsA.forEach(subcomponent -> {
            if (subcomponent != null) {
                scenariosUtil.deleteScenario(subcomponent.getComponentIdentity(), subcomponent.getScenarioIdentity(), currentUser);
            }
        });

        listOfSubcomponentsB = Arrays.asList(cidComponentItemB, cidComponentItemD, cidComponentItemF);

        listOfSubcomponentsB.forEach(subcomponent -> {
            if (subcomponent != null) {
                scenariosUtil.deleteScenario(subcomponent.getComponentIdentity(), subcomponent.getScenarioIdentity(), currentUser);
            }
        });

        assemblyUtils.deleteAssemblyAndComponents(componentAssembly);
    }

    @Test
    @Category(ExtendedRegression.class)
    @TestRail(testCaseId = {"21707", "21708", "21709", "21710", "11955", "11958", "6600"})
    @Description("Validate assembly association priority for Default strategy")
    public void testDefaultAssemblyAssociationsWorkflow() {

        assemblyUtils.publishSubComponents(componentAssembly);

        loginPage = new CidAppLoginPage(driver);

        cidComponentItemA = loginPage.login(currentUser).uploadComponent(hingeAssembly, newScenarioName, resourceFile, currentUser);
        componentsTreePage = new EvaluatePage(driver).navigateToScenario(componentAssembly)
            .openComponents()
            .addColumn(ColumnsEnum.PUBLISHED);

        softAssertions.assertThat(componentsTreePage.getRowDetails(bigRing, scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(pin, scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(smallRing, scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());

        cidComponentItemB = new EvaluatePage(driver).uploadComponent(bigRing, newScenarioName2, resourceFile2, currentUser);
        evaluatePage = new EvaluatePage(driver).navigateToScenario(cidComponentItemB)
            .publishScenario(PublishPage.class)
            .publish(EvaluatePage.class);

        cidComponentItemC = new EvaluatePage(driver).uploadComponent(hingeAssembly, newScenarioName2, resourceFile, currentUser);
        componentsTreePage = new EvaluatePage(driver).navigateToScenario(cidComponentItemC)
            .openComponents();

        softAssertions.assertThat(componentsTreePage.getRowDetails(bigRing, newScenarioName2)).contains(StatusIconEnum.PUBLIC.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(pin, scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(smallRing, scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());

        cidComponentItemD = new EvaluatePage(driver).uploadComponent(bigRing, newScenarioName3, resourceFile2, currentUser);

        cidComponentItemE = new EvaluatePage(driver).uploadComponent(hingeAssembly, newScenarioName4, resourceFile, currentUser);
        componentsTreePage = new EvaluatePage(driver).navigateToScenario(cidComponentItemE)
            .openComponents();

        softAssertions.assertThat(componentsTreePage.getRowDetails(bigRing, newScenarioName3)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(pin, scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(smallRing, scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());

        cidComponentItemF = new EvaluatePage(driver).uploadComponent(bigRing, newScenarioName4, resourceFile2, currentUser);

        cidComponentItemG = new EvaluatePage(driver).uploadComponent(hingeAssembly, newScenarioName3, resourceFile, currentUser);
        componentsTreePage = new EvaluatePage(driver).navigateToScenario(cidComponentItemG)
            .openComponents();

        softAssertions.assertThat(componentsTreePage.getRowDetails(bigRing, newScenarioName3)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(pin, scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(smallRing, scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());

        softAssertions.assertAll();
    }

    @Test
    @Category(ExtendedRegression.class)
    @TestRail(testCaseId = {"21699", "21701", "21702", "21703"})
    @Description("Validate assembly association priority for Prefer Private strategy")
    public void testPreferPrivateAssemblyAssociationsWorkflow() {

        String asmStrategy = "PREFER_PRIVATE";

        Map<PreferencesEnum, String> updateStrategy = new HashMap<>();
        updateStrategy.put(PreferencesEnum.ASSEMBLY_STRATEGY, asmStrategy);

        userPreferencesUtil.updatePreferences(currentUser, updateStrategy);

        assemblyUtils.publishSubComponents(componentAssembly);

        loginPage = new CidAppLoginPage(driver);

        cidComponentItemA = loginPage.login(currentUser).uploadComponent(hingeAssembly, newScenarioName, resourceFile, currentUser);
        componentsTreePage = new EvaluatePage(driver).navigateToScenario(componentAssembly)
            .openComponents()
            .addColumn(ColumnsEnum.PUBLISHED);

        softAssertions.assertThat(componentsTreePage.getRowDetails(bigRing, scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(pin, scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(smallRing, scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());

        cidComponentItemB = new EvaluatePage(driver).uploadComponent(bigRing, newScenarioName2, resourceFile2, currentUser);
        evaluatePage = new EvaluatePage(driver).navigateToScenario(cidComponentItemB)
            .publishScenario(PublishPage.class)
            .publish(EvaluatePage.class);

        cidComponentItemC = new EvaluatePage(driver).uploadComponent(hingeAssembly, newScenarioName2, resourceFile, currentUser);
        componentsTreePage = new EvaluatePage(driver).navigateToScenario(cidComponentItemC)
            .openComponents();

        softAssertions.assertThat(componentsTreePage.getRowDetails(bigRing, newScenarioName2)).contains(StatusIconEnum.PUBLIC.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(pin, scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(smallRing, scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());

        cidComponentItemD = new EvaluatePage(driver).uploadComponent(bigRing, newScenarioName3, resourceFile2, currentUser);

        cidComponentItemE = new EvaluatePage(driver).uploadComponent(hingeAssembly, newScenarioName4, resourceFile, currentUser);
        componentsTreePage = new EvaluatePage(driver).navigateToScenario(cidComponentItemE)
            .openComponents();

        softAssertions.assertThat(componentsTreePage.getRowDetails(bigRing, newScenarioName3)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(pin, scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(smallRing, scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());

        cidComponentItemF = new EvaluatePage(driver).uploadComponent(bigRing, newScenarioName4, resourceFile2, currentUser);

        cidComponentItemG = new EvaluatePage(driver).uploadComponent(hingeAssembly, newScenarioName3, resourceFile, currentUser);
        componentsTreePage = new EvaluatePage(driver).navigateToScenario(cidComponentItemG)
            .openComponents();

        softAssertions.assertThat(componentsTreePage.getRowDetails(bigRing, newScenarioName3)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(pin, scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(smallRing, scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());

        softAssertions.assertAll();
    }

    @Test
    @Category(ExtendedRegression.class)
    @TestRail(testCaseId = {"21700", "21704", "21705", "21706"})
    @Description("Validate assembly association priority for Prefer Public strategy")
    public void testPreferPublicAssemblyAssociationsWorkflow() {

        String asmStrategy = "PREFER_PUBLIC";

        Map<PreferencesEnum, String> updateStrategy = new HashMap<>();
        updateStrategy.put(PreferencesEnum.ASSEMBLY_STRATEGY, asmStrategy);

        userPreferencesUtil.updatePreferences(currentUser, updateStrategy);

        loginPage = new CidAppLoginPage(driver);

        cidComponentItemA = loginPage.login(currentUser).uploadComponent(hingeAssembly, newScenarioName, resourceFile, currentUser);
        componentsTreePage = new EvaluatePage(driver).navigateToScenario(componentAssembly)
            .openComponents()
            .addColumn(ColumnsEnum.PUBLISHED);

        softAssertions.assertThat(componentsTreePage.getRowDetails(bigRing, scenarioName)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(pin, scenarioName)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(smallRing, scenarioName)).contains(StatusIconEnum.PRIVATE.getStatusIcon());

        cidComponentItemB = new EvaluatePage(driver).uploadComponent(bigRing, newScenarioName2, resourceFile2, currentUser);

        cidComponentItemC = new EvaluatePage(driver).uploadComponent(hingeAssembly, newScenarioName2, resourceFile, currentUser);
        componentsTreePage = new EvaluatePage(driver).navigateToScenario(cidComponentItemC)
            .openComponents();

        softAssertions.assertThat(componentsTreePage.getRowDetails(bigRing, newScenarioName2)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(pin, scenarioName)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(smallRing, scenarioName)).contains(StatusIconEnum.PRIVATE.getStatusIcon());

        cidComponentItemD = new EvaluatePage(driver).uploadComponent(bigRing, newScenarioName3, resourceFile2, currentUser);
        evaluatePage = new EvaluatePage(driver).navigateToScenario(cidComponentItemD)
            .publishScenario(PublishPage.class)
            .publish(EvaluatePage.class);

        cidComponentItemE = new EvaluatePage(driver).uploadComponent(hingeAssembly, newScenarioName4, resourceFile, currentUser);
        componentsTreePage = new EvaluatePage(driver).navigateToScenario(cidComponentItemE)
            .openComponents();

        softAssertions.assertThat(componentsTreePage.getRowDetails(bigRing, newScenarioName3)).contains(StatusIconEnum.PUBLIC.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(pin, scenarioName)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(smallRing, scenarioName)).contains(StatusIconEnum.PRIVATE.getStatusIcon());

        cidComponentItemF = new EvaluatePage(driver).uploadComponent(bigRing, newScenarioName4, resourceFile2, currentUser);
        evaluatePage = new EvaluatePage(driver).navigateToScenario(cidComponentItemF)
            .publishScenario(PublishPage.class)
            .publish(EvaluatePage.class);

        cidComponentItemG = new EvaluatePage(driver).uploadComponent(hingeAssembly, newScenarioName3, resourceFile, currentUser);
        componentsTreePage = new EvaluatePage(driver).navigateToScenario(cidComponentItemG)
            .openComponents();

        softAssertions.assertThat(componentsTreePage.getRowDetails(bigRing, newScenarioName3)).contains(StatusIconEnum.PUBLIC.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(pin, scenarioName)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(smallRing, scenarioName)).contains(StatusIconEnum.PRIVATE.getStatusIcon());

        softAssertions.assertAll();
    }
}
