package com.apriori.cid.ui.tests.evaluate.assemblies;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.ASSEMBLY;
import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.EXTENDED_REGRESSION;

import com.apriori.cid.api.utils.AssemblyUtils;
import com.apriori.cid.api.utils.ScenariosUtil;
import com.apriori.cid.api.utils.UserPreferencesUtil;
import com.apriori.cid.ui.pageobjects.evaluate.EvaluatePage;
import com.apriori.cid.ui.pageobjects.evaluate.components.ComponentsTreePage;
import com.apriori.cid.ui.pageobjects.explore.ExplorePage;
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

public class MaturityAssemblyAssociationsTests extends TestBaseUI {

    private AssemblyUtils assemblyUtils = new AssemblyUtils();
    private UserPreferencesUtil userPreferencesUtil = new UserPreferencesUtil();
    private ScenariosUtil scenariosUtil = new ScenariosUtil();
    private CidAppLoginPage loginPage;
    private EvaluatePage evaluatePage;
    private ExplorePage explorePage;
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
    private ComponentInfoBuilder cidComponentItemH;
    private List<ComponentInfoBuilder> listOfSubcomponents;

    private String fuseBlockAsm;
    private String conductor;
    private String housing;
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
    private String asmStrategy;

    @BeforeEach
    public void testSetup() {
        fuseBlockAsm = "Auto_Fuse_Block_Asm";
        assemblyProcessGroup = ProcessGroupEnum.ASSEMBLY;
        assemblyExtension = ".CATProduct";
        conductor = "Auto_Conductor";
        housing = "Auto_Fuse_Connector_Housing";
        subComponentExtension = ".CATPart";
        subComponentNames = Arrays.asList(conductor, housing);
        subComponentProcessGroup = ProcessGroupEnum.FORGING;
        resourceFile = FileResourceUtil.getCloudFile(assemblyProcessGroup, fuseBlockAsm + assemblyExtension);
        resourceFile2 = FileResourceUtil.getCloudFile(subComponentProcessGroup, conductor + subComponentExtension);
        asmStrategy = "PREFER_HIGH_MATURITY";

        currentUser = UserUtil.getUser();

        scenarioName = new GenerateStringUtil().generateScenarioName();
        newScenarioName = new GenerateStringUtil().generateScenarioName();
        newScenarioName2 = new GenerateStringUtil().generateScenarioName();
        newScenarioName3 = new GenerateStringUtil().generateScenarioName();
        newScenarioName4 = new GenerateStringUtil().generateScenarioName();

        Map<PreferencesEnum, String> updateStrategy = new HashMap<>();
        updateStrategy.put(PreferencesEnum.ASSEMBLY_STRATEGY, asmStrategy);

        userPreferencesUtil.updatePreferences(currentUser, updateStrategy);

        componentAssembly = assemblyUtils.associateAssemblyAndSubComponents(
            fuseBlockAsm,
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

            assemblyUtils.deleteAssemblyAndComponents(componentAssembly);
        }

        listOfSubcomponents = Arrays.asList(cidComponentItemA, cidComponentItemB, cidComponentItemC, cidComponentItemD, cidComponentItemE, cidComponentItemF,
            cidComponentItemG, cidComponentItemH);

        listOfSubcomponents.forEach(subcomponent -> {
            if (subcomponent != null) {
                scenariosUtil.deleteScenario(subcomponent.getComponentIdentity(), subcomponent.getScenarioIdentity(), currentUser);
            }
        });
    }

    @Test
    @Tag(ASSEMBLY)
    @TestRail(id = {21671, 21687, 21688})
    @Description("Validate, with Prefer Maturity strategy, private sub-components with low and medium maturity")
    public void testMaturityPresetPrivateLowAndMedium() {

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(currentUser)
            .multiSelectScenarios(conductor + "," + scenarioName)
            .clickActions()
            .info()
            .inputCostMaturity("Low")
            .submit(ExplorePage.class)
            .selectFilter("Recent")
            .multiSelectScenarios(housing + "," + scenarioName)
            .clickActions()
            .info()
            .inputCostMaturity("Low")
            .submit(ExplorePage.class)
            .addColumn(ColumnsEnum.COST_MATURITY);

        softAssertions.assertThat(explorePage.getRowDetails(conductor, scenarioName)).contains("Low");
        softAssertions.assertThat(explorePage.getRowDetails(housing, scenarioName)).contains("Low");

        cidComponentItemA = new ExplorePage(driver).uploadComponent(fuseBlockAsm, newScenarioName, resourceFile, assemblyExtension, currentUser);
        componentsTreePage = new ExplorePage(driver).navigateToScenario(cidComponentItemA)
            .openComponents()
            .addColumn(ColumnsEnum.COST_MATURITY)
            .addColumn(ColumnsEnum.SCENARIO_TYPE)
            .addColumn(ColumnsEnum.PUBLISHED);

        softAssertions.assertThat(componentsTreePage.getRowDetails(conductor, scenarioName)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(conductor, scenarioName)).contains(StatusIconEnum.VERIFIED.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(conductor, scenarioName)).contains("Low");
        softAssertions.assertThat(componentsTreePage.getRowDetails(housing, scenarioName)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(housing, scenarioName)).contains(StatusIconEnum.VERIFIED.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(housing, scenarioName)).contains("Low");

        componentsTreePage.closePanel()
            .clickExplore();

        cidComponentItemB = new ExplorePage(driver).uploadComponent(conductor, newScenarioName, resourceFile2, subComponentExtension, currentUser);
        evaluatePage = new ExplorePage(driver).navigateToScenario(cidComponentItemB)
            .clickActions()
            .info()
            .inputCostMaturity("Medium")
            .submit(EvaluatePage.class);

        cidComponentItemD = new EvaluatePage(driver).uploadComponent(fuseBlockAsm, newScenarioName2, resourceFile, assemblyExtension, currentUser);
        componentsTreePage = new EvaluatePage(driver).navigateToScenario(cidComponentItemD)
            .openComponents();

        softAssertions.assertThat(componentsTreePage.getRowDetails(conductor, newScenarioName)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(conductor, newScenarioName)).contains(StatusIconEnum.VERIFIED.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(conductor, newScenarioName)).contains("Medium");
        softAssertions.assertThat(componentsTreePage.getRowDetails(housing, scenarioName)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(housing, scenarioName)).contains(StatusIconEnum.VERIFIED.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(housing, scenarioName)).contains("Low");

        componentsTreePage.closePanel()
            .clickExplore();

        cidComponentItemC = new ExplorePage(driver).uploadComponent(conductor, newScenarioName2, resourceFile2, subComponentExtension, currentUser);
        evaluatePage = new ExplorePage(driver).navigateToScenario(cidComponentItemC)
            .clickActions()
            .info()
            .inputCostMaturity("Medium")
            .submit(EvaluatePage.class);

        cidComponentItemD = new EvaluatePage(driver).uploadComponent(fuseBlockAsm, newScenarioName3, resourceFile, assemblyExtension, currentUser);
        componentsTreePage = new EvaluatePage(driver).navigateToScenario(cidComponentItemD)
            .openComponents();

        softAssertions.assertThat(componentsTreePage.getRowDetails(conductor, newScenarioName2)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(conductor, newScenarioName2)).contains(StatusIconEnum.VERIFIED.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(conductor, newScenarioName2)).contains("Medium");
        softAssertions.assertThat(componentsTreePage.getRowDetails(housing, scenarioName)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(housing, scenarioName)).contains(StatusIconEnum.VERIFIED.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(housing, scenarioName)).contains("Low");

        softAssertions.assertAll();
    }

    @Test
    @Tag(ASSEMBLY)
    @TestRail(id = {21689, 21690, 21691})
    @Description("Validate, with Prefer Maturity strategy, private sub-components with high maturity and cost complete status")
    public void testMaturityPresetPrivateHighComplete() {

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(currentUser)
            .multiSelectScenarios(conductor + "," + scenarioName)
            .clickActions()
            .info()
            .inputCostMaturity("High")
            .submit(ExplorePage.class)
            .selectFilter("Recent")
            .multiSelectScenarios(housing + "," + scenarioName)
            .clickActions()
            .info()
            .inputCostMaturity("High")
            .submit(ExplorePage.class)
            .addColumn(ColumnsEnum.COST_MATURITY);

        softAssertions.assertThat(explorePage.getRowDetails(conductor, scenarioName)).contains("High");
        softAssertions.assertThat(explorePage.getRowDetails(housing, scenarioName)).contains("High");

        cidComponentItemA = new ExplorePage(driver).uploadComponent(fuseBlockAsm, newScenarioName, resourceFile, assemblyExtension, currentUser);
        componentsTreePage = new ExplorePage(driver).navigateToScenario(cidComponentItemA)
            .openComponents()
            .addColumn(ColumnsEnum.COST_MATURITY)
            .addColumn(ColumnsEnum.SCENARIO_TYPE)
            .addColumn(ColumnsEnum.PUBLISHED);

        softAssertions.assertThat(componentsTreePage.getRowDetails(conductor, scenarioName)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(conductor, scenarioName)).contains(StatusIconEnum.VERIFIED.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(conductor, scenarioName)).contains("High");
        softAssertions.assertThat(componentsTreePage.getRowDetails(housing, scenarioName)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(housing, scenarioName)).contains(StatusIconEnum.VERIFIED.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(housing, scenarioName)).contains("High");

        componentsTreePage.closePanel()
            .clickExplore();

        cidComponentItemB = new ExplorePage(driver).uploadComponent(conductor, newScenarioName, resourceFile2, subComponentExtension, currentUser);
        evaluatePage = new ExplorePage(driver).navigateToScenario(cidComponentItemB)
            .clickActions()
            .info()
            .inputCostMaturity("Medium")
            .selectStatus("Complete")
            .submit(EvaluatePage.class);

        cidComponentItemD = new EvaluatePage(driver).uploadComponent(fuseBlockAsm, newScenarioName2, resourceFile, assemblyExtension, currentUser);
        componentsTreePage = new EvaluatePage(driver).navigateToScenario(cidComponentItemD)
            .openComponents()
            .addColumn(ColumnsEnum.STATUS);

        softAssertions.assertThat(componentsTreePage.getRowDetails(conductor, newScenarioName)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(conductor, newScenarioName)).contains(StatusIconEnum.VERIFIED.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(conductor, newScenarioName)).contains("Medium");
        softAssertions.assertThat(componentsTreePage.getRowDetails(conductor, newScenarioName)).contains("Complete");
        softAssertions.assertThat(componentsTreePage.getRowDetails(housing, scenarioName)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(housing, scenarioName)).contains(StatusIconEnum.VERIFIED.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(housing, scenarioName)).contains("High");

        componentsTreePage.closePanel()
            .clickExplore();

        cidComponentItemC = new ExplorePage(driver).uploadComponent(conductor, newScenarioName2, resourceFile2, subComponentExtension, currentUser);
        evaluatePage = new ExplorePage(driver).navigateToScenario(cidComponentItemC)
            .clickActions()
            .info()
            .inputCostMaturity("High")
            .selectStatus("Complete")
            .submit(EvaluatePage.class);

        cidComponentItemD = new EvaluatePage(driver).uploadComponent(fuseBlockAsm, newScenarioName3, resourceFile, assemblyExtension, currentUser);
        componentsTreePage = new EvaluatePage(driver).navigateToScenario(cidComponentItemD)
            .openComponents();

        softAssertions.assertThat(componentsTreePage.getRowDetails(conductor, newScenarioName2)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(conductor, newScenarioName2)).contains(StatusIconEnum.VERIFIED.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(conductor, newScenarioName2)).contains("High");
        softAssertions.assertThat(componentsTreePage.getRowDetails(conductor, newScenarioName2)).contains("Complete");
        softAssertions.assertThat(componentsTreePage.getRowDetails(housing, scenarioName)).contains(StatusIconEnum.PRIVATE.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(housing, scenarioName)).contains(StatusIconEnum.VERIFIED.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(housing, scenarioName)).contains("High");

        softAssertions.assertAll();
    }

    @Test
    @Tag(ASSEMBLY)
    @TestRail(id = {21692, 21693, 21694, 21695})
    @Description("Validate, with Prefer Maturity strategy, public sub-components with same scenario name and low then medium maturity")
    public void testMaturityPresetPublicWithSameNameLowAndMedium() {

        assemblyUtils.publishSubComponents(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(currentUser);

        cidComponentItemH = new ExplorePage(driver).uploadComponent(conductor, scenarioName, resourceFile2, subComponentExtension, currentUser);
        cidComponentItemA = new ExplorePage(driver).uploadComponent(fuseBlockAsm, newScenarioName, resourceFile, assemblyExtension, currentUser);
        componentsTreePage = new ExplorePage(driver).navigateToScenario(cidComponentItemA)
            .openComponents()
            .addColumn(ColumnsEnum.PUBLISHED);

        softAssertions.assertThat(componentsTreePage.getRowDetails(conductor, scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(housing, scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());

        cidComponentItemB = new ExplorePage(driver).uploadComponent(conductor, newScenarioName, resourceFile2, subComponentExtension, currentUser);
        evaluatePage = new ExplorePage(driver).navigateToScenario(cidComponentItemB)
            .clickActions()
            .info()
            .inputCostMaturity("Low")
            .submit(EvaluatePage.class)
            .publishScenario(PublishPage.class)
            .publish(EvaluatePage.class);

        cidComponentItemC = new EvaluatePage(driver).uploadComponent(fuseBlockAsm, newScenarioName2, resourceFile, assemblyExtension, currentUser);
        componentsTreePage = new EvaluatePage(driver).navigateToScenario(cidComponentItemC)
            .openComponents()
            .addColumn(ColumnsEnum.COST_MATURITY);

        softAssertions.assertThat(componentsTreePage.getRowDetails(conductor, newScenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(conductor, newScenarioName)).contains("Low");
        softAssertions.assertThat(componentsTreePage.getRowDetails(housing, scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());

        componentsTreePage.closePanel()
            .clickDeleteIcon()
            .clickDelete(ExplorePage.class);

        cidComponentItemD = new ExplorePage(driver).uploadComponent(conductor, newScenarioName3, resourceFile2, subComponentExtension, currentUser);
        evaluatePage = new ExplorePage(driver).navigateToScenario(cidComponentItemD)
            .clickActions()
            .info()
            .inputCostMaturity("Medium")
            .submit(EvaluatePage.class)
            .publishScenario(PublishPage.class)
            .publish(EvaluatePage.class);

        cidComponentItemE = new EvaluatePage(driver).uploadComponent(fuseBlockAsm, newScenarioName2, resourceFile, assemblyExtension, currentUser);
        componentsTreePage = new EvaluatePage(driver).navigateToScenario(cidComponentItemE)
            .openComponents();

        softAssertions.assertThat(componentsTreePage.getRowDetails(conductor, newScenarioName3)).contains(StatusIconEnum.PUBLIC.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(conductor, newScenarioName3)).contains("Medium");
        softAssertions.assertThat(componentsTreePage.getRowDetails(housing, scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());

        componentsTreePage.closePanel()
            .clickDeleteIcon()
            .clickDelete(ExplorePage.class);

        cidComponentItemF = new ExplorePage(driver).uploadComponent(conductor, newScenarioName4, resourceFile2, subComponentExtension, currentUser);
        evaluatePage = new ExplorePage(driver).navigateToScenario(cidComponentItemF)
            .clickActions()
            .info()
            .inputCostMaturity("Medium")
            .submit(EvaluatePage.class)
            .publishScenario(PublishPage.class)
            .publish(EvaluatePage.class);

        cidComponentItemG = new EvaluatePage(driver).uploadComponent(fuseBlockAsm, newScenarioName2, resourceFile, assemblyExtension, currentUser);
        componentsTreePage = new EvaluatePage(driver).navigateToScenario(cidComponentItemG)
            .openComponents();

        softAssertions.assertThat(componentsTreePage.getRowDetails(conductor, newScenarioName4)).contains(StatusIconEnum.PUBLIC.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(conductor, newScenarioName4)).contains("Medium");
        softAssertions.assertThat(componentsTreePage.getRowDetails(housing, scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());

        softAssertions.assertAll();
    }

    @Test
    @Tag(ASSEMBLY)
    @TestRail(id = {21696, 21697, 21698})
    @Description("Validate, with Prefer Maturity strategy, public sub-components with high maturity and cost complete status")
    public void testMaturityPresetPublicHighComplete() {

        assemblyUtils.publishSubComponents(componentAssembly);

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(currentUser)
            .selectFilter("Public")
            .multiSelectScenarios(conductor + "," + scenarioName)
            .clickActions()
            .info()
            .inputCostMaturity("High")
            .submit(ExplorePage.class)
            .selectFilter("Public")
            .multiSelectScenarios(housing + "," + scenarioName)
            .clickActions()
            .info()
            .inputCostMaturity("High")
            .submit(ExplorePage.class)
            .selectFilter("Private")
            .multiSelectScenarios(fuseBlockAsm + "," + scenarioName)
            .clickDeleteIcon()
            .clickDelete(ExplorePage.class);

        cidComponentItemA = new ExplorePage(driver).uploadComponent(fuseBlockAsm, scenarioName, resourceFile, assemblyExtension, currentUser);
        componentsTreePage = new ExplorePage(driver).navigateToScenario(cidComponentItemA)
            .openComponents()
            .addColumn(ColumnsEnum.PUBLISHED);

        softAssertions.assertThat(componentsTreePage.getRowDetails(conductor, scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(housing, scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());

        cidComponentItemB = new ExplorePage(driver).uploadComponent(conductor, newScenarioName, resourceFile2, subComponentExtension, currentUser);
        evaluatePage = new ExplorePage(driver).navigateToScenario(cidComponentItemB)
            .clickActions()
            .info()
            .inputCostMaturity("Medium")
            .selectStatus("Complete")
            .submit(EvaluatePage.class)
            .publishScenario(PublishPage.class)
            .publish(EvaluatePage.class);

        cidComponentItemC = new EvaluatePage(driver).uploadComponent(fuseBlockAsm, newScenarioName2, resourceFile, assemblyExtension, currentUser);
        componentsTreePage = new EvaluatePage(driver).navigateToScenario(cidComponentItemC)
            .openComponents()
            .addColumn(ColumnsEnum.COST_MATURITY)
            .addColumn(ColumnsEnum.STATUS);

        softAssertions.assertThat(componentsTreePage.getRowDetails(conductor, newScenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(conductor, newScenarioName)).contains("Medium");
        softAssertions.assertThat(componentsTreePage.getRowDetails(conductor, newScenarioName)).contains("Complete");
        softAssertions.assertThat(componentsTreePage.getRowDetails(housing, scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());

        componentsTreePage.closePanel()
            .clickDeleteIcon()
            .clickDelete(ExplorePage.class);

        cidComponentItemD = new ExplorePage(driver).uploadComponent(conductor, newScenarioName3, resourceFile2, subComponentExtension, currentUser);
        evaluatePage = new ExplorePage(driver).navigateToScenario(cidComponentItemD)
            .clickActions()
            .info()
            .inputCostMaturity("High")
            .selectStatus("Complete")
            .submit(EvaluatePage.class)
            .publishScenario(PublishPage.class)
            .publish(EvaluatePage.class);

        cidComponentItemE = new EvaluatePage(driver).uploadComponent(fuseBlockAsm, newScenarioName2, resourceFile, assemblyExtension, currentUser);
        componentsTreePage = new EvaluatePage(driver).navigateToScenario(cidComponentItemE)
            .openComponents();

        softAssertions.assertThat(componentsTreePage.getRowDetails(conductor, newScenarioName3)).contains(StatusIconEnum.PUBLIC.getStatusIcon());
        softAssertions.assertThat(componentsTreePage.getRowDetails(conductor, newScenarioName3)).contains("High");
        softAssertions.assertThat(componentsTreePage.getRowDetails(conductor, newScenarioName3)).contains("Complete");
        softAssertions.assertThat(componentsTreePage.getRowDetails(housing, scenarioName)).contains(StatusIconEnum.PUBLIC.getStatusIcon());

        softAssertions.assertAll();
    }
}
