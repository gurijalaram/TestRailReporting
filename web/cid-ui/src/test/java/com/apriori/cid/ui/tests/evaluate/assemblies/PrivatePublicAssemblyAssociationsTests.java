package com.apriori.cid.ui.tests.evaluate.assemblies;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.ASSEMBLY;

import com.apriori.cid.api.utils.AssemblyUtils;
import com.apriori.cid.api.utils.ScenariosUtil;
import com.apriori.cid.api.utils.UserPreferencesUtil;
import com.apriori.cid.ui.pageobjects.evaluate.EvaluatePage;
import com.apriori.cid.ui.pageobjects.evaluate.components.ComponentsTreePage;
import com.apriori.cid.ui.pageobjects.login.CidAppLoginPage;
import com.apriori.cid.ui.pageobjects.navtoolbars.PublishPage;
import com.apriori.cid.ui.utils.ColumnsEnum;
import com.apriori.cid.ui.utils.StatusIconEnum;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.enums.PreferencesEnum;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.FileResourceUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrivatePublicAssemblyAssociationsTests extends TestBaseUI {

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

    @BeforeEach
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

    @AfterEach
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
    @Tag(ASSEMBLY)
    @TestRail(id = {21707, 21708, 21709, 21710, 11955, 11958, 6600})
    @Description("Validate assembly association priority for Default strategy")
    public void testDefaultAssemblyAssociationsWorkflow() {

        assemblyUtils.publishSubComponents(componentAssembly);

        loginPage = new CidAppLoginPage(driver);

        cidComponentItemA = loginPage.login(currentUser).uploadComponent(hingeAssembly, newScenarioName, resourceFile, assemblyExtension, currentUser);
        componentsTreePage = new EvaluatePage(driver).navigateToScenario(componentAssembly)
            .openComponents()
            .addColumn(ColumnsEnum.PUBLISHED);

        softAssertions.assertThat(componentsTreePage.getRowDetails(bigRing, scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(pin, scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(smallRing, scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());

        cidComponentItemB = new EvaluatePage(driver).uploadComponent(bigRing, newScenarioName2, resourceFile2, subComponentExtension, currentUser);
        evaluatePage = new EvaluatePage(driver).navigateToScenario(cidComponentItemB)
            .publishScenario(PublishPage.class)
            .publish(EvaluatePage.class);

        cidComponentItemC = new EvaluatePage(driver).uploadComponent(hingeAssembly, newScenarioName2, resourceFile, assemblyExtension, currentUser);
        componentsTreePage = new EvaluatePage(driver).navigateToScenario(cidComponentItemC)
            .openComponents();

        softAssertions.assertThat(componentsTreePage.getRowDetails(bigRing, newScenarioName2)).contains(StatusIconEnum.PUBLIC.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(pin, scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(smallRing, scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());

        cidComponentItemD = new EvaluatePage(driver).uploadComponent(bigRing, newScenarioName3, resourceFile2, subComponentExtension, currentUser);

        cidComponentItemE = new EvaluatePage(driver).uploadComponent(hingeAssembly, newScenarioName4, resourceFile, assemblyExtension, currentUser);
        componentsTreePage = new EvaluatePage(driver).navigateToScenario(cidComponentItemE)
            .openComponents();

        softAssertions.assertThat(componentsTreePage.getRowDetails(bigRing, newScenarioName3)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(pin, scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(smallRing, scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());

        cidComponentItemF = new EvaluatePage(driver).uploadComponent(bigRing, newScenarioName4, resourceFile2, subComponentExtension, currentUser);

        cidComponentItemG = new EvaluatePage(driver).uploadComponent(hingeAssembly, newScenarioName3, resourceFile, assemblyExtension, currentUser);
        componentsTreePage = new EvaluatePage(driver).navigateToScenario(cidComponentItemG)
            .openComponents();

        softAssertions.assertThat(componentsTreePage.getRowDetails(bigRing, newScenarioName3)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(pin, scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(smallRing, scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());

        softAssertions.assertAll();
    }

    @Test
    @Tag(ASSEMBLY)
    @TestRail(id = {21699, 21701, 21702, 21703})
    @Description("Validate assembly association priority for Prefer Private strategy")
    public void testPreferPrivateAssemblyAssociationsWorkflow() {

        String asmStrategy = "PREFER_PRIVATE";

        Map<PreferencesEnum, String> updateStrategy = new HashMap<>();
        updateStrategy.put(PreferencesEnum.ASSEMBLY_STRATEGY, asmStrategy);

        userPreferencesUtil.updatePreferences(currentUser, updateStrategy);

        assemblyUtils.publishSubComponents(componentAssembly);

        loginPage = new CidAppLoginPage(driver);

        cidComponentItemA = loginPage.login(currentUser).uploadComponent(hingeAssembly, newScenarioName, resourceFile, assemblyExtension, currentUser);
        componentsTreePage = new EvaluatePage(driver).navigateToScenario(componentAssembly)
            .openComponents()
            .addColumn(ColumnsEnum.PUBLISHED);

        softAssertions.assertThat(componentsTreePage.getRowDetails(bigRing, scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(pin, scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(smallRing, scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());

        cidComponentItemB = new EvaluatePage(driver).uploadComponent(bigRing, newScenarioName2, resourceFile2, subComponentExtension, currentUser);
        evaluatePage = new EvaluatePage(driver).navigateToScenario(cidComponentItemB)
            .publishScenario(PublishPage.class)
            .publish(EvaluatePage.class);

        cidComponentItemC = new EvaluatePage(driver).uploadComponent(hingeAssembly, newScenarioName2, resourceFile, assemblyExtension, currentUser);
        componentsTreePage = new EvaluatePage(driver).navigateToScenario(cidComponentItemC)
            .openComponents();

        softAssertions.assertThat(componentsTreePage.getRowDetails(bigRing, newScenarioName2)).contains(StatusIconEnum.PUBLIC.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(pin, scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(smallRing, scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());

        cidComponentItemD = new EvaluatePage(driver).uploadComponent(bigRing, newScenarioName3, resourceFile2, subComponentExtension, currentUser);

        cidComponentItemE = new EvaluatePage(driver).uploadComponent(hingeAssembly, newScenarioName4, resourceFile, assemblyExtension, currentUser);
        componentsTreePage = new EvaluatePage(driver).navigateToScenario(cidComponentItemE)
            .openComponents();

        softAssertions.assertThat(componentsTreePage.getRowDetails(bigRing, newScenarioName3)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(pin, scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(smallRing, scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());

        cidComponentItemF = new EvaluatePage(driver).uploadComponent(bigRing, newScenarioName4, resourceFile2, subComponentExtension, currentUser);

        cidComponentItemG = new EvaluatePage(driver).uploadComponent(hingeAssembly, newScenarioName3, resourceFile, assemblyExtension, currentUser);
        componentsTreePage = new EvaluatePage(driver).navigateToScenario(cidComponentItemG)
            .openComponents();

        softAssertions.assertThat(componentsTreePage.getRowDetails(bigRing, newScenarioName3)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(pin, scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(smallRing, scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());

        softAssertions.assertAll();
    }

    @Test
    @Tag(ASSEMBLY)
    @TestRail(id = {21700, 21704, 21705, 21706})
    @Description("Validate assembly association priority for Prefer Public strategy")
    public void testPreferPublicAssemblyAssociationsWorkflow() {

        String asmStrategy = "PREFER_PUBLIC";

        Map<PreferencesEnum, String> updateStrategy = new HashMap<>();
        updateStrategy.put(PreferencesEnum.ASSEMBLY_STRATEGY, asmStrategy);

        userPreferencesUtil.updatePreferences(currentUser, updateStrategy);

        loginPage = new CidAppLoginPage(driver);

        cidComponentItemA = loginPage.login(currentUser).uploadComponent(hingeAssembly, newScenarioName, resourceFile, assemblyExtension, currentUser);
        componentsTreePage = new EvaluatePage(driver).navigateToScenario(componentAssembly)
            .openComponents()
            .addColumn(ColumnsEnum.PUBLISHED);

        softAssertions.assertThat(componentsTreePage.getRowDetails(bigRing, scenarioName)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(pin, scenarioName)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(smallRing, scenarioName)).contains(StatusIconEnum.PRIVATE.getStatusIcon());

        cidComponentItemB = new EvaluatePage(driver).uploadComponent(bigRing, newScenarioName2, resourceFile2, subComponentExtension, currentUser);

        cidComponentItemC = new EvaluatePage(driver).uploadComponent(hingeAssembly, newScenarioName2, resourceFile, assemblyExtension, currentUser);
        componentsTreePage = new EvaluatePage(driver).navigateToScenario(cidComponentItemC)
            .openComponents();

        softAssertions.assertThat(componentsTreePage.getRowDetails(bigRing, newScenarioName2)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(pin, scenarioName)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(smallRing, scenarioName)).contains(StatusIconEnum.PRIVATE.getStatusIcon());

        cidComponentItemD = new EvaluatePage(driver).uploadComponent(bigRing, newScenarioName3, resourceFile2, subComponentExtension, currentUser);
        evaluatePage = new EvaluatePage(driver).navigateToScenario(cidComponentItemD)
            .publishScenario(PublishPage.class)
            .publish(EvaluatePage.class);

        cidComponentItemE = new EvaluatePage(driver).uploadComponent(hingeAssembly, newScenarioName4, resourceFile, assemblyExtension, currentUser);
        componentsTreePage = new EvaluatePage(driver).navigateToScenario(cidComponentItemE)
            .openComponents();

        softAssertions.assertThat(componentsTreePage.getRowDetails(bigRing, newScenarioName3)).contains(StatusIconEnum.PUBLIC.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(pin, scenarioName)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(smallRing, scenarioName)).contains(StatusIconEnum.PRIVATE.getStatusIcon());

        cidComponentItemF = new EvaluatePage(driver).uploadComponent(bigRing, newScenarioName4, resourceFile2, subComponentExtension, currentUser);
        evaluatePage = new EvaluatePage(driver).navigateToScenario(cidComponentItemF)
            .publishScenario(PublishPage.class)
            .publish(EvaluatePage.class);

        cidComponentItemG = new EvaluatePage(driver).uploadComponent(hingeAssembly, newScenarioName3, resourceFile, assemblyExtension, currentUser);
        componentsTreePage = new EvaluatePage(driver).navigateToScenario(cidComponentItemG)
            .openComponents();

        softAssertions.assertThat(componentsTreePage.getRowDetails(bigRing, newScenarioName3)).contains(StatusIconEnum.PUBLIC.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(pin, scenarioName)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(smallRing, scenarioName)).contains(StatusIconEnum.PRIVATE.getStatusIcon());

        softAssertions.assertAll();
    }
}
