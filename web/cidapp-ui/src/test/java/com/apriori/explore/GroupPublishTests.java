package com.apriori.explore;

import static com.apriori.TestSuiteType.TestSuite.EXTENDED_REGRESSION;
import static com.apriori.enums.CssSearch.COMPONENT_NAME_EQ;
import static com.apriori.enums.CssSearch.SCENARIO_NAME_EQ;
import static com.apriori.enums.CssSearch.SCENARIO_STATE_EQ;

import com.apriori.FileResourceUtil;
import com.apriori.GenerateStringUtil;
import com.apriori.TestBaseUI;
import com.apriori.cidappapi.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.models.response.CostingTemplate;
import com.apriori.cidappapi.utils.AssemblyUtils;
import com.apriori.cidappapi.utils.ScenariosUtil;
import com.apriori.enums.ProcessGroupEnum;
import com.apriori.enums.ScenarioStateEnum;
import com.apriori.pageobjects.evaluate.EvaluatePage;
import com.apriori.pageobjects.explore.ExplorePage;
import com.apriori.pageobjects.login.CidAppLoginPage;
import com.apriori.pageobjects.navtoolbars.AssignPage;
import com.apriori.pageobjects.navtoolbars.InfoPage;
import com.apriori.pageobjects.navtoolbars.PublishPage;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testrail.TestRail;
import com.apriori.utils.CssComponent;

import com.utils.MultiUpload;
import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GroupPublishTests extends TestBaseUI {

    private final SoftAssertions softAssertions = new SoftAssertions();
    private final CssComponent cssComponent = new CssComponent();
    private final AssemblyUtils assemblyUtils = new AssemblyUtils();
    private final ScenariosUtil scenariosUtil = new ScenariosUtil();
    private UserCredentials currentUser;
    private CidAppLoginPage loginPage;
    private ExplorePage explorePage;
    private PublishPage publishPage;
    private AssignPage assignPage;
    private InfoPage infoPage;
    private ComponentInfoBuilder cidComponentItem;
    private ComponentInfoBuilder cidComponentItemA;

    @Test
    @TestRail(id = {14458})
    @Description("Publish multiple components")
    public void testGroupPublishPartScenarios() {
        final ProcessGroupEnum processGroupEnum1 = ProcessGroupEnum.PLASTIC_MOLDING;
        final String componentName1 = "titan charger lead";
        final File resourceFile1 = FileResourceUtil.getCloudFile(processGroupEnum1, componentName1 + ".SLDPRT");
        final String scenarioName1 = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        final ProcessGroupEnum processGroupEnum2 = ProcessGroupEnum.SHEET_METAL;
        final String componentName2 = "Part0004";
        final File resourceFile2 = FileResourceUtil.getCloudFile(processGroupEnum2, componentName2 + ".ipt");
        final String scenarioName2 = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);

        cidComponentItem = loginPage.login(currentUser)
            .uploadComponent(componentName1, scenarioName1, resourceFile1, currentUser);

        cidComponentItemA = new ExplorePage(driver).uploadComponent(componentName2, scenarioName2, resourceFile2, currentUser);
        List<MultiUpload> multiComponents = new ArrayList<>();
        multiComponents.add(new MultiUpload(resourceFile1, scenarioName1));
        multiComponents.add(new MultiUpload(resourceFile2, scenarioName2));

        explorePage = new ExplorePage(driver)
            .refresh()
            .multiSelectScenarios("" + componentName1 + ", " + scenarioName1 + "", "" + componentName2 + ", " + scenarioName2 + "")
            .publishScenario(PublishPage.class)
            .override()
            .clickContinue(PublishPage.class)
            .publish(PublishPage.class)
            .close(ExplorePage.class);

        multiComponents.forEach(component ->
            softAssertions.assertThat(cssComponent.getComponentParts(currentUser, COMPONENT_NAME_EQ.getKey() + component.getResourceFile().getName().split("\\.")[0],
                SCENARIO_NAME_EQ.getKey() + component.getScenarioName(), SCENARIO_STATE_EQ.getKey() + ScenarioStateEnum.NOT_COSTED)).hasSizeGreaterThan(0));

        explorePage.refresh()
            .selectFilter("Public");

        softAssertions.assertThat(explorePage.getListOfScenarios(componentName1, scenarioName1)).isEqualTo(1);
        softAssertions.assertThat(explorePage.getListOfScenarios(componentName2, scenarioName2)).isEqualTo(1);

        softAssertions.assertAll();
    }

    @Test
    @Tag(EXTENDED_REGRESSION)
    @TestRail(id = {14463, 14460})
    @Description("Publish multiple assemblies")
    public void testGroupPublishAssemblyScenarios() {
        currentUser = UserUtil.getUser();
        final String assemblyName1 = "Hinge assembly";
        final String assemblyExtension1 = ".SLDASM";
        final ProcessGroupEnum assemblyProcessGroup1 = ProcessGroupEnum.ASSEMBLY;
        final List<String> subComponentNames1 = Arrays.asList("big ring", "Pin", "small ring");
        final String subComponentExtension1 = ".SLDPRT";
        final ProcessGroupEnum subComponentProcessGroup1 = ProcessGroupEnum.FORGING;

        final String assemblyScenarioName1 = new GenerateStringUtil().generateScenarioName();

        ComponentInfoBuilder componentAssembly1 = assemblyUtils.associateAssemblyAndSubComponents(
            assemblyName1,
            assemblyExtension1,
            assemblyProcessGroup1,
            subComponentNames1,
            subComponentExtension1,
            subComponentProcessGroup1,
            assemblyScenarioName1,
            currentUser);

        // TODO: 20/02/2023 cn - nick pls fix these templates
        CostingTemplate pinTemplate = CostingTemplate.builder().processGroupName(ProcessGroupEnum.CASTING_DIE.getProcessGroup()).productionLife(9.0).build();
        CostingTemplate bigRingTemplate = CostingTemplate.builder().processGroupName(ProcessGroupEnum.POWDER_METAL.getProcessGroup()).annualVolume(700).build();

        scenariosUtil.setSubcomponentCostingTemplate(componentAssembly1, pinTemplate, "pin", "small ring");
        scenariosUtil.setSubcomponentCostingTemplate(componentAssembly1, bigRingTemplate, "big ring");

        assemblyUtils.uploadSubComponents(componentAssembly1).uploadAssembly(componentAssembly1);
        assemblyUtils.costSubComponents(componentAssembly1).costAssembly(componentAssembly1);
        assemblyUtils.publishSubComponents(componentAssembly1);

        final String assemblyName2 = "flange c";
        final String assemblyExtension2 = ".CATProduct";
        final ProcessGroupEnum assemblyProcessGroup2 = ProcessGroupEnum.ASSEMBLY;
        final List<String> subComponentNames2 = Arrays.asList("flange", "nut", "bolt");
        final String subComponentExtension2 = ".CATPart";
        final ProcessGroupEnum subComponentProcessGroup2 = ProcessGroupEnum.PLASTIC_MOLDING;

        final String assemblyScenarioName2 = new GenerateStringUtil().generateScenarioName();

        ComponentInfoBuilder componentAssembly2 = assemblyUtils.associateAssemblyAndSubComponents(
            assemblyName2,
            assemblyExtension2,
            assemblyProcessGroup2,
            subComponentNames2,
            subComponentExtension2,
            subComponentProcessGroup2,
            assemblyScenarioName2,
            currentUser);

        // TODO: 20/02/2023 cn - nick pls fix these templates
        CostingTemplate costingTemplate = CostingTemplate.builder().processGroupName(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup()).build();

        scenariosUtil.setSubcomponentCostingTemplate(componentAssembly2, costingTemplate, "flange", "nut", "bolt");

        assemblyUtils.uploadSubComponents(componentAssembly2).uploadAssembly(componentAssembly2);
        assemblyUtils.costSubComponents(componentAssembly2).costAssembly(componentAssembly2);
        assemblyUtils.publishSubComponents(componentAssembly2);

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(currentUser)
            .multiSelectScenarios("" + assemblyName1 + ", " + assemblyScenarioName1 + "", "" + assemblyName2 + ", " + assemblyScenarioName2 + "")
            .publishScenario(PublishPage.class)
            .override()
            .clickContinue(PublishPage.class)
            .publish(PublishPage.class)
            .close(ExplorePage.class)
            .checkComponentStateRefresh(componentAssembly1, ScenarioStateEnum.COST_COMPLETE)
            .checkComponentStateRefresh(componentAssembly2, ScenarioStateEnum.COST_COMPLETE)
            .refresh()
            .selectFilter("Public");

        softAssertions.assertThat(explorePage.getListOfScenarios(assemblyName1, assemblyScenarioName1)).isEqualTo(1);
        softAssertions.assertThat(explorePage.getListOfScenarios(assemblyName2, assemblyScenarioName2)).isEqualTo(1);

        explorePage.multiSelectScenarios("" + assemblyName1 + ", " + assemblyScenarioName1 + "", "" + assemblyName2 + ", " + assemblyScenarioName2 + "");

        softAssertions.assertThat(explorePage.isPublishButtonEnabled()).isEqualTo(false);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {14459})
    @Description("Publish a mix of components and assemblies")
    public void testGroupPublishComponentAndAssembly() {
        currentUser = UserUtil.getUser();
        final String assemblyName1 = "Hinge assembly";
        final String assemblyExtension1 = ".SLDASM";
        final ProcessGroupEnum assemblyProcessGroup1 = ProcessGroupEnum.ASSEMBLY;
        final List<String> subComponentNames1 = Arrays.asList("big ring", "Pin", "small ring");
        final String subComponentExtension1 = ".SLDPRT";
        final ProcessGroupEnum subComponentProcessGroup1 = ProcessGroupEnum.FORGING;

        final String assemblyScenarioName1 = new GenerateStringUtil().generateScenarioName();

        ComponentInfoBuilder componentAssembly1 = assemblyUtils.associateAssemblyAndSubComponents(
            assemblyName1,
            assemblyExtension1,
            assemblyProcessGroup1,
            subComponentNames1,
            subComponentExtension1,
            subComponentProcessGroup1,
            assemblyScenarioName1,
            currentUser);

        assemblyUtils.uploadSubComponents(componentAssembly1).uploadAssembly(componentAssembly1);

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(currentUser)
            .multiSelectScenarios("" + "big ring" + ", " + assemblyScenarioName1 + "");

        softAssertions.assertThat(explorePage.isPublishButtonEnabled()).isEqualTo(true);

        explorePage.multiSelectScenarios("" + assemblyName1 + ", " + assemblyScenarioName1 + "");

        softAssertions.assertThat(explorePage.isPublishButtonEnabled()).isEqualTo(false);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {14464, 21551})
    @Description("Publish multiple components and set inputs in modal")
    public void testGroupPublishWithInputs() {
        final ProcessGroupEnum processGroupEnum1 = ProcessGroupEnum.PLASTIC_MOLDING;
        final String componentName1 = "titan charger lead";
        final File resourceFile1 = FileResourceUtil.getCloudFile(processGroupEnum1, componentName1 + ".SLDPRT");
        final String scenarioName1 = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        final ProcessGroupEnum processGroupEnum2 = ProcessGroupEnum.SHEET_METAL;
        final String componentName2 = "Part0004";
        final File resourceFile2 = FileResourceUtil.getCloudFile(processGroupEnum2, componentName2 + ".ipt");
        final String scenarioName2 = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);

        cidComponentItem = loginPage.login(currentUser)
            .uploadComponent(componentName1, scenarioName1, resourceFile1, currentUser);

        cidComponentItemA = new ExplorePage(driver).uploadComponent(componentName2, scenarioName2, resourceFile2, currentUser);

        String scenarioAssignedTo = scenariosUtil.getScenarioCompleted(cidComponentItem).getCreatedByName();

        List<MultiUpload> multiComponents = new ArrayList<>();
        multiComponents.add(new MultiUpload(resourceFile1, scenarioName1));
        multiComponents.add(new MultiUpload(resourceFile2, scenarioName2));

        publishPage = new ExplorePage(driver).refresh()
            .multiSelectScenarios("" + componentName1 + ", " + scenarioName1 + "", "" + componentName2 + ", " + scenarioName2 + "")
            .publishScenario(PublishPage.class)
            .override()
            .clickContinue(PublishPage.class);

        softAssertions.assertThat(publishPage.getAssociationAlert()).contains("High maturity and complete status scenarios can be prioritized to make more accurate associations when uploading new assemblies.");

        publishPage.selectStatus("Analysis")
            .selectCostMaturity("Low")
            .selectAssignee(currentUser)
            .publish(PublishPage.class);

        explorePage = publishPage.close(ExplorePage.class);

        multiComponents.forEach(component ->
            softAssertions.assertThat(cssComponent.getComponentParts(currentUser, COMPONENT_NAME_EQ.getKey() + component.getResourceFile().getName().split("\\.")[0],
                SCENARIO_NAME_EQ.getKey() + component.getScenarioName(), SCENARIO_STATE_EQ.getKey() + ScenarioStateEnum.NOT_COSTED)).hasSizeGreaterThan(0));

        assignPage = explorePage.refresh()
            .selectFilter("Public")
            .openScenario(componentName1, scenarioName1)
            .clickActions()
            .assign();

        softAssertions.assertThat(assignPage.isAssigneeDisplayed(scenarioAssignedTo)).isEqualTo(true);

        infoPage = assignPage.cancel(EvaluatePage.class)
            .clickActions()
            .info();

        softAssertions.assertThat(infoPage.getCostMaturity()).isEqualTo("Low");
        softAssertions.assertThat(infoPage.getStatus()).isEqualTo("Analysis");

        infoPage.cancel(EvaluatePage.class)
            .clickExplore()
            .openScenario(componentName2, scenarioName2)
            .clickActions()
            .assign();

        softAssertions.assertThat(assignPage.isAssigneeDisplayed(scenarioAssignedTo)).isEqualTo(true);

        infoPage = assignPage.cancel(EvaluatePage.class)
            .clickActions()
            .info();
        softAssertions.assertThat(infoPage.getCostMaturity()).isEqualTo("Low");
        softAssertions.assertThat(infoPage.getStatus()).isEqualTo("Analysis");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {14461, 14462})
    @Description("Attempt to publish more than 10 components/assemblies")
    public void testGroupPublishMoreThanTenScenarios() {
        currentUser = UserUtil.getUser();
        final String scenarioName = new GenerateStringUtil().generateScenarioName();
        List<MultiUpload> multiComponents = new ArrayList<>();
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.FORGING, "big ring.SLDPRT"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.PLASTIC_MOLDING, "titan charger lead.SLDPRT"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.FORGING, "small ring.SLDPRT"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, "Part0004.ipt"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, "bracket_basic.prt"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.POWDER_METAL, "PowderMetalShaft.stp"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.PLASTIC_MOLDING, "Push Pin.stp"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.CASTING_INVESTMENT, "piston cover_model1.prt"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, "Part0005b.ipt"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.CASTING_INVESTMENT, "piston rod_model1.prt"), scenarioName));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.CASTING_INVESTMENT, "piston_model1.prt"), scenarioName));

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(currentUser)
            .importCadFile()
            .inputScenarioName(scenarioName)
            .inputMultiComponents(multiComponents)
            .submit()
            .clickClose()
            .setPagination()
            .selectFilter("Recent")
            .multiSelectScenarios("" + "piston rod_model1" + ", " + scenarioName + "",
                "" + "Part0005b" + ", " + scenarioName + "");

        softAssertions.assertThat(explorePage.isPublishButtonEnabled()).isEqualTo(false);

        multiComponents.forEach(component ->
            softAssertions.assertThat(cssComponent.getComponentParts(currentUser, COMPONENT_NAME_EQ.getKey() + component.getResourceFile().getName().split("\\.")[0],
                SCENARIO_NAME_EQ.getKey() + component.getScenarioName(), SCENARIO_STATE_EQ.getKey() + ScenarioStateEnum.NOT_COSTED)).hasSizeGreaterThan(0));

        explorePage.refresh()
            .setPagination()
            .multiSelectScenarios("" + "piston rod_model1" + ", " + scenarioName + "",
                "" + "Part0005b" + ", " + scenarioName + "",
                "" + "piston cover_model1" + ", " + scenarioName + "",
                "" + "Push Pin" + ", " + scenarioName + "",
                "" + "PowderMetalShaft" + ", " + scenarioName + "",
                "" + "bracket_basic" + ", " + scenarioName + "",
                "" + "Part0004" + ", " + scenarioName + "",
                "" + "small ring" + ", " + scenarioName + "",
                "" + "titan charger lead" + ", " + scenarioName + "",
                "" + "big ring" + ", " + scenarioName + "");

        softAssertions.assertThat(explorePage.isPublishButtonEnabled()).isEqualTo(true);

        explorePage.multiSelectScenarios("" + "piston_model1" + ", " + scenarioName + "");

        softAssertions.assertThat(explorePage.isPublishButtonEnabled()).isEqualTo(false);

        softAssertions.assertAll();
    }
}
